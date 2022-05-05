package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.robam.roki.R;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;

/**
 * 类型01 Dialog
 *左上角 标题,报警码，中部文本内容，底部两个按钮，居中弹出
 */
public class DialogType_01 extends BaseDialog {

    private TextView mContentTv;
    private TextView mCancelTv;
    private TextView mOkTv;
    private View mMiddleDivider;
    private View rootView;

    public DialogType_01(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_type_01, null);
        mTitleTv = rootView.findViewById(R.id.common_dialog_title_text);
        mTitleAralmCodeTv = rootView.findViewById(R.id.common_dialog_title_aralm_text);
        mContentTv = rootView.findViewById(R.id.common_dialog_content_text);
        mCancelTv = rootView.findViewById(R.id.common_dialog_cancel_btn);
        mOkTv = rootView.findViewById(R.id.common_dialog_ok_btn);
        mMiddleDivider = rootView.findViewById(R.id.common_dialog_btn_middle_divider);
        createDialog(rootView);
    }



    @Override
    public void setContentText(int contentStrId) {
        mContentTv.setText(contentStrId);
    }

    @Override
    public void setContentText(CharSequence contentStr) {
        mContentTv.setText(contentStr);
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
    public CoreDialog getCoreDialog() {
        return new CoreDialog(mContext,R.style.dialog,rootView,true);
    }
}
