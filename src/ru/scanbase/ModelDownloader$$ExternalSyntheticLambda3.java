package ru.scanbase;

import android.content.Context;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: /home/karpov-m/work/scanbase/apk/unpacked/classes.dex */
public final /* synthetic */ class ModelDownloader$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ ModelDownloader f$0;
    public final /* synthetic */ Context f$1;
    public final /* synthetic */ ModelDownloader.Callback f$2;

    public /* synthetic */ ModelDownloader$$ExternalSyntheticLambda3(ModelDownloader modelDownloader, Context context, ModelDownloader.Callback callback) {
        this.f$0 = modelDownloader;
        this.f$1 = context;
        this.f$2 = callback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$start$0(this.f$1, this.f$2);
    }
}
