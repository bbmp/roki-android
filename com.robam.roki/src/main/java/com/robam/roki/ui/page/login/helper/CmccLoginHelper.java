package com.robam.roki.ui.page.login.helper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;

import com.cmic.sso.sdk.AuthThemeConfig;
import com.cmic.sso.sdk.auth.AuthnHelper;
import com.cmic.sso.sdk.auth.CheckBoxListener;
import com.cmic.sso.sdk.auth.LoginClickListener;
import com.cmic.sso.sdk.auth.LoginPageInListener;
import com.cmic.sso.sdk.auth.TokenListener;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.events.UserLoginNewEvent;
import com.legent.plat.io.RCRetrofitCallback;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.io.cloud.Requests;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.CmccBackEvent;
import com.robam.common.events.CmccEvent;
import com.robam.common.io.cloud.IRokiRestService;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.activity3.RWebActivity;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.form.UserActivity;
import com.robam.roki.ui.page.login.config.Constant;
import com.robam.roki.ui.widget.view.ScaleImageView;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.json.JSONException;
import org.json.JSONObject;

import static com.legent.plat.services.AccountService.IsOwnUser;
import static com.legent.plat.services.AccountService.LastOwnAccount;

/**
 * @ClassName: LoginHelper
 * @Description: ????????????????????? 1.?????????????????? 2.???????????? 3.????????????
 * @Author: Hxw
 * @CreateDate: 2021/3/17 10:04
 */
public class CmccLoginHelper {
    @SuppressLint("StaticFieldLeak")
    private static CmccLoginHelper instance = new CmccLoginHelper();
    private Context context;
    /**
     * ???????????????
     */
    private final static int REQUEST_PHONE_CODE = 111;
    /***
     * ???????????????
     */
    private final static int REQUEST_LOGIN_AUTH = 112;
    /**
     * SDK?????????
     */
    private final String RESULT_OK = "103000";
    /**
     * ??????????????????
     */
    private AuthnHelper mAuthnHelper;
    /**
     * ?????????????????????????????????????????????????????????
     */
    private AuthThemeConfig.Builder themeConfigBuilder;
    /**
     * ?????????
     */
    private String phoneNum;
    /**
     * ????????????
     */
    private String code;
    private String token = null;
    /**
     * context
     */
    FragmentActivity cx;
    /**
     * ????????????
     */
    Dialog alertDialog;

    public boolean isGetPhone = false;

    public static CmccLoginHelper getInstance() {
        return instance;
    }

