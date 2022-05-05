package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;

import butterknife.ButterKnife;

/**
 * Created by linxiaobin on 2016/1/4.
 */
public class DeviceOvenStartNoticeDialog extends AbsDialog {
    public DeviceOvenStartNoticeDialog(Context context) {
        super(context);
    }

    public void init() {

    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_oven_notice_start;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    static public void show(Context cx) {
        DeviceOvenStartNoticeDialog dlg = new DeviceOvenStartNoticeDialog(cx);
        Window win = dlg.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        dlg.setCancelable(false);
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();

    }

    static public void dismiss(Context cx) {
        dismiss(cx);
    }
}

