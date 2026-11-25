// Нативная JNI-обёртка llama.cpp для ScanBase (вкладка «Отзыв ИИ»).
//
// Восстановлена по соседней обёртке bw2work/jni/bw2llama.cpp (тот же форк llama.cpp:
// llama_process / llama_batch_ext_*), под сигнатуры ru.scanbase.LlamaModel:
//   init(path, ctx_size, threads) -> handle
//   generate(handle, prompt, max_tokens, seconds, mode, sink) -> String
//   release(handle)
//
// Главное отличие от прежнего libscanllama.so: вместо llama_sampler_init_greedy
// ставится цепочка DRY + repeat-penalty + top_k + top_p + temp + dist. Жадная
// выборка на маленькой Qwen-1.5B проваливалась в петли («Это средство …» ×N);
// DRY и штраф за повторы именно это и лечат. Промпт идёт одним user-сообщением
// через chat-шаблон из GGUF (маленькая Qwen без ChatML отвечает хуже).

#include <llama.h>

#include <android/log.h>
#include <jni.h>

#include <algorithm>
#include <chrono>
#include <cstdint>
#include <cstring>
#include <ctime>
#include <string>
#include <vector>

#define TAG "ScanLlama"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

namespace {

struct Handle {
    llama_model * model = nullptr;
    llama_context * ctx = nullptr;
    llama_sampler * smpl = nullptr;
    llama_batch_ext * batch = nullptr;
};

std::string applyTemplate(llama_model * model, const std::string & user) {
    const char * tmpl = llama_model_chat_template(model, nullptr);
    if (!tmpl) {
        return user;
    }

    std::vector<llama_chat_message> chat;
    chat.push_back({ "user", user.c_str() });

    int32_t need = llama_chat_apply_template(tmpl, chat.data(), chat.size(), true, nullptr, 0);
    if (need <= 0) {
        return user;
    }

    std::vector<char> buf((size_t) need + 16);
    int32_t n = llama_chat_apply_template(tmpl, chat.data(), chat.size(), true, buf.data(), (int32_t) buf.size());
    if (n <= 0) {
        return user;
    }

    return std::string(buf.data(), (size_t) n);
}

}  // namespace

