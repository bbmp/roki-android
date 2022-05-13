//package com.robam.roki.ui.dialog;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.google.common.base.Preconditions;
//import com.google.common.base.Strings;
//import com.legent.Callback;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.dialogs.AbsDialog;
//import com.legent.utils.StringUtils;
//import com.legent.utils.api.ToastUtils;
//import com.legent.utils.api.ViewUtils;
//import com.robam.common.io.cloud.RokiRestHelper;
//import com.robam.common.pojos.CrmCustomer;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageArgumentKey;
//import com.robam.roki.ui.PageKey;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * Created by sylar on 15/6/20.
// */
//public class MaintainFailedDialog extends AbsDialog {
//
//
//    CrmCustomer crmCustomer;
//
//    @InjectView(R.id.edtPhone)
//    EditText edtPhone;
//    @InjectView(R.id.imgClear)
//    ImageView imgClear;
//    @InjectView(R.id.txtOK)
//    TextView txtOK;
//    @InjectView(R.id.layout)
//    LinearLayout layout;
//
//    static public void show(Context cx) {
//        MaintainFailedDialog dlg = new MaintainFailedDialog(cx);
//        dlg.show();
//    }
//
//    public MaintainFailedDialog(Context cx) {
//        super(cx, R.style.Theme_Dialog_FullScreen);
//        ViewUtils.setFullScreen(cx, this);
//    }
//
//    @Override
//    protected int getViewResId() {
//        return R.layout.dialog_maintain_failed;
//    }
//
//    @OnClick(R.id.layout)
//    public void onClick() {
//        dismiss();
//    }
//
//    @OnClick(R.id.imgClear)
//    public void onClickClear() {
//        edtPhone.setText(null);
//        setClearVisible(false);
//    }
//
//
//    @OnClick(R.id.txtCheck)
//    public void onClickCheck() {
//        try {
//            String phone = edtPhone.getText().toString();
//            Preconditions.checkNotNull(phone, "手机号不可为空！");
//            Preconditions.checkState(StringUtils.isMobile(phone), "无效的手机号");
//            onCheck(phone);
//        } catch (Exception e) {
//            ToastUtils.showException(e);
//        }
//    }
//
//
//    @OnClick(R.id.txtOK)
//    public void onClickOK() {
//        onOK();
//    }
//
//    @Override
//    protected void initView(View view) {
//        ButterKnife.inject(this, view);
//
//        setButtonValid(false);
//        setClearVisible(false);
//
//        edtPhone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                setClearVisible(!Strings.isNullOrEmpty(s.toString()));
//            }
//        });
//    }
//
//    void setClearVisible(boolean visible) {
//        imgClear.setVisibility(visible ? View.VISIBLE : View.GONE);
//    }
//
//    void onCheck(String phone) {
//        RokiRestHelper.getCrmCustomer(phone, new Callback<CrmCustomer>() {
//            @Override
//            public void onSuccess(CrmCustomer customer) {
//                crmCustomer = customer;
//                boolean isValid = customer != null && customer.products != null && customer.products.size() > 0;
//                setButtonValid(isValid);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                ToastUtils.showThrowable(t);
//            }
//        });
//    }
//
//
//    void onOK() {
//        if (crmCustomer == null) return;
//
//        dismiss();
//
//
//        Bundle bd = new Bundle();
//        bd.putParcelable(PageArgumentKey.CrmCustomer, crmCustomer);
//        UIService.getInstance().postPage(PageKey.MaintainRequest, bd);
//    }
//
//    void setButtonValid(boolean isValid) {
//        txtOK.setSelected(!isValid);
//        txtOK.setEnabled(isValid);
//    }
//}
