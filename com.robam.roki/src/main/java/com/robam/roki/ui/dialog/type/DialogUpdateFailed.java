package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robam.roki.R;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;

/**
 * 藏宝盒更新失败
 */
public class DialogUpdateFailed extends BaseDialog {

    private TextView mContentText;
    private TextView mTvOk;
    private TextView mTvCancel;
    private View rootView;

    public DialogUpdateFailed(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_update_failed, null);
        mTitleTv = rootView.findViewById(R.id.tv_title);
        mContentText = rootView.findViewById(R.id.tv_content_text);
        mTvOk = rootView.findViewById(R.id.tv_ok_btn);
        mTvCancel = rootView.findViewById(R.id.tv_cancel_btn);
        createDialog(rootView);
    }

    @Override
    public void bindAllListeners() {
        mTvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick(v);
            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick(v);
            }
        });
    }

    @Override
    public void setTitleText(int titleStrId) {
        mTitleTv.setText(titleStrId);
    }

    @Override
    public void setTitleText(CharSequence titleStr) {
        mTitleTv.setText(titleStr);
    }

    @Override
    public void setContentText(int contentStrId) {
        mContentText.setText(contentStrId);
    }

    @Override
    public void setContentText(CharSequence contentStr) {
        mContentText.setText(contentStr);
    }

    @Override
    public void setOkBtn(CharSequence text, View.OnClickListener okOnClickListener) {
        mTvOk.setText(text);
        setOnOkClickListener(okOnClickListener);
    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        mTvOk.setText(textId);
        setOnOkClickListener(okOnClickListener);
    }

    @Override
    public void setCancelBtn(int textId, View.OnClickListener cancelOnClickListener) {
        mTvCancel.setText(textId);
        setOnCancelClickListener(cancelOnClickListener);
    }


    @Override
    public void setCancelBtn(CharSequence text, View.OnClickListener cancelOnClickListener) {
        mTvCancel.setText(text);
        setOnCancelClickListener(cancelOnClickListener);
    }

    @Override
    public CoreDialog getCoreDialog() {
        return new CoreDialog(mContext, R.style.dialog, rootView, true);
    }
}
