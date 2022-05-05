package com.robam.roki.ui.dialog.type;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robam.roki.R;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;

/**
 * 左上角 标题,报警码，中部文本内容，底部一个按钮，居中弹出
 */
public class DialogType_02 extends BaseDialog {

    private TextView mOkTv;
    private TextView mContentTv;

    public DialogType_02(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_type_02, null);
        mTitleTv = rootView.findViewById(R.id.common_dialog_title_text);
        mTitleAralmCodeTv = rootView.findViewById(R.id.common_dialog_title_aralm_text);
        mContentTv = rootView.findViewById(R.id.common_dialog_content_text);
        mOkTv = rootView.findViewById(R.id.common_dialog_center_one_btn);
        createDialog(rootView);
    }


    @Override
    public void setTitleText(CharSequence titleStr) {
        super.setTitleText(titleStr);
    }

    @Override
    public void setTitleText(int titleStrId) {
        super.setTitleText(titleStrId);
    }

    @Override
    public void setOkBtn(CharSequence text, View.OnClickListener okOnClickListener) {
        mOkTv.setText(text);
        setOnOkClickListener(okOnClickListener);
    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        mOkTv.setText(textId);
        setOnOkClickListener(okOnClickListener);
    }

    @Override
    public void setContentText(CharSequence contentStr) {
        mContentTv.setText(contentStr);
    }

    @Override
    public void setContentText(int contentStrId) {
        mContentTv.setText(contentStrId);
    }

    @Override
    public void bindAllListeners() {

        mOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick(v);
            }
        });
    }



}
