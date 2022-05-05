package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.robam.roki.R;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;

/**
 * 藏宝盒下载新版本
 */
public class DialogDownNewVersion extends BaseDialog {

    private TextView mContentText;
    private TextView tv_update;
    private ProgressBar progressBar;
    private View rootView;

    public DialogDownNewVersion(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_down_new_version, null);
        tv_update = rootView.findViewById(R.id.tv_update);
        mTitleTv = rootView.findViewById(R.id.common_dialog_title_text);
        mContentText = rootView.findViewById(R.id.tv_content_text);
        progressBar = rootView.findViewById(R.id.progress);
        createDialog(rootView);

    }

    @Override
    public void setProgress(int por) {
        super.setProgress(por);
        progressBar.setProgress(por);
    }

    @Override
    public void bindAllListeners() {

    }

    @Override
    public void setTitleText(int titleStrId) {
        mTitleTv.setText(titleStrId);
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
    public CoreDialog getCoreDialog() {
        return new CoreDialog(mContext, R.style.dialog, rootView, true);
    }
}
