package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ViewUtils;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/17.
 */
public class CookPreviewDialog extends AbsDialog {

    static public void show(Context cx, int count, int index, String desc) {
        CookPreviewDialog dlg = new CookPreviewDialog(cx, count, index, desc);
        dlg.show();
    }


    @InjectView(R.id.txtStepIndex)
    TextView txtStepIndex;
    @InjectView(R.id.txtStepCount)
    TextView txtStepCount;
    @InjectView(R.id.txtStepDesc)
    TextView txtStepDesc;

    int index, count;
    String desc;

    public CookPreviewDialog(Context cx, int count, int index, String desc) {
        super(cx, R.style.Theme_Dialog_FullScreen);
        ViewUtils.setFullScreen(cx, this);
        this.count = count;
        this.index = index;
        this.desc = desc;

        txtStepIndex.setText(String.valueOf(index + 1));
        txtStepCount.setText(String.format("/%s >", count));
        txtStepDesc.setText(desc);
    }

    @OnClick(R.id.layout)
    public void onClick() {
        this.dismiss();
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_cook_preview;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }
}