extern "C" {

JNIEXPORT jlong JNICALL
Java_ru_scanbase_LlamaModel_init(JNIEnv * env, jclass, jstring jpath, jint ctx_size, jint threads) {
    const char * path = env->GetStringUTFChars(jpath, nullptr);
    if (!path || !*path) {
        if (path) {
            env->ReleaseStringUTFChars(jpath, path);
        }
        return 0;
    }

    llama_backend_init();

    llama_model_params mp = llama_model_default_params();
    mp.n_gpu_layers = 0;

    llama_model * model = llama_model_load_from_file(path, mp);
    env->ReleaseStringUTFChars(jpath, path);
    if (!model) {
        LOGE("model load failed");
        return 0;
    }

    llama_context_params cp = llama_context_default_params();
    cp.n_ctx = (uint32_t) std::max(512, (int) ctx_size);
    cp.n_batch = std::min((uint32_t) 512, cp.n_ctx);
    cp.n_ubatch = cp.n_batch;
    cp.n_seq_max = 1;
    cp.n_threads = std::max(1, (int) threads);
    cp.n_threads_batch = cp.n_threads;

    llama_context * ctx = llama_init_from_model(model, cp);
    if (!ctx) {
        LOGE("context init failed");
        llama_model_free(model);
        return 0;
    }

    llama_batch_ext * batch = llama_batch_ext_init(ctx);
    if (!batch) {
        LOGE("batch init failed");
        llama_free(ctx);
        llama_model_free(model);
        return 0;
    }

    auto * h = new Handle();
    h->model = model;
    h->ctx = ctx;
    h->batch = batch;
    LOGI("model ready ctx=%d threads=%d", (int) cp.n_ctx, (int) cp.n_threads);
    return (jlong) (intptr_t) h;
}

JNIEXPORT jstring JNICALL
Java_ru_scanbase_LlamaModel_generate(JNIEnv * env, jclass, jlong jhandle, jstring jprompt,
                                     jint max_tokens, jint seconds, jint mode, jobject sink) {
    auto * h = (Handle *) (intptr_t) jhandle;
    if (!h || !h->ctx || !h->model || !jprompt) {
        return env->NewStringUTF("");
    }

    const char * cprompt = env->GetStringUTFChars(jprompt, nullptr);
    std::string full = applyTemplate(h->model, cprompt ? cprompt : "");
    if (cprompt) {
        env->ReleaseStringUTFChars(jprompt, cprompt);
    }

    const llama_vocab * vocab = llama_model_get_vocab(h->model);
    int32_t cap = (int32_t) (full.size() + 64);
    std::vector<llama_token> tokens((size_t) cap);
    int32_t n = llama_tokenize(vocab, full.data(), (int32_t) full.size(), tokens.data(), cap, true, true);
    if (n < 0) {
        tokens.resize((size_t) (-n));
        n = llama_tokenize(vocab, full.data(), (int32_t) full.size(), tokens.data(), (int32_t) tokens.size(), true, true);
    }
    if (n <= 0) {
        LOGE("tokenize failed n=%d", n);
        return env->NewStringUTF("");
    }

    llama_token bos = llama_vocab_bos(vocab);
    if (bos >= 0 && tokens[0] == bos && n > 1) {
        tokens.erase(tokens.begin());
        n -= 1;
    }

    int limit = std::max(16, (int) max_tokens);
    int nBatch = std::max(1, (int) llama_n_batch(h->ctx));
    int nCtx = (int32_t) llama_n_ctx(h->ctx);
    int maxPrompt = std::max(64, nCtx - limit - 64);
    if (n > maxPrompt) {
        tokens.erase(tokens.begin(), tokens.begin() + (n - maxPrompt));
        n = maxPrompt;
    }

    if (h->smpl) {
        llama_sampler_free(h->smpl);
        h->smpl = nullptr;
    }

    // Анти-повторная цепочка. DRY и repeat-penalty модифицируют логиты, затем
    // temp/top_k/top_p + dist. Порядок модификаторов до сэмплирования.
    const char * breakers[] = { "\n", " ", "\"", "'" };
    h->smpl = llama_sampler_chain_init(llama_sampler_chain_default_params());
    llama_sampler_chain_add(h->smpl, llama_sampler_init_dry(vocab, 1.05f, 1.75f, 2, 256, breakers, 4));
    llama_sampler_chain_add(h->smpl, llama_sampler_init_penalties(llama_vocab_n_tokens(vocab), 64, 1.15f, 0.0f, 0.0f));
    llama_sampler_chain_add(h->smpl, llama_sampler_init_top_k(40));
    llama_sampler_chain_add(h->smpl, llama_sampler_init_top_p(0.85f, 1));
    llama_sampler_chain_add(h->smpl, llama_sampler_init_temp(0.4f));
    llama_sampler_chain_add(h->smpl, llama_sampler_init_dist((uint32_t) time(nullptr)));

    auto t0 = std::chrono::steady_clock::now();
    double budget = seconds > 0 ? (double) seconds : 100.0;

    llama_memory_t mem = llama_get_memory(h->ctx);
    llama_memory_clear(mem, false);
    llama_pos pos = llama_memory_seq_pos_max(mem, 0) + 1;

    for (int start = 0; start < n; start += nBatch) {
        int len = std::min(nBatch, n - start);
        llama_batch_ext_clear(h->batch);
        for (int i = 0; i < len; ++i) {
            int32_t idx = llama_batch_ext_add_token(h->batch, 0, tokens[(size_t) (start + i)]);
            if (idx < 0) {
                LOGE("add prompt token failed idx=%d", idx);
                return env->NewStringUTF("");
            }
            llama_pos p = pos + (llama_pos) i;
            llama_batch_ext_set_pos(h->batch, idx, &p);
        }
        llama_batch_ext_set_output_logits(h->batch, len - 1, true);
        if (llama_process(h->ctx, LLAMA_PROCESS_TYPE_DECODE, h->batch) != 0) {
            LOGE("decode prompt failed start=%d len=%d", start, len);
            return env->NewStringUTF("");
        }
        pos += (llama_pos) len;
    }

    jmethodID onPiece = nullptr;
    if (sink) {
        jclass cls = env->GetObjectClass(sink);
        onPiece = env->GetMethodID(cls, "onPiece", "(Ljava/lang/String;)V");
    }

    std::string text;
    int nOut = 0;
    for (int i = 0; i < limit; ++i) {
        double el = std::chrono::duration<double>(std::chrono::steady_clock::now() - t0).count();
        if (el > budget) {
            break;
        }

        llama_token tok = llama_sampler_sample(h->smpl, h->ctx, -1);
        llama_sampler_accept(h->smpl, tok);
        if (llama_vocab_is_eog(vocab, tok)) {
            break;
        }

        char piece[256];
        int32_t len = llama_token_to_piece(vocab, tok, piece, sizeof(piece), 0, false);
        if (len > 0) {
            text.append(piece, (size_t) len);
            nOut++;
            if (onPiece) {
                std::string s(piece, (size_t) len);
                jstring jp = env->NewStringUTF(s.c_str());
                env->CallVoidMethod(sink, onPiece, jp);
                env->DeleteLocalRef(jp);
            }
        }

        llama_batch_ext_clear(h->batch);
        int32_t idx = llama_batch_ext_add_token(h->batch, 0, tok);
        if (idx < 0) {
            break;
        }
        llama_batch_ext_set_pos(h->batch, idx, &pos);
        llama_batch_ext_set_output_logits(h->batch, idx, true);
        if (llama_process(h->ctx, LLAMA_PROCESS_TYPE_DECODE, h->batch) != 0) {
            break;
        }
        pos += 1;
    }

    double secs = std::chrono::duration<double>(std::chrono::steady_clock::now() - t0).count();
    LOGI("generate done tokens=%d secs=%.2f tok_s=%.1f", nOut, secs, nOut / (secs > 0.01 ? secs : 0.01));

    return env->NewStringUTF(text.c_str());
}

JNIEXPORT void JNICALL
Java_ru_scanbase_LlamaModel_release(JNIEnv *, jclass, jlong jhandle) {
    auto * h = (Handle *) (intptr_t) jhandle;
    if (!h) {
        return;
    }
    if (h->batch) {
        llama_batch_ext_free(h->batch);
    }
    if (h->smpl) {
        llama_sampler_free(h->smpl);
    }
    if (h->ctx) {
        llama_free(h->ctx);
    }
    if (h->model) {
        llama_model_free(h->model);
    }
    delete h;
}

}  // extern "C"
