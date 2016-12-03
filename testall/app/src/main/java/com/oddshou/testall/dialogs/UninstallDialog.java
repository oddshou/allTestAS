package com.oddshou.testall.dialogs;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by win7 on 2016/12/3.
 */

public class UninstallDialog extends Dialog {
    public UninstallDialog(Context context) {
        this(context, android.R.style.Theme_Holo_Dialog);
    }

    public UninstallDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected UninstallDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