    /**
     * ??????SDK??????
     */
    TokenListener tokenListener = new TokenListener() {
        @Override
        public void onGetTokenComplete(int i, JSONObject jsonObject) {
            switch (i) {
                case REQUEST_PHONE_CODE:
                    Log.i("LOGIN", "REQUEST_PHONE_CODE-------------------" + jsonObject.toString());
                    try {
                        String resultCode = jsonObject.getString("resultCode");
                        Log.i("LOGIN", "resultCode-------------------" + resultCode);
                        if (RESULT_OK.equals(resultCode)) {
//                            loginAuth();
                            isGetPhone = true;
                        } else {
                            isGetPhone = false;
//                            login();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case REQUEST_LOGIN_AUTH:
                    ProgressDialogHelper.setRunning(cx, false);
                    Log.i("LOGIN", "REQUEST_LOGIN_AUTH-------------------" + jsonObject.toString());
                    try {
                        String resultCode = jsonObject.getString("resultCode");
                        Log.i("LOGIN", "resultCode-------------------" + resultCode);
                        if (RESULT_OK.equals(resultCode)) {
                            String token = jsonObject.getString("token");
                            Log.i("LOGIN", "token-------------------" + token);
                            getPhoneNum(token);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * ????????????????????????
     *
     * @param token
     */
    private void getPhoneNum(String token) {
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.getPhoneNum(token, new Callback<String>() {
            @Override
            public void onSuccess(String phone) {
                ProgressDialogHelper.setRunning(cx, false);
                phoneNum = phone;
                token(phone);
                Log.i("LOGIN", phone);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                Log.i("LOGIN", t.getMessage());
                ToastUtils.showThrowable(t);
            }
        });
    }

    /**
     * ???????????????token
     *
     * @param phone
     */
    private void token(String phone) {
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.token("mobile", phone, new Callback<Reponses.TokenReponse>() {

            @Override
            public void onSuccess(Reponses.TokenReponse tokenReponse) {
                ProgressDialogHelper.setRunning(cx, false);
                String access_token = tokenReponse.token_type + " " + tokenReponse.access_token;
                Log.i("LOGIN", "access_token--------------------" + access_token);
                isFirstLogin(access_token);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                Log.i("LOGIN", t.getMessage());
                ToastUtils.showThrowable(t);
            }
        });
    }

    /**
     * ???????????????????????????
     *
     * @param access_token
     */
    private void isFirstLogin(final String access_token) {
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.isFirstLogin(access_token, new Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean isFirstLogin) {
                ProgressDialogHelper.setRunning(cx, false);
                Log.i("LOGIN", "access_token--------------------" + access_token);
                getOauth(access_token, isFirstLogin);
            }


            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                Log.i("LOGIN", t.getMessage());
                ToastUtils.showThrowable(t);
            }
        });
    }

    private void getUser2(long userId, boolean isFirstLogin) {
        CloudHelper.getUser2(userId, Reponses.GetUserReponse.class,
                new RetrofitCallback<Reponses.GetUserReponse>() {
                    @Override
                    public void onSuccess(Reponses.GetUserReponse getUserReponse) {
                        if (null != getUserReponse) {
                            User user = getUserReponse.user;
                            PreferenceUtils.setBool(IsOwnUser, true);
                            PreferenceUtils.setString(LastOwnAccount, user.getAccount());
                            onLoginCompleted(user, isFirstLogin);
                        }
                    }

                    @Override
                    public void onFaild(String err) {

                    }

                });
    }

    /**
     * ??????????????????
     *
     * @param access_token
     */
    private void getOauth(final String access_token, boolean isFirstLogin) {
        ProgressDialogHelper.setRunning(cx, true);
        CloudHelper.getOauth(access_token, Reponses.LoginReponse.class, new RetrofitCallback<Reponses.LoginReponse>() {
            @Override
            public void onSuccess(Reponses.LoginReponse loginReponse) {
                ProgressDialogHelper.setRunning(cx, false);
                if (null != loginReponse) {
                    User user = loginReponse.user;

                    Log.i("LOGIN", "user--------------------" + user.toString());
                    user.authorization = access_token;
                    getUser2(user.id, isFirstLogin);
                }
            }

            @Override
            public void onFaild(String err) {
                ProgressDialogHelper.setRunning(cx, false);
            }
        });
    }

    /**
     * ??????????????????
     *
     * @param user
     */
    private void onLoginCompleted(User user, boolean isFirstLogin) {
        ToastUtils.showShort("??????????????????");
        user.phone = phoneNum;
        Plat.accountService.onLogin(user);
        if (isFirstLogin) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            UIService.getInstance().postPage(PageKey.LoginSetPassword, bundle);
            mAuthnHelper.quitAuthActivity();
        } else {
            if (cx instanceof MainActivity) {
                EventUtils.postEvent(new UserLoginNewEvent());
                mAuthnHelper.quitAuthActivity();
            } else if (cx instanceof RWebActivity) {
                mAuthnHelper.quitAuthActivity();
            } else {
                mAuthnHelper.quitAuthActivity();
                MainActivity.start(cx);
            }
        }
    }

    /**
     * ????????????
     */
    public void getPhnoeInfo() {
        mAuthnHelper.getPhoneInfo(Constant.CMCC_APP_ID, Constant.CMCC_APP_KEY, tokenListener, REQUEST_PHONE_CODE);
    }

    /**
     * ????????????
     */
    public void loginAuth() {

        mAuthnHelper.loginAuth(Constant.CMCC_APP_ID, Constant.CMCC_APP_KEY, tokenListener, REQUEST_LOGIN_AUTH);
        ProgressDialogHelper.setRunning(cx, true);
    }

    /**
     * ????????????
     */
    public void quitAuthActivity() {
        mAuthnHelper.quitAuthActivity();
    }

    /**
     * ????????????/?????????????????????
     */
    public void login() {
        UIService.getInstance().postPage(PageKey.LoginPageBase);
    }

    public void toLogin() {
        if (isGetPhone) {
            loginAuth();
        } else {
            login();
        }
    }

    /**
     * ?????????????????????sdk
     */
    public void initSdk(FragmentActivity activity) {
        cx = activity;
        AuthnHelper.setDebugMode(true);
        mAuthnHelper = AuthnHelper.getInstance(cx);
        mAuthnHelper.setPageInListener(new LoginPageInListener() {
            @Override
            public void onLoginPageInComplete(String resultCode, JSONObject jsonObj) {
                if (resultCode.equals("200087")) {
                    Log.d("initSDK", "page in---------------");
                    EventUtils.postEvent(new CmccEvent(true));
                }
            }
        });

        //????????????????????????View
        RelativeLayout relativeLayout = new RelativeLayout(cx);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        View view = cx.getLayoutInflater().inflate(R.layout.page_login_cmcc, relativeLayout, false);
        //title
        TitleBar tb_title = (TitleBar) view.findViewById(R.id.tb_title);
        tb_title.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View view) {
                EventUtils.postEvent(new CmccBackEvent());
                mAuthnHelper.quitAuthActivity();
            }

            @Override
            public void onTitleClick(View view) {
            }

            @Override
            public void onRightClick(View view) {
                try {
                    mAuthnHelper.quitAuthActivity();
                    Bundle bundle = new Bundle();
                    bundle.putInt("right_title_id", R.string.login_code);
                    bundle.putBoolean("isCmccLogin", true);
                    UIService.getInstance().postPage(PageKey.UserLogin, bundle);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        //??????????????????
        AppCompatButton btn_login_other_phone = (AppCompatButton) view.findViewById(R.id.btn_login_other_phone);
        btn_login_other_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthnHelper.quitAuthActivity();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isCmccLogin", true);
                UIService.getInstance().postPage(PageKey.UserLogin, bundle);
            }
        });
        ScaleImageView iv_login_wechat = (ScaleImageView) view.findViewById(R.id.iv_login_wechat);
        LinearLayout ll_login_other = (LinearLayout) view.findViewById(R.id.ll_login_other);
        if (!MobApp.mWxApi.isWXAppInstalled()) {
            ll_login_other.setVisibility(View.GONE);
            iv_login_wechat.setVisibility(View.GONE);
        }
        iv_login_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("20171023", "code=null");
                if (token != null) {
                    Log.i("20171023", "code=null");
                    code = null;
                    login3rd(code, token);
                } else {
                    Log.i("20171023", "code=null");
                    sendWxMessage();
                }
            }
        });
        themeConfigBuilder = new AuthThemeConfig.Builder()
                .setAuthPageActIn("cmcc_in_activity", "cmcc_in_activity")
                .setAuthPageActOut("cmcc_out_activity", "cmcc_out_activity")
                .setNavColor(Color.parseColor("#EFCE17"))
                .setStatusBar(Color.parseColor("#FFFFFF"), true)
                .setNavTextColor(Color.parseColor("#333333"))
                .setAuthContentView(view)
                .setCheckTipText("")
                .setNumberSize(25, false)
                .setNumberColor(Color.parseColor("#333333"))
                .setNumFieldOffsetY_B(200)
                .setNumFieldOffsetY(200)//?????????Y?????????
                .setLogBtnText("????????????????????????")
                .setLogBtnTextColor(Color.parseColor("#333333"))
                .setLogBtnImgPath("button_round_selector_2")
                .setLogBtnClickListener(new LoginClickListener() {
                    @Override
                    public void onLoginClickStart(Context context, JSONObject jsonObject) {
                        alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                if (event.getAction() == KeyEvent.ACTION_UP) {
                                    dialog.dismiss();
                                    alertDialog = null;
                                }
                                return keyCode == KeyEvent.KEYCODE_BACK;
                            }
                        });
                        alertDialog.show();
                        alertDialog.setContentView(LayoutInflater.from(context).inflate(R.layout.dialog_progress, null));
                    }

                    @Override
                    public void onLoginClickComplete(Context context, JSONObject jsonObject) {
                        if (alertDialog != null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                            alertDialog = null;
                        }
                    }
                })
                .setLogBtn(520, 50)
