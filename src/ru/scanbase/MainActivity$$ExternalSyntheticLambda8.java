package ru.scanbase;

import android.webkit.WebView;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: /home/karpov-m/work/scanbase/apk/unpacked/classes.dex */
public final /* synthetic */ class MainActivity$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ WebView f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ MainActivity$$ExternalSyntheticLambda8(WebView webView, String str) {
        this.f$0 = webView;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.loadDataWithBaseURL(null, this.f$1, "text/html", "utf-8", null);
    }
}
