package com.robam.roki.ui.page.mine;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.manger.InputTextManager;
import com.robam.roki.ui.widget.view.ClearEditText;
import com.robam.roki.ui.widget.view.CountdownView;
import com.robam.roki.ui.widget.view.PasswordEditText;
import com.robam.roki.utils.StringUtil;
import com.robam.roki.utils.ToolUtils;


/**
 * desc : 忘记密码（验证手机号）
 * @author hxw
 */
public class MineForgetPasswordPage extends MyBasePage<MainActivity> {

    /**
     * 手机号
     */
    private ClearEditText etLoginPhone;
    /**
     * 输入验证码
     */
    private ClearEditText etPhoneCode;
    /**
     * 获取验证码
     */
    private CountdownView cvFindCountdown;
    /**
     * 下一步/完成
     */
    private AppCompatButton btnComplete;
    /**
     * 用户信息
     */
    private User user;
    /**
     * 记录验证码
     */
    private String code ;
    @Override
    protected int getLayoutId() {
        return R.layout.page_mine_forget_passwod;
    }
    @Override
    protected void initView() {
        setTitle(R.string.acc_change_password);
        getTitleBar().setOnTitleBarListener(this);
        etLoginPhone = (ClearEditText) findViewById(R.id.et_login_phone);
        etPhoneCode = (ClearEditText) findViewById(R.id.et_phone_code);
        cvFindCountdown = (CountdownView) findViewById(R.id.cv_find_countdown);
        btnComplete = (AppCompatButton) findViewById(R.id.btn_complete);
//        InputTextManager.with(getActivity())
//                .addView(etLoginPhone)
//                .addView(etPhoneCode)
//                .setMain(btnComplete)
//                .build();
        setOnClickListener(btnComplete ,cvFindCountdown);
    }
    @Override
    protected void initData() {
        user = getBundle().getParcelable(PageArgumentKey.User);
        etLoginPhone.setText(user.phone);
        etLoginPhone.setEnabled(false);
    }
    @Override
    public void onClick(View view) {
        ToolUtils.hideSoftInput(activity);
        if (view.equals(cvFindCountdown)){
            String phone = etLoginPhone.getText().toString();
            if (Strings.isNullOrEmpty(phone)){
                ToastUtils.show(R.string.weixin_login_phone_num_not_null );
                return;
            }
            if (!StringUtils.isMobile(phone)){
                ToastUtils.show(R.string.weixin_login_not_phone_num );
                return;
            }
            getCode(phone);
        }else if (view.equals(btnComplete)){
            if (StringUtil.isEmpty(etPhoneCode.getText().toString())){
                ToastUtils.show(R.string.common_code_input_hint );
                return;
            }
            if (etPhoneCode.getText().toString().equals(code)){
                Bundle bundle = getBundle();
                bundle.putString("verifyCode" ,code);
                UIService.getInstance().postPage(PageKey.MineSettingPasswordPage, getBundle());
            }else {
                ToastUtils.show(R.string.common_code_error_hint);
            }
        }
    }

    /**
     * 获取验证码
     * @param phone
     */
    private void getCode(final String phone) {
        if (!Objects.equal(user.phone, phone)) {
            ToastUtils.show("请正确输入原手机号码");
            return;
        }
        ProgressDialogHelper.setRunning(cx, true);
        CloudHelper.getVerifyCode(phone, Reponses.GetVerifyCodeReponse.class, new RetrofitCallback<Reponses.GetVerifyCodeReponse>() {
            @Override
            public void onSuccess(Reponses.GetVerifyCodeReponse getVerifyCodeReponse) {
                ProgressDialogHelper.setRunning(cx, false);
                if (null != getVerifyCodeReponse) {
                    cvFindCountdown.start();
                    code = getVerifyCodeReponse.verifyCode;
                    ToastUtils.show(R.string.common_code_send_hint);
                }
            }

            @Override
            public void onFaild(String err) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show(err);
            }
        });
    }

    /**
     * 返回
     *
     * @param view 被点击的左项View
     */
    @Override
    public void onLeftClick(View view) {
        UIService.getInstance().popBack();
    }

}
