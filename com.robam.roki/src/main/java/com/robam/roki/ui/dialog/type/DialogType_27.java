package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.robam.roki.R;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;

public class DialogType_27 extends BaseDialog {

    private TextView mCancelTv;
    private Button mOkTv;
    protected Button mContxt;

    public DialogType_27(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {

        View rootView = LayoutInflater.from(mContext).inflate(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
                R.layout.common_dialog_recipe_type_27  : R.layout.common_dialog_recipe_type_27_h
                , null);
//        mCancelTv = rootView.findViewById(R.id.common_dialog_cancel_btn);
        mTitleTv = rootView.findViewById(R.id.common_dialog_title_text);
        tv_title = rootView.findViewById(R.id.tv_title);
        mOkTv = rootView.findViewById(R.id.common_dialog_ok_btn);
        mContxt = rootView.findViewById(R.id.common_dialog_content_text);
        if (mDialog == null) {
            mDialog = new CoreDialog(mContext, R.style.dialog, rootView, true);
            mDialog.setPosition(Gravity.CENTER, 0, 0);
        }

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
        if (mCancelTv != null){
            mCancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCancelClick(v);
                }
            });
        }
        if (mOkTv != null){
            mOkTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOkClick(v);
                }
            });
        }
    }


    @Override
    public void setContentText(int contentStrId) {
        super.setContentText(contentStrId);
        mContxt.setText(contentStrId);
    }

    @Override
    public void setContentText(CharSequence contentStr) {
        super.setContentText(contentStr);
        mContxt.setText(contentStr);
    }

}
