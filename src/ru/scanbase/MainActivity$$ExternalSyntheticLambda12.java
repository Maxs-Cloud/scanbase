package ru.scanbase;

import android.webkit.ValueCallback;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: /home/karpov-m/work/scanbase/apk/unpacked/classes.dex */
public final /* synthetic */ class MainActivity$$ExternalSyntheticLambda12 implements ValueCallback {
    public final /* synthetic */ MainActivity f$0;

    public /* synthetic */ MainActivity$$ExternalSyntheticLambda12(MainActivity mainActivity) {
        this.f$0 = mainActivity;
    }

    @Override // android.webkit.ValueCallback
    public final void onReceiveValue(Object obj) {
        this.f$0.lambda$lambda$runExtraction$0$0((String) obj);
    }
}
