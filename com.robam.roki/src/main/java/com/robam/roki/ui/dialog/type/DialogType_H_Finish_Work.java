package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robam.roki.R;
import com.robam.roki.ui.dialog.BaseDialog;

/**
 * Created by yinwei on 2017/12/19.
 */

public class DialogType_H_Finish_Work extends BaseDialog {

    private TextView mCancelTv;
    private TextView mOkTv;
//    private View mMiddleDivider;
//    private TextView mContxt;
    public DialogType_H_Finish_Work(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.commom_dialog_finish_work_h, null);
        mTitleTv = rootView.findViewById(R.id.tv_title);
        mCancelTv = rootView.findViewById(R.id.btn_cancel);
        mOkTv = rootView.findViewById(R.id.btn_finish);
//        mContxt = rootView.findViewById(R.id.common_dialog_content_text);
//        mMiddleDivider = rootView.findViewById(R.id.common_dialog_btn_middle_divider);
        createDialog(rootView);
    }

    @Override
    public void setCancelBtn(CharSequence text, View.OnClickListener cancelOnClickListener) {
        mCancelTv.setText(text);
        setOnCancelClickListener(cancelOnClickListener);
    }

    @Override
    public void setCancelBtn(int textId, View.OnClickListener cancelOnClickListener) {
        mCancelTv.setText(textId);
        setOnCancelClickListener(cancelOnClickListener);
    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        mOkTv.setText(textId);
        setOnOkClickListener(okOnClickListener);
    }

    @Override
    public void setOkBtn(CharSequence text, View.OnClickListener okOnClickListener) {
        mOkTv.setText(text);
        setOnOkClickListener(okOnClickListener);
    }


    @Override
    public void bindAllListeners() {
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick(v);
            }
        });
        mOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick(v);
            }
        });
    }



    @Override
    public void setContentText(int contentStrId) {
        super.setContentText(contentStrId);
//        mContxt.setText(contentStrId);
    }

    @Override
    public void setContentText(CharSequence contentStr) {
//        mContxt.setText(contentStr);
    }
}
