package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.robam.roki.R;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;
import com.robam.roki.utils.StringConstantsUtil;

/**
 * Created by  2017/8/17.
 * roki所有烟机统一弹框
 */
public class DialogType_08 extends BaseDialog {


    private LinearLayout mLlSmartSetting;       //智能设定
    private LinearLayout mLlProductInformation; //产品信息
    private LinearLayout mLlSalesService;       //售后服务
    private LinearLayout mLlFeedbackSuggestions;//反馈建议
    private Button mBtnCancel;

    public DialogType_08(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_type_08, null);
        mLlSmartSetting = rootView.findViewById(R.id.ll_smart_setting);
        mLlProductInformation = rootView.findViewById(R.id.ll_product_information);
        mLlSalesService = rootView.findViewById(R.id.ll_sales_service);
        mLlFeedbackSuggestions = rootView.findViewById(R.id.ll_feedback_suggestions);
        mBtnCancel = rootView.findViewById(R.id.btn_cancel);
        createDialog(rootView);
    }

    protected void createDialog(View rootView) {
        if (mDialog == null) {
            mDialog = new CoreDialog(mContext, R.style.Dialog_Microwave_professtion_bottom, rootView, true);
            mDialog.setPosition(Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void setCancelBtn(CharSequence text, View.OnClickListener cancelOnClickListener) {
        mBtnCancel.setText(text);
        setOnCancelClickListener(cancelOnClickListener);
    }

    @Override
    public void setCancelBtn(int textId, View.OnClickListener cancelOnClickListener) {
        mBtnCancel.setText(textId);
        setOnCancelClickListener(cancelOnClickListener);
    }

    @Override
    public void setOkBtn(CharSequence text, View.OnClickListener okOnClickListener) {
        setOnOkClickListener(okOnClickListener);
    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        setOnOkClickListener(okOnClickListener);
    }

    @Override
    public void bindAllListeners() {

        mLlSmartSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(StringConstantsUtil.STRING_SMART_SETTING);
                onOkClick(v);
            }
        });
        mLlProductInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(StringConstantsUtil.STRING_PRODUCT_INFORMATION);
                onOkClick(v);
            }
        });
        mLlSalesService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(StringConstantsUtil.STRING_SALES_SERVICE);
                onOkClick(v);
            }
        });
        mLlFeedbackSuggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(StringConstantsUtil.STRING_FEEDBACK_SUGGESTIONS);
               onOkClick(v);
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick(v);
            }
        });

    }

}
