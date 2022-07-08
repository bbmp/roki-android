package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.View;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.User;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.ui.Helper;

/**
 * Created by sylar on 15/6/9.
 */
public class UserRecoverPwdPage extends UserVerifyCodePage {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        userProfileView.setVisibility(View.GONE);
//        onCheckChanged(true);
    }

    @Override
    protected void onFinalConfirm(final String phone, String pwd, String verifyCode) {

        final String pwdMd5 = User.encryptPassword(pwd);
        ProgressDialogHelper.setRunning(cx, true);

        Plat.accountService.resetPasswordByPhone(phone, pwdMd5, verifyCode, new VoidCallback() {
            @Override
            public void onSuccess() {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showShort("密码修改成功");

                Helper.login(phone, pwdMd5);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showThrowable(t);
            }
        });
    }
}
