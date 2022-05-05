package com.robam.roki.ui.page.login;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.AppCompatButton;
import com.legent.plat.pojos.User;
import com.legent.plat.services.AccountService;
import com.legent.utils.StringUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.ui.form.UserActivity;
import com.robam.roki.ui.page.login.helper.LoginHelper;
import com.robam.roki.ui.page.login.manger.InputTextManager;
import com.robam.roki.ui.widget.view.ClearEditText;
import com.robam.roki.ui.widget.view.PasswordEditText;
import com.robam.roki.utils.StringUtil;
import com.robam.roki.utils.ToolUtils;


/**
 * desc : 密码登录
 * @author hxw
 */
public class LoginPasswordPage extends MyBasePage<UserActivity> {

    /**
     * 手机号
     */
    private ClearEditText et_login_phone;
    /**
     * 密码
     */
    private PasswordEditText et_password;
    /**
     * 提交登录
     */
    private AppCompatButton btn_login_commit;

    public static LoginPasswordPage newInstance() {
        Bundle args = new Bundle();
        LoginPasswordPage fragment = new LoginPasswordPage();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.page_login_password;
    }

    @Override
    protected void initView() {
        et_login_phone = (ClearEditText) findViewById(R.id.et_login_phone);
        et_password = (PasswordEditText) findViewById(R.id.et_password);
        btn_login_commit = (AppCompatButton) findViewById(R.id.btn_login_commit);
//        InputTextManager.with(getActivity())
//                .addView(et_login_phone)
//                .addView(et_password)
//                .setMain(btn_login_commit)
//                .build();
        setOnClickListener(btn_login_commit);
    }

    @Override
    public void onClick(View view) {
        ToolUtils.hideSoftInput(activity);
        if (view == btn_login_commit){
            login() ;
        }
    }

    /**
     * 密码登录
     */
    private void login() {
        if (StringUtil.isEmpty(et_login_phone.getText().toString())){
            ToastUtils.show(R.string.weixin_login_phone_num_not_null);
            return;
        }
        if (StringUtil.isEmpty(et_password.getText().toString())){
            ToastUtils.show(R.string.common_password_input_error );
            return;
        }
        String account = et_login_phone.getText().toString();
        String pwd = et_password.getText().toString();
        if (!LoginPageBase.instance.isLoginAuthentication()){
            ToastUtils.show(R.string.accept_user_privacy_policy );
            return;
        }
        if (!StringUtils.isMobile(account)){
            ToastUtils.show(R.string.weixin_login_not_phone_num);
            return;
        }

        final String pwdMd5 = User.encryptPassword(pwd);
        LoginHelper.loginPassword(activity  ,account ,pwdMd5);
    }

    @Override
    protected void initData() {
        String phone = PreferenceUtils.getString(AccountService.UserPhone, null);
        if (!StringUtil.isEmpty(phone)){
            et_login_phone.setText(phone);
        }
    }


}
