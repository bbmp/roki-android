package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.View;

import com.google.common.base.Objects;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.ui.PageArgumentKey;

/**
 * Created by sylar on 15/6/17.
 */
public class UserModifyPhonePage extends  AbsVerifyCodePage{

    User user;

    @Override
    protected void init(Bundle bd) {
        user = bd.getParcelable(PageArgumentKey.User);
//        divUserProfile.setVisibility(View.GONE);
    }

    @Override
    void onConfirm(final String phone, String verifyCode) {

        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.updateUser(user.id, user.name, phone, user.email, user.gender, new VoidCallback() {
            @Override
            public void onSuccess() {
                ProgressDialogHelper.setRunning(cx, false);
                UIService.getInstance().popBack();
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showThrowable(t);
            }
        });
    }


    @Override
    void getCode(final String phone) {

        if (Objects.equal(user.phone, phone)) {
            ToastUtils.showShort("手机号未改变，无需验证");
            return;
        }
        LogUtils.i("20170512","phone:"+phone);
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.isExisted(phone,
                new Callback<Boolean>() {

                    @Override
                    public void onSuccess(Boolean result) {
                        ProgressDialogHelper.setRunning(cx, false);

                        if (result) {
                            ToastUtils.showShort("手机号已经注册，请使用其它号码");
                        } else {
                            UserModifyPhonePage.super.getCode(phone);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.showThrowable(t);
                    }
                });
    }
}
