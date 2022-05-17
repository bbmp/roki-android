package com.robam.roki.ui.page.login;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.common.base.Strings;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.IRokiRestService;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.form.UserActivity;
import com.robam.roki.ui.page.login.helper.LoginHelper;
import com.robam.roki.ui.page.login.manger.InputTextManager;
import com.robam.roki.ui.widget.view.ClearEditText;
import com.robam.roki.ui.widget.view.CountdownView;
import com.robam.roki.utils.StringUtil;
import com.robam.roki.utils.ToolUtils;



/**
 * desc : 微信登录后绑定手机号
 *
 * @author hxw
 */
public class LoginBindPhonePage extends MyBasePage<UserActivity> {

    /**
     * 手机号
     */
    private ClearEditText et_login_phone;
    /**
     * 验证码
     */
    private ClearEditText et_phone_code;
    /**
     * 登录
     */
    private AppCompatButton btn_bind;
    /**
     * 获取验证码
     */
    private CountdownView cv_find_countdown;
    /**
     * 记录返回的code
     */
    private String code;
    private String openId;
    private String accessToken;
    /**
     * 隐私协议勾选框
     */
    private CheckBox cbAcceptRokiPrivacy;
    /**‘
     * 隐私协议
     */
    private AppCompatTextView userLoginRegister;


    @Override
    protected int getLayoutId() {
        return R.layout.page_login_bind_phone;
    }

    @Override
    protected void initView() {
        getTitleBar().setOnTitleBarListener(this);
        et_login_phone = (ClearEditText) findViewById(R.id.et_login_phone);
        et_phone_code = (ClearEditText) findViewById(R.id.et_phone_code);
        cv_find_countdown = (CountdownView) findViewById(R.id.cv_find_countdown);
        btn_bind = (AppCompatButton) findViewById(R.id.btn_bind);
        cbAcceptRokiPrivacy = (CheckBox) findViewById(R.id.cb_accept_roki_privacy);
        userLoginRegister = (AppCompatTextView) findViewById(R.id.user_login_register);
        initLoginRegister();
//        InputTextManager.with(getActivity())
//                .addView(et_login_phone)
//                .addView(et_phone_code)
//                .setMain(btn_bind)
//                .build();
        setOnClickListener(cv_find_countdown, btn_bind);


    }

    @Override
    protected void initData() {
        openId = getBundle().getString("openId");
        accessToken = getBundle().getString("accessToken");
    }

    @Override
    public void onClick(View view) {
        ToolUtils.hideSoftInput(activity);
        if (view == cv_find_countdown) {
            String phone = et_login_phone.getText().toString();
            if (Strings.isNullOrEmpty(phone)) {
                ToastUtils.show(R.string.weixin_login_phone_num_not_null);
                return;
            }
            if (!StringUtils.isMobile(phone)) {
                ToastUtils.show(R.string.weixin_login_not_phone_num);
                return;
            }
            //先判断手机号码是否被注册
//            isExisted(phone);
            getCode(phone);
        } else if (view == btn_bind) {
            if (StringUtil.isEmpty(et_login_phone.getText().toString())){
                ToastUtils.show(R.string.weixin_login_phone_num_not_null);
                return;
            }
            if (StringUtil.isEmpty(et_phone_code.getText().toString())){
                ToastUtils.show(R.string.common_code_input_hint );
                return;
            }
            String phone = et_login_phone.getText().toString();
            String inputCode = et_phone_code.getText().toString();
            if(!cbAcceptRokiPrivacy.isChecked()){
                ToastUtils.show(R.string.accept_user_privacy_policy);
                return;
            }
            if (!StringUtils.isMobile(phone)) {
                ToastUtils.show(R.string.weixin_login_not_phone_num);
                return;
            }
            if (!inputCode.equals(code)) {
                ToastUtils.show(R.string.login_code_mismatch);
                return;
            }
            LoginHelper.getTokenBindPhone("wx", phone, code, accessToken, openId);
        }
    }

    /**
     * 返回
     *
     * @param view 被点击的左项View
     */
    @Override
    public void onLeftClick(View view) {
        if (activity instanceof MainActivity) {
            UIService.getInstance().returnHome();
        } else {
            MainActivity.start(activity);
        }
    }


    /**
     * 获取登录验证码
     */
    private void getCode(String phone) {
        ProgressDialogHelper.setRunning(cx, true);
        CloudHelper.getVerifyCode(phone, Reponses.GetVerifyCodeReponse.class, new RetrofitCallback<Reponses.GetVerifyCodeReponse>() {
            @Override
            public void onSuccess(Reponses.GetVerifyCodeReponse getVerifyCodeReponse) {
                ProgressDialogHelper.setRunning(cx, false);
                if (null != getVerifyCodeReponse) {
                    code = getVerifyCodeReponse.verifyCode;
                    ToastUtils.show(R.string.weixin_login_send_msg);
                    cv_find_countdown.start();
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
     * 初始化认证提示
     */
    private void initLoginRegister() {
//        loginAuthentication.setText(Html.fromHtml(getResources().getString(R.string.login_authentication2)));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.login_authentication));
        spannableStringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = String.format("%s", IRokiRestService.UserNotice);
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Url, url);
                bd.putString(PageArgumentKey.WebTitle, "ROKI用户协议");
                UIService.getInstance().postPage(PageKey.WebClientNew, bd);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#EFCE17"));
                ds.setUnderlineText(false);
            }
        }, 6, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = String.format("%s", IRokiRestService.RegisterAgreement);
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Url, url);
                bd.putString(PageArgumentKey.WebTitle, "隐私协议");
                UIService.getInstance().postPage(PageKey.WebClientNew, bd);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#EFCE17"));
                ds.setUnderlineText(false);
            }
        }, 17, 23, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        userLoginRegister.setMovementMethod(LinkMovementMethod.getInstance());
        userLoginRegister.setText(spannableStringBuilder);
        avoidHintColor(userLoginRegister);
    }
    private void avoidHintColor(View view) {
        if (view instanceof TextView) {
            ((TextView) view).setHighlightColor(getResources().getColor(android.R.color.transparent));
        }
    }
}
