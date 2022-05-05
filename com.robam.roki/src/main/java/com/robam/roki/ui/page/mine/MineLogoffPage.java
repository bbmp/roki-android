package com.robam.roki.ui.page.mine;

import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.legent.ui.UIService;
import com.robam.roki.R;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.widget.view.PasswordEditText;


/**
 *
 * @author hxw
 */
public class MineLogoffPage extends MyBasePage<MainActivity> {

    private PasswordEditText etPasswordOld;
    private PasswordEditText etPasswordNew1;
    private PasswordEditText etPasswordNew2;
    private AppCompatButton btnComplete;
    private AppCompatButton btnForgetPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.page_mine_change_password;
    }

    @Override
    protected void initView() {
        setTitle(R.string.acc_change_password);
        etPasswordOld = (PasswordEditText) findViewById(R.id.et_password_old);
        etPasswordNew1 = (PasswordEditText) findViewById(R.id.et_password_new1);
        etPasswordNew2 = (PasswordEditText) findViewById(R.id.et_password_new_2);
        btnComplete = (AppCompatButton) findViewById(R.id.btn_complete);
        btnForgetPassword = (AppCompatButton) findViewById(R.id.btn_forget_password);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {

    }


    /**
     * 返回
     *
     * @param view 被点击的左项View
     */
    @Override
    public void onLeftClick(View view) {
        if (activity instanceof MainActivity) {
            UIService.getInstance().popBack();
        } else {
            MainActivity.start(activity);
        }
    }


}
