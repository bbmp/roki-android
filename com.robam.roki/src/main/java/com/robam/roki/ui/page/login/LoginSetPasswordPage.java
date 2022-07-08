package com.robam.roki.ui.page.login;


import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.hjq.toast.ToastUtils;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.RCReponse;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.api.PreferenceUtils;
import com.robam.roki.R;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.form.UserActivity;
import com.robam.roki.ui.page.login.manger.InputTextManager;
import com.robam.roki.ui.widget.view.PasswordEditText;
import com.robam.roki.utils.StringUtil;


/**
 * desc : 微信登录后绑定手机号设置密码
 * @author hxw
 */
public class LoginSetPasswordPage extends MyBasePage<UserActivity> {

    /**
     * 密码1
     */
    private PasswordEditText et_password;
    /**
     * 密码2
     */
    private PasswordEditText et_password_2;
    /**
     * 完成
     */
    private AppCompatButton btn_complete;
    /**
     * 记录返回的code
     */
    private String code ;
    private User user;


    @Override
    protected int getLayoutId() {
        return R.layout.page_login_set_password;
    }

    @Override
    protected void initView() {
        getTitleBar().setOnTitleBarListener(this);
        et_password = (PasswordEditText) findViewById(R.id.et_password);
        et_password_2 = (PasswordEditText) findViewById(R.id.et_password_2);
        btn_complete = (AppCompatButton) findViewById(R.id.btn_complete);
//        InputTextManager.with(getActivity())
//                .addView(et_password)
//                .addView(et_password_2)
//                .setMain(btn_complete)
//                .build();
        setOnClickListener(btn_complete);

    }

    @Override
    protected void initData() {
         user = (User) getArguments().getParcelable("user");
    }

    @Override
    public void onClick(View view) {
        if (view == btn_complete){
            if (StringUtil.isEmpty(et_password.getText().toString()) ){
                ToastUtils.show(R.string.change_password_2 );
                return;
            }
            if ( StringUtil.isEmpty(et_password_2.getText().toString())){
                ToastUtils.show(R.string.change_password_3 );
                return;
            }
            if (!et_password.getText().toString().endsWith(et_password_2.getText().toString())){
                ToastUtils.show(R.string.common_password_input_unlike );
                return;
            }
            setPassword();
        }
    }

    private void setPassword(){
        String password = et_password.getText().toString();
        String pwdMd5 = User.encryptPassword(password);
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.setPassword(user.authorization, pwdMd5, new Callback<RCReponse>() {
            @Override
            public void onSuccess(RCReponse rcReponse) {
                ToastUtils.show("设置密码成功");
                ProgressDialogHelper.setRunning(cx, false);
                Plat.accountService.getCurrentUser().setPassword(pwdMd5);
                PreferenceUtils.setString(user.getAccount(), pwdMd5);
                if (activity instanceof MainActivity) {
                    UIService.getInstance().returnHome();
                } else{
                    MainActivity.start(activity);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show(t.getMessage());
            }
        });
    }
    /**
     * 返回
     * @param view     被点击的左项View
     */
    @Override
    public void onLeftClick(View view) {
        if (activity instanceof MainActivity) {
            UIService.getInstance().returnHome();
        } else{
            MainActivity.start(activity);
        }
    }

}
