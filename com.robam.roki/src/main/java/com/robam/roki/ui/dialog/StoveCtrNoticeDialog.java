package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.View;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ViewUtils;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhaiyuanyi on 2015/12/17.
 */
public class StoveCtrNoticeDialog extends AbsDialog {
    static public void show(Context cx) {
        StoveCtrNoticeDialog dlg = new StoveCtrNoticeDialog(cx);
        dlg.show();
    }

    public StoveCtrNoticeDialog(Context context) {
        super(context, R.style.Theme_Dialog_FullScreen);
        ViewUtils.setFullScreen(cx, this);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_stove_ctrview;
    }
    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 2000);
    }

    @OnClick(R.id.layout)
    public void onClick() {
        dismiss();
    }
}
