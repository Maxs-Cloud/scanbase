package ru.scanbase;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/* JADX INFO: loaded from: /home/karpov-m/work/scanbase/apk/unpacked/classes.dex */
public class ModelDownloader {
    static final String[] URLS = {"https://hf-mirror.com/Qwen/Qwen2.5-1.5B-Instruct-GGUF/resolve/main/qwen2.5-1.5b-instruct-q4_k_m.gguf", "https://huggingface.co/Qwen/Qwen2.5-1.5B-Instruct-GGUF/resolve/main/qwen2.5-1.5b-instruct-q4_k_m.gguf"};
    private Thread thread;
    private volatile boolean cancelled = false;
    private final Handler ui = new Handler(Looper.getMainLooper());

    public interface Callback {
        void onDone(String str);

        void onFail(String str);

        void onProgress(long j, long j2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: download, reason: merged with bridge method [inline-methods] */
    public void lambda$start$0(Context context, Callback callback) {
        File file = new File(LlamaModel.modelFile(context).getParentFile(), "model.gguf.part");
        String str = "нет сети";
        for (String str2 : URLS) {
            if (this.cancelled) {
                postFail(callback, "отменено");
                return;
            }
            try {
                if (fetch(str2, file, callback)) {
                    File fileModelFile = LlamaModel.modelFile(context);
                    if (fileModelFile.exists() && !fileModelFile.delete()) {
                        postFail(callback, "не удалось заменить старую модель");
                        return;
                    }
                    if (!file.renameTo(fileModelFile)) {
                        postFail(callback, "не удалось перенести файл модели");
                        return;
                    }
                    Log.i("ScanBase", "модель скачана: " + fileModelFile.length() + " байт из " + str2);
                    postDone(callback, host(str2));
                    return;
                }
                str = "файл докачался, но не совпал по размеру";
            } catch (Exception e) {
                String string = e.getMessage() == null ? e.toString() : e.getMessage();
                Log.i("ScanBase", "загрузка с " + host(str2) + " не вышла: " + string);
                str = string;
            }
        }
        postFail(callback, str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v0 */
    /* JADX WARN: Type inference failed for: r12v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r12v2 */
    private boolean fetch(String str, File file, Callback callback) throws Exception {
        long j;
        InputStream inputStream;
        int i;
        long j2;
        long length = file.exists() ? file.length() : 0L;
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        boolean z = true;
        httpURLConnection.setInstanceFollowRedirects(true);
        httpURLConnection.setConnectTimeout(20000);
        httpURLConnection.setReadTimeout(40000);
        if (length > 0) {
            httpURLConnection.setRequestProperty("Range", "bytes=" + length + "-");
        }
        int responseCode = httpURLConnection.getResponseCode();
        boolean z2 = 0;
        boolean z3 = responseCode == 206;
        if (responseCode != 200 && responseCode != 206) {
            httpURLConnection.disconnect();
            throw new IOException("http " + responseCode);
        }
        long contentLength = httpURLConnection.getContentLength();
        if (z3) {
            String headerField = httpURLConnection.getHeaderField("Content-Range");
            long j3 = (headerField == null || headerField.lastIndexOf(47) < 0) ? 0L : Long.parseLong(headerField.substring(headerField.lastIndexOf(47) + 1).trim());
            j = j3 > 0 ? j3 : contentLength + length;
        } else {
            j = contentLength;
            length = 0;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file, z3);
        InputStream inputStream2 = httpURLConnection.getInputStream();
        byte[] bArr = new byte[262144];
        long j4 = 0;
        while (!this.cancelled && (i = inputStream2.read(bArr)) > 0) {
            try {
                fileOutputStream.write(bArr, z2, i);
                long j5 = ((long) i) + length;
                if (j5 - j4 > 3000000 || (j > 0 && j5 >= j)) {
                    inputStream = inputStream2;
                    try {
                        postProgress(callback, j5, j);
                        j2 = j5;
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream.close();
                        inputStream.close();
                        httpURLConnection.disconnect();
                        throw th;
                    }
                } else {
                    j2 = j4;
                    inputStream = inputStream2;
                }
                bArr = bArr;
                inputStream2 = inputStream;
                j4 = j2;
                length = j5;
                z = true;
                z2 = 0;
            } catch (Throwable th2) {
                th = th2;
                inputStream = inputStream2;
            }
        }
        fileOutputStream.close();
        inputStream2.close();
        httpURLConnection.disconnect();
        if (this.cancelled) {
            return z2;
        }
        return (j <= 0 || file.length() == j) ? z : z2;
    }

    private static String host(String str) {
        return str.replaceAll("^https?://", "").replaceAll("/.*", "");
    }

    private void postDone(final Callback callback, final String str) {
        this.ui.post(new Runnable() { // from class: ru.scanbase.ModelDownloader$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                callback.onDone(str);
            }
        });
    }

    private void postFail(final Callback callback, final String str) {
        this.ui.post(new Runnable() { // from class: ru.scanbase.ModelDownloader$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                callback.onFail(str);
            }
        });
    }

    private void postProgress(final Callback callback, final long j, final long j2) {
        this.ui.post(new Runnable() { // from class: ru.scanbase.ModelDownloader$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                callback.onProgress(j, j2);
            }
        });
    }

    public void cancel() {
        this.cancelled = true;
    }

    public boolean running() {
        Thread thread = this.thread;
        return thread != null && thread.isAlive();
    }

    public void start(final Context context, final Callback callback) {
        if (running()) {
            return;
        }
        this.cancelled = false;
        Thread thread = new Thread(new Runnable() { // from class: ru.scanbase.ModelDownloader$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$start$0(context, callback);
            }
        }, "model-download");
        this.thread = thread;
        thread.start();
    }
}
