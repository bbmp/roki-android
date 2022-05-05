package com.robam.roki.ui.page;


import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.ui.form.MainActivity;

/**
 * Created by sylar on 15/6/9.
 * 快速登录 快速登录页
 */
public class UserExpressLoginPage extends AbsVerifyCodePage {

    @Override
    void onConfirm(String phone, String verifyCode) {
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.expressLogin(phone, verifyCode, new Callback<User>() {
            @Override
            public void onSuccess(User user) {
                ProgressDialogHelper.setRunning(cx, false);
                onLoginCompleted(user);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showThrowable(t);
            }
        });
    }

    void onLoginCompleted(User user) {
        ToastUtils.showShort("用户登录成功");
        if (activity instanceof MainActivity)
            UIService.getInstance().returnHome();
        else
            MainActivity.start(activity);
    }

    @Override
    void getVerifyCode(String phone, Callback<String> callback) {
        Plat.accountService.getDynamicPwd(phone, callback);
    }

    @Override
    String getCodeDesc() {
        return "动态密码";
    }


}
