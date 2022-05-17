package com.robam.roki.ui.page;

import android.os.Bundle;
import android.widget.Toast;

import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.ui.form.MainActivity;


/**
 * Created by yinwei on 2017/10/27.
 */

public class UserLoginThirdPage extends AbsVerifyCodePage {
    private String openId;
    @Override
    protected void init(Bundle bd) {
        super.init(bd);
        openId = bd.getString("openId");
        LogUtils.i("20171027"," openId:"+openId);
    }

    @Override
    void onConfirm(String phone, String verifyCode) {
        ProgressDialogHelper.setRunning(cx, true);
        LogUtils.i("20171030","openId:"+openId+" phone:"+phone+" verifyCode:"+verifyCode);
        phoneBind(openId,phone,verifyCode);
    }

    private void phoneBind(String openId,String phone,String verifyCode){
        Plat.accountService.login3rd("wx", openId, phone, verifyCode, new Callback<User>() {
            @Override
            public void onSuccess(User user) {

                ProgressDialogHelper.setRunning(cx, false);
                onThirdLoginCompleted(user);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showThrowable(t);
            }
        });
    }

    void onThirdLoginCompleted(User user) {

        if (activity instanceof MainActivity)
            UIService.getInstance().returnHome();
        else
            MainActivity.start(activity);
    }

//    @Override
//    void getVerifyCode(String phone, Callback<String> callback) {
//        Plat.accountService.getDynamicPwd(phone, callback);
//    }

    @Override
    String getCodeDesc() {
        return cx.getString(R.string.weixin_login_dy_pwd);
    }
}
