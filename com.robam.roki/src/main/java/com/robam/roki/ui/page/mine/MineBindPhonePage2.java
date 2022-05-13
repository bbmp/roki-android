package com.robam.roki.ui.page.mine;

import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.StringUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.UserModifyPhonePage;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.page.login.manger.InputTextManager;
import com.robam.roki.ui.widget.view.ClearEditText;
import com.robam.roki.ui.widget.view.CountdownView;


/**
 * desc : 修改绑定手机号(验证新手机)
 *
 * @author hxw
 */
public class MineBindPhonePage2 extends MyBasePage<MainActivity> {
    /**
     * 手机号
     */
    private ClearEditText etLoginPhone;
    /**
     * 输入验证码
     */
    private ClearEditText etPhoneCode;
    /**
     * 获取验证码
     */
    private CountdownView cvFindCountdown;
    /**
     * 下一步/完成
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
        return R.layout.page_mine_bind_phone_2;
    }
    @Override
    protected void initView() {
        setTitle(R.string.bind_change_phone);
        getTitleBar().setOnTitleBarListener(this);
        etLoginPhone = (ClearEditText) findViewById(R.id.et_login_phone);
        etPhoneCode = (ClearEditText) findViewById(R.id.et_phone_code);
        cvFindCountdown = (CountdownView) findViewById(R.id.cv_find_countdown);
        btnComplete = (AppCompatButton) findViewById(R.id.btn_complete);
        InputTextManager.with(getActivity())
                .addView(etLoginPhone)
                .addView(etPhoneCode)
                .setMain(btnComplete)
                .build();
        setOnClickListener(btnComplete ,cvFindCountdown);
    }
    @Override
    protected void initData() {
        user = getBundle().getParcelable(PageArgumentKey.User);
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
            isExisted(phone);
        }else if (view.equals(btnComplete)){
            if (etPhoneCode.getText().toString().equals(code)){
                updateUser(phone ,code) ;
            }else {
                ToastUtils.show(R.string.common_code_error_hint);
            }
        }
    }

    /**
     * 换绑新手机
     * @param phone
     * @param verifyCode
     */
    private void updateUser(final String phone, String verifyCode) {
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.bindNewPhone(user.id, phone, verifyCode,  new VoidCallback() {
            @Override
            public void onSuccess() {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show("换绑手机成功");

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
     * 判断号码是否被注册
     */
    private void isExisted(final String phone){
        ProgressDialogHelper.setRunning(cx, true);
        CloudHelper.isExisted(phone,
                new Callback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        ProgressDialogHelper.setRunning(cx, false);
                        if (result) {
                            ToastUtils.show("手机号已经注册，请使用其它号码");
                        } else {
                           getCode(phone);
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
     * 获取验证码
     * @param phone
     */
    private void getCode(final String phone) {
        if (Objects.equal(user.phone, phone)) {
           ToastUtils.show("请输入新的手机号码");
            return;
        }

        ProgressDialogHelper.setRunning(cx, true);
        CloudHelper.getVerifyCode(phone, Reponses.GetVerifyCodeReponse.class, new RetrofitCallback<Reponses.GetVerifyCodeReponse>() {
            @Override
            public void onSuccess(Reponses.GetVerifyCodeReponse getVerifyCodeReponse) {
                ProgressDialogHelper.setRunning(cx, false);
                if (null != getVerifyCodeReponse) {
                    cvFindCountdown.start();
                    code = getVerifyCodeReponse.verifyCode;
                    ToastUtils.show(R.string.common_code_send_hint);
                }
            }

            @Override
            public void onFaild(String err) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show(err);
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