//                .setLogBtnOffsetY_B(270)//????????????Y?????????
                .setLogBtnOffsetY(260)//????????????Y?????????
                .setPrivacyState(false)//?????????check
                .setPrivacyAlignment("??????????????????" + AuthThemeConfig.PLACEHOLDER + "???ROKI???????????????????????????", "ROKI????????????", String.format("%s", IRokiRestService.UserNotice), "????????????", String.format("%s", IRokiRestService.RegisterAgreement),
                        "", "", "", "")
                .setPrivacyText(12, Color.parseColor("#333333"), Color.parseColor("#333333"), false, false)
                .setClauseColor(Color.parseColor("#333333"), Color.parseColor("#EFCE17"))//????????????
                .setCheckBoxListener(new CheckBoxListener() {
                    @Override
                    public void onLoginClick(Context context, JSONObject jsonObject) {
                        ToastUtils.showShort(R.string.accept_user_privacy_policy);
                    }
                })
                .setCheckBoxImgPath("icon_selected", "icon_select", 18, 18)
                .setPrivacyOffsetY(400)//????????????Y?????????
                .setAppLanguageType(0)
                .setPrivacyBookSymbol(true);
        ;
        AuthThemeConfig build = themeConfigBuilder.build();
        mAuthnHelper.setAuthThemeConfig(build);
    }
    //-------------------------------????????????--------------------------------------------

    /**
     * ??????????????? ??????
     *
     * @param code
     * @param accessToken
     */
    private void login3rd(String code, final String accessToken) {
        LoginHelper.loginWx(cx, code, true);
    }

    /**
     * ????????????
     */
    private void sendWxMessage() {
        Log.i("20171023", "code=null");
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "user_wx_login";
        MobApp.mWxApi.sendReq(req);
    }

}
