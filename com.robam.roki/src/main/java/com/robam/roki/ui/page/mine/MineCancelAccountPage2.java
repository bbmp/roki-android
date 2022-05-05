package com.robam.roki.ui.page.mine;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.manger.InputTextManager;
import com.robam.roki.ui.widget.view.ClearEditText;
import com.robam.roki.ui.widget.view.CountdownView;
import com.robam.roki.utils.StringUtil;


/**
 * desc : 注销账户界面
 *
 * @author hxw
 */
public class MineCancelAccountPage2 extends MyBasePage<MainActivity> {
    private static final String TAG = "MineCancelAccountPage2";
    /**
     * 手机号
     */
    private ClearEditText etLoginPhone;
    /**
     * 验证码
     */
    private ClearEditText etPhoneCode;
    /**
     * 获取验证码
     */
    private CountdownView cvFindCountdown;
    /**
     * 提交
     */
    private AppCompatButton btnComplete;
    /**
     * 用户信息
     */
    private User user;
    /**
     * 记录验证码
     */
    private String code ;
    /**
     * 手机号码
     */
    private String phone;

    @Override
    protected int getLayoutId() {
        return R.layout.page_mine_cancel_account_2;
    }

    @Override
    protected void initView() {
        setTitle(R.string.my_cancel_account);
        getTitleBar().setOnTitleBarListener(this);
        etLoginPhone = (ClearEditText) findViewById(R.id.et_login_phone);
        etPhoneCode = (ClearEditText) findViewById(R.id.et_phone_code);
        cvFindCountdown = (CountdownView) findViewById(R.id.cv_find_countdown);
        btnComplete = (AppCompatButton) findViewById(R.id.btn_complete);
//        InputTextManager.with(getActivity())
//                .addView(etLoginPhone)
//                .addView(etPhoneCode)
//                .setMain(btnComplete)
//                .build();
        setOnClickListener(btnComplete ,cvFindCountdown);
    }

    @Override
    protected void initData() {
        user = getBundle().getParcelable(PageArgumentKey.User);
        etLoginPhone.setText(user.phone);
        etLoginPhone.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(cvFindCountdown)){
            phone = etLoginPhone.getText().toString();
            if (Strings.isNullOrEmpty(phone)){
                ToastUtils.show(R.string.weixin_login_phone_num_not_null );
                return;
            }
            if (!StringUtils.isMobile(phone)){
                ToastUtils.show(R.string.weixin_login_not_phone_num );
                return;
            }
            getCode(phone);
        }else if (view.equals(btnComplete)){
            if (StringUtil.isEmpty(etPhoneCode.getText().toString())){
                ToastUtils.show(R.string.common_code_input_hint);
                return;
            }
            if (etPhoneCode.getText().toString().equals(code)){
                unRegistAccount();
            }else {
                ToastUtils.show(R.string.common_code_error_hint);
            }
        }
    }

    /**
     * 注销用户
     */
    private void unRegistAccount(){
        LogUtils.i("", "user.id:" + user.id + " user.phone:" + user.phone + " verifyCode:" + code + " currentUserId:" + Plat.accountService.getCurrentUserId());
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.unRegistAccount(user.getID(), user.phone, code, new Callback<Reponses.UnregisterResponse>() {

            @Override
            public void onSuccess(Reponses.UnregisterResponse unregisterResponse) {
                ProgressDialogHelper.setRunning(cx, false);
                LogUtils.i(TAG, "onSuccess:" + unregisterResponse.toString());
                ToastUtils.show(unregisterResponse.msg);
                if (unregisterResponse.rc == 0) {
                    PreferenceUtils.setBool("logout", false);
                    PreferenceUtils.clear();
                    Plat.accountService.logout(null);

                    UIService.getInstance().popBack();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                LogUtils.i(TAG, "onFailure:" + t.toString());
                ToastUtils.show(t.getMessage());
            }
        });
    }
    /**
     * 获取验证码
     * @param phone
     */
    private void getCode(final String phone) {
        if (!Objects.equal(user.phone, phone)) {
            ToastUtils.show("请正确输入原手机号码");
            return;
        }
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.getVerifyCode(phone, new Callback<String>() {
            @Override
            public void onSuccess(String s) {
                cvFindCountdown.start();
                code = s;
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show(R.string.common_code_send_hint);
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
