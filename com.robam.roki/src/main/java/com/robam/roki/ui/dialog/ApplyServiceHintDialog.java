package com.robam.roki.ui.dialog;

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
public class ApplyServiceHintDialog extends AbsDialog {

    static public void show(Context cx) {
        ApplyServiceHintDialog dlg = new ApplyServiceHintDialog(cx);
        dlg.show();
    }

    public ApplyServiceHintDialog(Context cx) {
        super(cx, R.style.Theme_Dialog_FullScreen);
        ViewUtils.setFullScreen(cx, this);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_apply_service_hint;
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

    @OnClick(R.id.layout)
    public void onClick() {
        dismiss();
    }
}
