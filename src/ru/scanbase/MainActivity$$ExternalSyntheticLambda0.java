package ru.scanbase;

import android.view.KeyEvent;
import android.widget.TextView;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: /home/karpov-m/work/scanbase/apk/unpacked/classes.dex */
public final /* synthetic */ class MainActivity$$ExternalSyntheticLambda0 implements TextView.OnEditorActionListener {
    public final /* synthetic */ MainActivity f$0;

    public /* synthetic */ MainActivity$$ExternalSyntheticLambda0(MainActivity mainActivity) {
        this.f$0 = mainActivity;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$onCreate$0(textView, i, keyEvent);
    }
}
