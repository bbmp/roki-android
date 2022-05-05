package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robam.roki.R;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;

/**
 * 藏宝盒发现新版本
 */
public class DialogDiscoverNewVersion extends BaseDialog {

    private TextView mContentText;
    private TextView mCancelBtn;
    private TextView mOkBtn;
    private View rootView;

    public DialogDiscoverNewVersion(Context context) {

        super(context);
    }

    @Override
    public void initDialog() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_discover_new_version, null);
        mTitleTv = rootView.findViewById(R.id.common_dialog_title_text);
        mContentText = rootView.findViewById(R.id.tv_content_text);
        mCancelBtn = rootView.findViewById(R.id.tv_cancel_btn);
        mOkBtn = rootView.findViewById(R.id.tv_ok_btn);
        createDialog(rootView);
    }

    @Override
    public void setTitleText(int titleStrId) {
        super.setTitleText(titleStrId);
        mTitleTv.setText(titleStrId);
    }

    @Override
    public void setContentText(int contentStrId) {
        mContentText.setText(contentStrId);
    }

    @Override
    public void setContentText(CharSequence contentStr) {

        String[] array = ((String) contentStr).split(";");
        StringBuilder sb = new StringBuilder();
        String append;
        for (int i = 0; i < array.length; i++) {
            if (i < array.length - 1) {
                append = sb.append(array[i]).append(";").append("\n").toString();
            } else {
                append = sb.append(array[i]).append("\n").toString();
            }
            mContentText.setText(append);
        }
    }


    @Override
    public void setCancelBtn(CharSequence text, View.OnClickListener cancelOnClickListener) {
        mCancelBtn.setText(text);
        setOnCancelClickListener(cancelOnClickListener);
    }

    @Override
    public void setCancelBtn(int textId, View.OnClickListener cancelOnClickListener) {
        mCancelBtn.setText(textId);
        setOnCancelClickListener(cancelOnClickListener);
    }


    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        mOkBtn.setText(textId);
        setOnOkClickListener(okOnClickListener);
    }

    @Override
    public void setOkBtn(CharSequence text, View.OnClickListener okOnClickListener) {
        mOkBtn.setText(text);
        setOnOkClickListener(okOnClickListener);
    }

    @Override
    public void bindAllListeners() {
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick(v);
            }
        });
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick(v);
            }
        });
    }

    @Override
    public CoreDialog getCoreDialog() {
        return new CoreDialog(mContext, R.style.dialog, rootView, true);
    }
}
