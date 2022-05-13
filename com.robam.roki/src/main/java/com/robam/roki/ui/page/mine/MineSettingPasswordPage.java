package com.robam.roki.ui.page.mine;

import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.page.login.manger.InputTextManager;
import com.robam.roki.ui.widget.view.PasswordEditText;
import com.robam.roki.utils.StringUtil;
import com.robam.roki.utils.ToolUtils;


/**
 * desc : 忘记密码(设置密码)
 *
 * @author hxw
 */
public class MineSettingPasswordPage extends MyBasePage<MainActivity> {

    /**
     * 新密码
     */
    private PasswordEditText etPasswordNew1;
    /**
     * 新密码
     */
    private PasswordEditText etPasswordNew2;
    /**
     * 完成
     */
    private AppCompatButton btnComplete;
    /**
     * 用户信息
     */
    private User user;
    /**
     * 是否有设置密码 true ：有
     */
    boolean hasPwd;
    private String verifyCode;

    @Override
    protected int getLayoutId() {
        return R.layout.page_mine_setting_password;
    }

    @Override
    protected void initView() {
        setTitle(R.string.acc_change_password);
        getTitleBar().setOnTitleBarListener(this);
        etPasswordNew1 = (PasswordEditText) findViewById(R.id.et_password_new1);
        etPasswordNew2 = (PasswordEditText) findViewById(R.id.et_password_new_2);
        btnComplete = (AppCompatButton) findViewById(R.id.btn_complete);
        setOnClickListener(btnComplete);
//        InputTextManager.with(getActivity())
//                .addView(etPasswordNew1)
//                .addView(etPasswordNew2)
//                .setMain(btnComplete)
//                .build();
    }

    @Override
    protected void initData() {
        user = getBundle().getParcelable(PageArgumentKey.User);
        verifyCode = getBundle().getString("verifyCode");
        hasPwd = user.hasPassword();
    }

    @Override
    public void onClick(View view) {
        ToolUtils.hideSoftInput(activity);
        if (view.equals(btnComplete)) {
            onConfirm();
        }
    }

    private void onConfirm() {
        if (StringUtil.isEmpty(etPasswordNew1.getText().toString()) || StringUtil.isEmpty(etPasswordNew2.getText().toString())){
            ToastUtils.show("新密码不可为空");
            return;
        }
        String newPwd = etPasswordNew1.getText().toString();
        String newPwd2 = etPasswordNew2.getText().toString();
        if (!newPwd.equals(newPwd2)){
            ToastUtils.show("两次密码不一致");
            return;
        }
        if (newPwd.length() < 6){
            ToastUtils.show("请输入大于6位的密码");
            return;
        }
        setPasswordByPhone(user.phone ,newPwd ,verifyCode);

    }
    protected void setPasswordByPhone(final String phone, String pwd, String verifyCode) {

        final String pwdMd5 = User.encryptPassword(pwd);
        ProgressDialogHelper.setRunning(cx, true);

        CloudHelper.resetPasswordByPhone(phone, pwdMd5, verifyCode, new VoidCallback() {
            @Override
            public void onSuccess() {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show("密码修改成功");

                PreferenceUtils.setBool("logout",false);
                Plat.accountService.logout(null);
                CmccLoginHelper.getInstance().toLogin();
                UIService.getInstance().returnHome();
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showThrowable(t);
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
