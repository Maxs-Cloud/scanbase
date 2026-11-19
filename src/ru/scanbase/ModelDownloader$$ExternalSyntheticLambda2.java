package ru.scanbase;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: /home/karpov-m/work/scanbase/apk/unpacked/classes.dex */
public final /* synthetic */ class ModelDownloader$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ModelDownloader.Callback f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ ModelDownloader$$ExternalSyntheticLambda2(ModelDownloader.Callback callback, long j, long j2) {
        this.f$0 = callback;
        this.f$1 = j;
        this.f$2 = j2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.onProgress(this.f$1, this.f$2);
    }
}
