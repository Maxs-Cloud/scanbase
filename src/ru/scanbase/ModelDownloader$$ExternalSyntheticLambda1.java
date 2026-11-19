package ru.scanbase;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: /home/karpov-m/work/scanbase/apk/unpacked/classes.dex */
public final /* synthetic */ class ModelDownloader$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ModelDownloader.Callback f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ ModelDownloader$$ExternalSyntheticLambda1(ModelDownloader.Callback callback, String str) {
        this.f$0 = callback;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.onFail(this.f$1);
    }
}
