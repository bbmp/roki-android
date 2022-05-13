package com.robam.roki.ui.page.login.helper;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.google.common.base.MoreObjects;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.events.UserLoginNewEvent;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;

import static com.legent.plat.services.AccountService.IsOwnUser;
import static com.legent.plat.services.AccountService.LastOwnAccount;
import static com.legent.plat.services.AccountService.getInstance;

/**
 * @ClassName: LoginHelper
 * @Description: 登录帮助类
 * @Author: Hxw
 * @CreateDate: 2021/3/25 10:18
 */
public class LoginHelper {
    private static FragmentActivity cx ;
    private static String  codeWx ;
    /**
     * 是否从一键登录界面进入wx登录
     */
    private static boolean isCmcc;


    public static void loginPassword(FragmentActivity activity ,String sjhm
            , String password
        ){
        cx = activity ;
        ProgressDialogHelper.setRunning(cx, true);
        CloudHelper.getToken("mobilePassword", sjhm, "", password, "", "", "","roki_client", "test", IAppType.RKDRD,
                Reponses.TokenReponse.class, new RetrofitCallback<Reponses.TokenReponse>() {
            @Override
            public void onSuccess(Reponses.TokenReponse tokenReponse) {
                String access_token = tokenReponse.token_type + " " + tokenReponse.access_token;
                Log.i("LOGIN", "access_token--------------------" + access_token);
                getOauth(access_token);
            }

            @Override
            public void onFaild(String err) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show(err);
            }

        });
    }

    /**
     * 验证码登录
     * @param activity
     * @param sjhm
     * @param smsCode
     */
    public static void loginCode(FragmentActivity activity ,String sjhm
            , String smsCode
    ){
        cx = activity ;
        ProgressDialogHelper.setRunning(cx, true);
        CloudHelper.getToken("mobileSmsCode", sjhm, smsCode, "", "", "", "","roki_client", "test", IAppType.RKDRD,
                Reponses.TokenReponse.class, new RetrofitCallback<Reponses.TokenReponse>() {
            @Override
            public void onSuccess(Reponses.TokenReponse tokenReponse) {
                String access_token = tokenReponse.token_type + " " + tokenReponse.access_token;
                Log.i("LOGIN", "access_token--------------------" + access_token);
                getOauth(access_token);
            }

            @Override
            public void onFaild(String err) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show(err);
            }

        });
    }

    /**
     * 绑定微信
     * @param activity
     * @param code
     */
    public static void loginWx(FragmentActivity activity ,String code
            ){
        cx = activity ;
        codeWx  = code ;
        isCmcc = false ;
        getThirdAccessToken("wx" ,code);
    }
    /**
     * 绑定微信
     * @param activity
     * @param code
     */
    public static void loginWx(FragmentActivity activity ,String code ,boolean isCmccLogin
    ){
        cx = activity ;
        codeWx  = code ;
        isCmcc = isCmccLogin;
        getThirdAccessToken("wx" ,code);
    }
    /**
     * 更具code 获取第三方登录accessToken
     * @param loginType
     * @param code
     */
    private static void getThirdAccessToken(final String loginType,String code){
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.getThirdAccessToken(loginType, code, new Callback<Reponses.GetThirdAccessTokenReponse>() {
            @Override
            public void onSuccess(Reponses.GetThirdAccessTokenReponse thirdAccessToken) {
                isBindSjhm(thirdAccessToken.data.accessToken ,thirdAccessToken.data.openId);

            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show(t.getMessage());
            }
        });
    }

    /**
     * 判断该微信是否绑定手机号
     */
    public static void isBindSjhm(String  accessToken , String openId){
        Plat.accountService.isBindSjhm(accessToken, openId, new Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (aBoolean ){
                    //已经绑定手机号 直接登录
                    getToken("wx" , "" , "" ,accessToken ,openId );
                }else {
                    ProgressDialogHelper.setRunning(cx, false);
                    Bundle bundle = new Bundle();
                    bundle.putString("accessToken" , accessToken);
                    bundle.putString("openId" , openId);
                    UIService.getInstance().postPage("LoginBindPhone" ,bundle);

//                    bindWx(accessToken );
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
     * 获取服务器token(微信登录)
     * @param loginType
     * @param accessToken
     * @param openId
     */
    public static void getToken(String loginType , String sjhm, String smsCode
            , String accessToken , String openId ){
        ProgressDialogHelper.setRunning(cx, true);
        CloudHelper.getToken(loginType, sjhm, smsCode, "", accessToken, "", openId,"roki_client", "test", IAppType.RKDRD,
                Reponses.TokenReponse.class, new RetrofitCallback<Reponses.TokenReponse>() {
            @Override
            public void onSuccess(Reponses.TokenReponse tokenReponse) {
                String access_token = tokenReponse.token_type + " " + tokenReponse.access_token;
                Log.i("LOGIN", "access_token--------------------" + access_token);
                getOauth(access_token);
            }

            @Override
            public void onFaild(String err) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show(err);
            }
        });
    }

    /**
     * 获取服务器token(微信登录 / 绑定手机号)
     * @param loginType
     * @param accessToken
     * @param openId
     */
    public static void getTokenBindPhone(String loginType , String sjhm, String smsCode
            , String accessToken , String openId ){
        ProgressDialogHelper.setRunning(cx, true);
        CloudHelper.getToken(loginType, sjhm, smsCode, "", accessToken, "", openId,"roki_client", "test", IAppType.RKDRD,
                Reponses.TokenReponse.class, new RetrofitCallback<Reponses.TokenReponse>() {
                    @Override
                    public void onSuccess(Reponses.TokenReponse tokenReponse) {
                        String access_token = tokenReponse.token_type + " " + tokenReponse.access_token;
                        Log.i("LOGIN", "access_token--------------------" + access_token);
//                getOauth(access_token);
                        isFirstLogin(access_token);
                    }

                    @Override
                    public void onFaild(String err) {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.show(err);
                    }
                });
    }

    /**
     * 判断是否第一次登录
     *
     * @param access_token
     */
    private static void isFirstLogin(final String access_token) {
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

    /**
     * 获取用户信息(登录信息)
     * @param access_token
     */
    public static void getOauth(final String access_token , boolean isFirstLogin) {
        CloudHelper.getOauth( access_token, Reponses.LoginReponse.class, new RetrofitCallback<Reponses.LoginReponse>() {
            @Override
            public void onSuccess(Reponses.LoginReponse loginReponse) {
                if (null != loginReponse) {
                    User user = loginReponse.user;
                    if (user != null){
                        //获取详细信息，性别 ，生日等
                        getUser2(user.id  ,isFirstLogin ,access_token) ;
                    }else {
                        ToastUtils.show("登录获取用户信息失败");
                    }
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
     * 获取用户信息(登录信息)
     * @param access_token
     */
    public static void getOauth(final String access_token) {
        CloudHelper.getOauth(access_token, Reponses.LoginReponse.class, new RetrofitCallback<Reponses.LoginReponse>() {
            @Override
            public void onSuccess(Reponses.LoginReponse loginReponse) {
                if (null != loginReponse) {
                    User user = loginReponse.user;
                    if (user != null){
                        //获取详细信息，性别 ，生日等
                        getUser2(user.id );
                    }else {
                        ToastUtils.show("登录获取用户信息失败");
                    }
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
     * 获取用户信息3.7
     * @param userId
     */
    public static void getUser2(final long userId) {
        CloudHelper.getUser2(userId, Reponses.GetUserReponse.class, new RetrofitCallback<Reponses.GetUserReponse>() {

            @Override
            public void onSuccess(Reponses.GetUserReponse getUserReponse) {
                ProgressDialogHelper.setRunning(cx, false);
                if (null != getUserReponse) {
                    User user = getUserReponse.user;
                    Plat.accountService.mapUsers.put(userId, user);

                    Log.i("LOGIN", "user--------------------" + user.toString());
                    onLoginCompleted(user, false);
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
     * 获取用户信息3.7
     * @param userId
     */
    public static void getUser2(final long userId ,boolean isFirst ,  String access_token) {
        CloudHelper.getUser2(userId, Reponses.GetUserReponse.class, new RetrofitCallback<Reponses.GetUserReponse>() {

            @Override
            public void onSuccess(Reponses.GetUserReponse getUserReponse) {
                if (null != getUserReponse) {
                    User user = getUserReponse.user;
                    user.authorization = access_token ;
                    Plat.accountService.mapUsers.put(userId, user);
                    ProgressDialogHelper.setRunning(cx, false);
                    Log.i("LOGIN", "user--------------------" + user.toString());
                    onLoginCompleted(user, isFirst);
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
     * 登录成功处理
     *
     * @param user
     */
    private static void onLoginCompleted(User user, boolean isFirstLogin) {
        ToastUtils.show("用户登录成功");
        Plat.accountService.onLogin(user);
        EventUtils.postEvent(new UserLoginNewEvent());
        if (isFirstLogin) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            UIService.getInstance().postPage(PageKey.LoginSetPassword, bundle);
        } else {

            if (!isCmcc) {
                UIService.getInstance().popBack();
            } else {
//                MainActivity.start(cx);
                CmccLoginHelper.getInstance().quitAuthActivity();
            }
        }
    }
}
