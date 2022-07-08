package com.robam.roki.ui.page;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.IRokiRestService;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.utils.ToolUtils;
import com.robam.roki.utils.YouzanUserAttestationUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/8.
 * 账号登录页面
 */
public class UserLoginPage extends BasePage {

    @InjectView(R.id.edtAccount)
    EditText edtAccount;
    @InjectView(R.id.edtPwd)
    EditText edtPwd;

    @InjectView(R.id.log_back)
    ImageView back;
    @InjectView(R.id.btn)
    ImageView btn;
    @InjectView(R.id.txtLogin)
    TextView txtLogin;

    @InjectView(R.id.user_login_register)
    TextView loginRegister;

    @InjectView(R.id.cb_accept_roki_privacy)
    CheckBox acceptRokiPrivay;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View view = inflater.inflate(R.layout.page_user_login, container, false);
        ButterKnife.inject(this, view);
        String account = Plat.accountService.getLastAccount();
        edtAccount.setText(account);
        initLoginRegister();
        if (Strings.isNullOrEmpty(account)) {
            edtAccount.requestFocus();
        } else {
            edtPwd.requestFocus();
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        MobApp.getmFirebaseAnalytics().setCurrentScreen(getActivity(), "账号登录页", null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.log_back)
    public void onClickBack() {
        boolean isHome = UIService.getInstance().isMainForm();
        if (isHome) {
            UIService.getInstance().popBack();
        } else {
            MainActivity.start((Activity) getContext());
        }
    }

    //点击登录
    @OnClick(R.id.txtLogin)
    public void onClickLogin() {
        try {
            hideSoftKeyboard(getContext(), txtLogin);
            login();
        } catch (Exception e) {
            ToastUtils.showException(e);
        }
    }

    @OnClick(R.id.txtExpressLogin)
    public void onClickExpressLogin() {
        UIService.getInstance().postPage(PageKey.UserExpressLogin);
    }

    //点击注册
    @OnClick(R.id.txtRegist)
    public void onClickRegist() {
        ToolUtils.logEvent("登录", "点击:注册", "roki_个人");
        UIService.getInstance().postPage(PageKey.UserRegist);
    }

    @OnClick(R.id.txtRetrievePwd)
    public void onClickRetrievePwd() {
        UIService.getInstance().postPage(PageKey.UserRecoverPwd);
    }

    private void login() {
        String account = edtAccount.getText().toString();
        String pwd = edtPwd.getText().toString();

        Preconditions.checkState(StringUtils.isMobile(account), cx.getString
                (R.string.login_unsupported_phone_format));
        Preconditions.checkState(!Strings.isNullOrEmpty(pwd), cx.getString(R.string.login_pwd_is_null));

        ToolUtils.logEvent("登录", "点击:登录", "roki_个人");

        final String pwdMd5 = User.encryptPassword(pwd);
        if (!acceptRokiPrivay.isChecked()) {
            ToastUtils.show(getString(R.string.accept_user_privacy_policy), Toast.LENGTH_SHORT);
            return;
        }
        Helper.login(activity, account, pwdMd5);
    }

    //微信登录授权
  /*  private static IWXAPI WXapi;
    private String WX_APP_ID = "wx77973859a88a8921";*/

    @OnClick(R.id.btn)
    public void onClickWX() {
        if (!acceptRokiPrivay.isChecked()) {
            ToastUtils.show(getString(R.string.accept_user_privacy_policy), Toast.LENGTH_SHORT);
            return;
        }
        LogUtils.i("20171023", "here is run");
        if (token != null) {
            LogUtils.i("20171023", "code=null");
            code = null;
            login3rd(code, token);
        } else {
            LogUtils.i("20171023", "code!=null");
            sendWxMessage();
        }
    }

    String code;
    static String token = null;


    private void initLoginRegister() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.roki_priviacy_agreement_title));
        spannableStringBuilder.setSpan(new ForegroundColorSpan(R.color.c67), 6, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                ds.setUnderlineText(false);
            }
        }, 6, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringBuilder.setSpan(new ForegroundColorSpan(R.color.c67), 17, 23, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
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
                ds.setUnderlineText(false);
            }
        }, 17, 23, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        loginRegister.setMovementMethod(LinkMovementMethod.getInstance());
        loginRegister.setText(spannableStringBuilder);
        avoidHintColor(loginRegister);
    }

    private void login3rd(String code, final String accessToken) {

        CloudHelper.otherLogin("wx", "RKDRD", code, accessToken, new Callback<Reponses.OtherLoginResponse>() {
            @Override
            public void onSuccess(Reponses.OtherLoginResponse user3In) {
                //  ToastUtils.show("请求成功",Toast.LENGTH_SHORT);
                //   LogUtils.i("20171023","user："+user3In.user.thirdInfos.accessToken);
                token = user3In.user.thirdInfos.getAccessToken();
                PreferenceUtils.setString("token", token);
                PreferenceUtils.setBool("logout", true);
                if (!user3In.user.binded) {
                    Bundle bundle = new Bundle();
                    bundle.putString("openId", user3In.user.thirdInfos.openId);
                    UIService.getInstance().postPage(PageKey.UserLoginThird, bundle);
                } else {
                    LogUtils.i("20180523", "user::" + user3In.user.password);
                    Plat.accountService.onLogin(user3In.user);
                    YouzanUserAttestationUtils.initYouzanData();
                    if (activity instanceof MainActivity) {
                        UIService.getInstance().popBack();
                    } else {
                        MainActivity.start(activity);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20171023", "t:" + t.getMessage() + " ");
                if ("7001".equals(t.getMessage())) {
                    token = null;
                    sendWxMessage();
                    return;
                }
                token = null;
//                ToastUtils.show("请求失败",Toast.LENGTH_SHORT);
            }
        });
    }

    private void avoidHintColor(View view) {
        if (view instanceof TextView)
            ((TextView) view).setHighlightColor(getResources().getColor(android.R.color.transparent));
    }

    private void sendWxMessage() {
        if (!MobApp.mWxApi.isWXAppInstalled()) {
            ToastUtils.show(R.string.weixin_login_not_installed_client, Toast.LENGTH_SHORT);
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        MobApp.mWxApi.sendReq(req);
    }

    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     */
    public void hideSoftKeyboard(Context context, TextView textView) {
        if (textView == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
