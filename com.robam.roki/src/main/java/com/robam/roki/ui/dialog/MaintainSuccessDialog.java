package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ViewUtils;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/20.
 */
public class MaintainSuccessDialog extends AbsDialog {

    static public Dialog show(Context cx) {
        MaintainSuccessDialog dlg = new MaintainSuccessDialog(cx);
        dlg.show();
        return dlg;
    }

    public MaintainSuccessDialog(Context cx) {
        super(cx, R.style.Theme_Dialog_FullScreen);
        ViewUtils.setFullScreen(cx, this);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_maintain_success;
    }

    @OnClick(R.id.layout)
    public void onClick() {
        dismiss();
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 3000);
    }
}
