package com.robam.roki.ui.page.login;


import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import com.google.common.base.Strings;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.plat.services.AccountService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.StringUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.ui.form.UserActivity;
import com.robam.roki.ui.page.login.helper.LoginHelper;
import com.robam.roki.ui.widget.view.ClearEditText;
import com.robam.roki.ui.widget.view.CountdownView;
import com.robam.roki.utils.StringUtil;
import com.robam.roki.utils.ToolUtils;


/**
 * desc ：验证码登录
 * @author hxw
 */
public class LoginCodePage extends MyBasePage<UserActivity> {

    /**
     * 手机号
     */
    private ClearEditText et_login_phone;
    /**
     * 验证码
     */
    private ClearEditText et_phone_code;
    /**
     * 登录
     */
    private AppCompatButton btn_login_commit;
    /**
     * 获取验证码
     */
    private CountdownView cv_find_countdown;
    /**
     * 记录返回的code
     */
    private String code ;

    public static LoginCodePage newInstance() {
        Bundle args = new Bundle();
        LoginCodePage fragment = new LoginCodePage();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.page_login_code;
    }

    @Override
    protected void initView() {

        et_login_phone = (ClearEditText) findViewById(R.id.et_login_phone);
        et_phone_code = (ClearEditText) findViewById(R.id.et_phone_code);
        cv_find_countdown = (CountdownView) findViewById(R.id.cv_find_countdown);
        btn_login_commit = (AppCompatButton) findViewById(R.id.btn_login_commit);
//        InputTextManager.with(getActivity())
//                .addView(et_login_phone)
//                .addView(et_phone_code)
//                .setMain(btn_login_commit)
//                .build();
        setOnClickListener(cv_find_countdown ,btn_login_commit);
    }

    @Override
    protected void initData() {
        String phone = PreferenceUtils.getString(AccountService.UserPhone, null);
        if (!StringUtil.isEmpty(phone)){
            et_login_phone.setText(phone);
        }
    }

    @Override
    public void onClick(View view) {
        ToolUtils.hideSoftInput(activity);
        if (view == cv_find_countdown){
            getCode();
        }else if (view == btn_login_commit){
            login();
        }
    }

    /**
     * 验证码登录
     */
    private void login(){
        if (StringUtil.isEmpty(et_login_phone.getText().toString())){
           ToastUtils.show(R.string.weixin_login_phone_num_not_null);
            return;
        }
        if (StringUtil.isEmpty(et_phone_code.getText().toString())){
            ToastUtils.show(R.string.common_code_input_hint );
            return;
        }
        String phone = et_login_phone.getText().toString();
        String inputCode = et_phone_code.getText().toString();
        if (!LoginPageBase.instance.isLoginAuthentication()){
            ToastUtils.show(R.string.accept_user_privacy_policy );
            return;
        }
        if (!StringUtils.isMobile(phone)){
            ToastUtils.show(R.string.weixin_login_not_phone_num );
            return;
        }
        if (!inputCode.equals(code)){
            ToastUtils.show(R.string.login_code_mismatch );
            return;
        }
        LoginHelper.loginCode(activity , phone ,inputCode);
        ProgressDialogHelper.setRunning(cx, true);

    }

    /**
     * 获取登录验证码
     */
    private void getCode() {
        String phone = et_login_phone.getText().toString();
        if (Strings.isNullOrEmpty(phone)){
            ToastUtils.show(R.string.weixin_login_phone_num_not_null );
            return;
        }
        if (!StringUtils.isMobile(phone)){
            ToastUtils.show(R.string.weixin_login_not_phone_num );
            return;
        }
        ProgressDialogHelper.setRunning(cx, true);
        CloudHelper.getVerifyCode(phone, Reponses.GetVerifyCodeReponse.class, new RetrofitCallback<Reponses.GetVerifyCodeReponse>() {
            @Override
            public void onSuccess(Reponses.GetVerifyCodeReponse getVerifyCodeReponse) {
                ProgressDialogHelper.setRunning(cx, false);
                if (null != getVerifyCodeReponse) {
                    code = getVerifyCodeReponse.verifyCode ;
                    ToastUtils.show( cx.getString(R.string.weixin_login_send_msg));
                    cv_find_countdown.start();
                }
            }

            @Override
            public void onFaild(String err) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show(err);
            }
        });

    }


}
