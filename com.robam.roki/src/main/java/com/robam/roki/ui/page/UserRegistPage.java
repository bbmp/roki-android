package com.robam.roki.ui.page;


import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.ui.form.MainActivity;

/**
 * Created by sylar on 15/6/9.
 * 注册 注册页面
 */

public class UserRegistPage extends UserVerifyCodePage {
    @Override
    protected void onFinalConfirm(final String phone, final String pwd, String verifyCode) {

        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.registByPhone(phone, null, User.encryptPassword(pwd), null, true, verifyCode, new VoidCallback() {
            @Override
            public void onSuccess() {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showShort("用户注册成功，正在登录...");
                login(phone, pwd);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showThrowable(t);
            }
        });
    }

    void login(String phone, String pwd) {
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.login(phone, User.encryptPassword(pwd), new Callback<User>() {
            @Override
            public void onSuccess(User user) {
                ProgressDialogHelper.setRunning(cx, false);

                ToastUtils.showShort("用户登录成功");
                if (activity instanceof MainActivity)
                    UIService.getInstance().returnHome();
                else
                    MainActivity.start(activity);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showThrowable(t);
            }
        });
    }


}
