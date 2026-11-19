package ru.scanbase;

import android.content.Context;
import android.util.Log;
import java.io.File;

/* JADX INFO: loaded from: /home/karpov-m/work/scanbase/apk/unpacked/classes.dex */
public class LlamaModel {
    private static long handle = 0;
    public static String lastError = "";
    private static boolean libsLoaded = false;

    public static boolean deleteModel(Context context) {
        releaseModel();
        return modelFile(context).delete();
    }

    public static void dropLegacyCopy(Context context) {
        File file = new File(context.getFilesDir(), "model.gguf");
        if (file.exists() && file.length() > 10000000 && file.delete()) {
            Log.i("ScanLlama", "удалён внутренний дубликат модели");
        }
    }

    private static native String generate(long j, String str, int i, int i2, int i3, AiSink aiSink);

    public static String generate(Context context, String str, int i, int i2) {
        return generate(context, str, i, i2, 3, (AiSink) null);
    }

    public static synchronized String generate(Context context, String str, int i, int i2, int i3, AiSink aiSink) {
        synchronized (LlamaModel.class) {
            lastError = "";
            try {
                loadLibs(context);
                if (!hasModel(context)) {
                    lastError = "model.gguf не найден";
                    return "";
                }
                if (handle == 0) {
                    long jInit = init(modelFile(context).getAbsolutePath(), 4096, Math.max(1, Math.min(4, Runtime.getRuntime().availableProcessors() - 1)));
                    handle = jInit;
                    if (jInit == 0) {
                        lastError = "init failed";
                        return "";
                    }
                }
                String strGenerate = generate(handle, str, i, i2, i3, aiSink);
                return strGenerate == null ? "" : strGenerate.trim();
            } catch (Throwable th) {
                String strValueOf = String.valueOf(th);
                lastError = strValueOf;
                Log.e("ScanLlama", strValueOf, th);
                return "";
            }
        }
    }

    public static boolean hasModel(Context context) {
        File fileModelFile = modelFile(context);
        return fileModelFile.exists() && fileModelFile.length() > 10000000;
    }

    private static native long init(String str, int i, int i2);

    private static void loadLibs(Context context) {
        if (libsLoaded) {
            return;
        }
        String[] strArr = {"c++_shared", "omp", "ggml-base", "ggml-cpu", "ggml", "llama", "scanllama"};
        for (int i = 0; i < 7; i++) {
            try {
                System.loadLibrary(strArr[i]);
            } catch (Throwable th) {
                Log.i("ScanLlama", "loadLibrary не сработал, пробую пути: " + String.valueOf(th));
                String str = context.getApplicationInfo().nativeLibraryDir;
                if (str == null || !new File(str).isDirectory()) {
                    throw new IllegalStateException("нет каталога нативных библиотек");
                }
                for (int i2 = 0; i2 < 7; i2++) {
                    System.load(new File(str, "lib" + strArr[i2] + ".so").getAbsolutePath());
                }
                libsLoaded = true;
                return;
            }
        }
        libsLoaded = true;
    }

    public static File modelFile(Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir == null) {
            externalFilesDir = context.getFilesDir();
        }
        return new File(externalFilesDir, "model.gguf");
    }

    public static long modelSize(Context context) {
        File fileModelFile = modelFile(context);
        if (fileModelFile.exists()) {
            return fileModelFile.length();
        }
        return 0L;
    }

    private static native void release(long j);

    public static synchronized void releaseModel() {
        long j = handle;
        handle = 0L;
        if (j != 0) {
            try {
                release(j);
            } catch (Throwable unused) {
            }
        }
    }
}
