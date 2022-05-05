package com.robam.roki.ui.page.mine;

import android.view.View;
import androidx.appcompat.widget.AppCompatButton;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.page.login.manger.InputTextManager;
import com.robam.roki.ui.widget.view.PasswordEditText;
import com.robam.roki.utils.ToolUtils;


/**
 * desc : 修改密码
 *
 * @author hxw
 */
public class MineChangePasswordPage extends MyBasePage<MainActivity> {
    /**
     * 原密码
     */
    private PasswordEditText etPasswordOld;
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
     * 忘记密码
     */
    private AppCompatButton btnForgetPassword;
    /**
     * 用户信息
     */
    private User user;


    @Override
    protected int getLayoutId() {
        return R.layout.page_mine_change_password;
    }

    @Override
    protected void initView() {
        setTitle(R.string.acc_change_password);
        getTitleBar().setOnTitleBarListener(this);
        etPasswordOld = (PasswordEditText) findViewById(R.id.et_password_old);
        etPasswordNew1 = (PasswordEditText) findViewById(R.id.et_password_new1);
        etPasswordNew2 = (PasswordEditText) findViewById(R.id.et_password_new_2);
        btnComplete = (AppCompatButton) findViewById(R.id.btn_complete);
        btnForgetPassword = (AppCompatButton) findViewById(R.id.btn_forget_password);
        setOnClickListener(btnComplete, btnForgetPassword);
    }

    @Override
    protected void initData() {
        user = getBundle().getParcelable(PageArgumentKey.User);
            setTitle(R.string.acc_change_password);
            InputTextManager.with(getActivity())
                    .addView(etPasswordNew1)
                    .addView(etPasswordNew2)
                    .addView(etPasswordOld)
                    .setMain(btnComplete)
                    .build();
    }

    @Override
    public void onClick(View view) {
        ToolUtils.hideSoftInput(activity);
        if (view.equals(btnComplete)) {
            onConfirm();
        } else if (view.equals(btnForgetPassword)) {
            UIService.getInstance().postPage(PageKey.MineForgetPasswordPage ,getBundle());
        }
    }

    private void onConfirm() {
        String oldPwd = etPasswordOld.getText().toString();
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
        oldPwd = User.encryptPassword(oldPwd);
        final String pwd = User.encryptPassword(newPwd);
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.updatePassword(user.id, oldPwd, pwd, new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.show("修改密码成功");
                ProgressDialogHelper.setRunning(cx, false);

                PreferenceUtils.setBool("logout",false);
                Plat.accountService.logout(null);
                CmccLoginHelper.getInstance().toLogin();
                UIService.getInstance().returnHome();
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
     *
     * @param view 被点击的左项View
     */
    @Override
    public void onLeftClick(View view) {
        UIService.getInstance().popBack();
    }


}
