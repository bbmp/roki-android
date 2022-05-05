package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.legent.ui.UIService;
import com.robam.common.io.cloud.IRokiRestService;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;

public class DialogType_24 extends BaseDialog {

    private Button mCancelTv;
    private Button mOkTv;
    protected TextView mContxt;

    public DialogType_24(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_recipe_type_24, null);
        mCancelTv = rootView.findViewById(R.id.common_dialog_cancel_btn);
        mOkTv = rootView.findViewById(R.id.common_dialog_ok_btn);
        mContxt = rootView.findViewById(R.id.common_dialog_content_text);
        if (mDialog == null) {
            mDialog = new CoreDialog(mContext, R.style.welcome_dialog, rootView, true);
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
        mContxt.setText(contentStrId);
    }

    @Override
    public void setContentText(CharSequence contentStr) {
        super.setContentText(contentStr);
        mContxt.setText(contentStr);
    }

}
