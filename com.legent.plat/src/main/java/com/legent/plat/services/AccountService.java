package com.legent.plat.services;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.constant.PrefsKey;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.events.UserUpdatedEvent;
import com.legent.plat.exceptions.ExceptionHelper;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.plat.pojos.PlatformUser;
import com.legent.plat.pojos.RCReponse;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.User3rd;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.services.account.AuthCallback;
import com.legent.plat.services.account.IAppOAuthService;
import com.legent.services.AbsService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class AccountService extends AbsService {

    private final static String AutoLogin = "AutoLogin";
    public final static String IsOwnUser = "IsOwnUser";
    private final static String LastUserId = "LastUserId";
    public final static String UserPhone = "UserPhone";
    private final static String LastAccount = "LastAccount";
    public final static String LastOwnAccount = "LastOwnAccount";
    private final static String LastPlatId = "LastPlatId";
    private final static String LastPlat3rd_Nickname = "LastPlat3rd_Nickname";
    private final static String LastPlat3rd_FaceUrl = "LastPlat3rd_FaceUrl";
    private final static String LastPlat3rd_Cert = "LastPlat3rd_Cert";
    private final static String Plat3rd_Pwd = "Plat3rd_Pwd";
    private final static String Authorization = "authorization"; //获取用户信息token
    private static AccountService instance = new AccountService();

    synchronized public static AccountService getInstance() {
        return instance;
    }

    private AccountService() {
    }

    private User curUser;
    public Map<Long, User> mapUsers = Maps.newHashMap();

    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);

        if (Plat.appOAuthService != null) {
            Plat.appOAuthService.init(cx, params);
        }

        boolean autoLogin = PreferenceUtils.getBool(AutoLogin, true);

        if (autoLogin) {
            autoLogin(null);
        }

    }

    @Override
    public void dispose() {
        super.dispose();
        if (Plat.appOAuthService != null) {
            Plat.appOAuthService.dispose();
        }
    }

    // -------------------------------------------------------------------------------
    // IAccountService
    // -------------------------------------------------------------------------------

    public boolean isLogon() {
        return curUser != null;
    }

    public User getCurrentUser() {
        return curUser;
    }

    public long getCurrentUserId() {
        if (curUser != null) {
            return curUser.id;
        }
        return PreferenceUtils.getLong(LastUserId, 0);
    }

    public String getLastAccount() {
        return PreferenceUtils.getString(LastOwnAccount, null);
    }


    public void autoLogin(Callback<User> callback) {
        if (curUser != null) {
            Helper.onSuccess(callback, curUser);
        } else {
            String account = PreferenceUtils.getString(LastAccount, null);
            String authorization = PreferenceUtils.getString(Authorization, null);
            long userId = PreferenceUtils.getLong(LastUserId, 0);
            if (userId != 0) {
                getUser2(userId);
            }
//            loginByLastAccount(callback);
        }
    }


    public void bind3r(String code,String accessToken, final Callback<User> callback) {
        CloudHelper.bind3rd(curUser.id, code, accessToken, "", new Callback<Reponses.GetUserReponse>() {
            @Override
            public void onSuccess(Reponses.GetUserReponse getUserReponse) {
                User user = getUserReponse.user;
                curUser.wxNickname = user.wxNickname;
                Helper.onSuccess(callback, curUser);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void unbind3rd(final VoidCallback callback) {
        CloudHelper.unbind3rd(curUser.id, new VoidCallback() {
            @Override
            public void onSuccess() {
                curUser.wxNickname = null;
//                onLogin(curUser);
                EventUtils.postEvent(new UserUpdatedEvent(curUser));
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void logout(final VoidCallback callback) {
        if (curUser != null) {

            CloudHelper.logout(curUser.TGT, new VoidCallback() {
                @Override
                public void onSuccess() {
                    PreferenceUtils.setBool(PrefsKey.Guided, false);
                    onLogout(curUser);

                }

                @Override
                public void onFailure(Throwable t) {
                    try {
                        callback.onFailure(t);
                    } catch (Exception e) {

                    }

                }
            });
        }
    }

    // -------------------------------------------------------------------------------
    // override
    // -------------------------------------------------------------------------------

    public void login(final String account, final String password, final Callback<User> callback) {
        CloudHelper.login(account, password, new Callback<User>() {

            @Override
            public void onSuccess(User user) {
                user.password = password;
                LogUtils.i("20180205", "ac::" + account);
                onLoginForOwnUser(account, user);
                Helper.onSuccess(callback, user);
                PreferenceUtils.setBool(PrefsKey.Guided, true);
                // setLoginTimeOut(user);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }


    public void expressLogin(final String phone, String verifyCode, final Callback<User> callback) {
        CloudHelper.expressLogin(phone, verifyCode, new Callback<User>() {

            @Override
            public void onSuccess(User user) {
                onLoginForOwnUser(phone, user);
                Helper.onSuccess(callback, user);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    //微信第三方登录
    public void login3rd(String appSource, String openId, String phone, final String verifyCode, final Callback<User> callback) {
        CloudHelper.phoneBind(appSource, openId, phone, verifyCode, new Callback<User>() {

            @Override
            public void onSuccess(User user) {
                LogUtils.i("20171030", "accountService");
                user.setPassword(verifyCode);
                LogUtils.i("20171030", "user.password::" + user.password);
                onLoginForThirdParty(user, user.phone, user.nickname, user.figureUrl, user.thirdInfos.
                        accessToken, user.password);
                Helper.onSuccess(callback, user);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void getUser(final long userId, final Callback<User> callback) {
        if (mapUsers.containsKey(userId)) {
            User user = mapUsers.get(userId);
            Helper.onSuccess(callback, user);
        } else {
            CloudHelper.getUser(userId, new Callback<User>() {

                @Override
                public void onSuccess(User user) {
                    mapUsers.put(userId, user);
                    Helper.onSuccess(callback, user);
                }

                @Override
                public void onFailure(Throwable t) {
                    Helper.onFailure(callback, t);
                }
            });
        }
    }

    /**
     * 获取用户信息3.7
     *
     * @param userId
     * @param callback
     */
    public void getUser2(final long userId) {
        CloudHelper.getUser2(userId, Reponses.GetUserReponse.class, new RetrofitCallback<Reponses.GetUserReponse>() {
            @Override
            public void onSuccess(Reponses.GetUserReponse getUserReponse) {
                if (null != getUserReponse)
                    mapUsers.put(userId, getUserReponse.user);

            }

            @Override
            public void onFaild(String err) {

            }
        });
    }

    public void bindNewPhone(long id,  final String phone, final String verifyCode,
                           final VoidCallback callback) {
        CloudHelper.bindNewPhone(id, phone, verifyCode,  new VoidCallback() {
            @Override
            public void onSuccess() {
                curUser.phone = phone;
                saveUser2Preference(curUser);
                EventUtils.postEvent(new UserUpdatedEvent(curUser));
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }
    /**
     * 修改用户信息,新增birthday
     *
     * @param id
     * @param name
     * @param phone
     * @param email
     * @param gender
     * @param birthday
     * @param callback
     */
    public void updateUser(long id, final String name, final String phone, final String email,
                           final boolean gender, final Date birthday, final  String sex ,final VoidCallback callback) {
        CloudHelper.updateUser(id, name, phone, email, gender, birthday, sex ,new VoidCallback() {
            @Override
            public void onSuccess() {
                curUser.nickname = name;
                curUser.phone = phone;
                curUser.email = email;
                curUser.gender = gender;
                curUser.birthday = birthday;
                curUser.sex = sex;
                saveUser2Preference(curUser);
                EventUtils.postEvent(new UserUpdatedEvent(curUser));
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void updateFigure(long userId, String figure,
                             final Callback<String> callback) {
        CloudHelper.updateFigure(userId, figure, new Callback<String>() {
            @Override
            public void onSuccess(String s) {
                curUser.figureUrl = s;
                EventUtils.postEvent(new UserUpdatedEvent(curUser));
                Helper.onSuccess(callback, s);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });

    }


    public void unRegistAccount(long userId, String phone, String verifyCode, Callback<Reponses.UnregisterResponse> callback) {
//        super.unRegistAccount(userId, phone, verifyCode, callback);
        CloudHelper.unregistByPhone(userId, phone, verifyCode, callback);
    }

    public void updatePassword(long userId, String oldPwd, final String newPwd,
                               final VoidCallback callback) {
        CloudHelper.updatePassword(userId, oldPwd, newPwd, new VoidCallback() {
            @Override
            public void onSuccess() {
                curUser.password = newPwd;
                EventUtils.postEvent(new UserUpdatedEvent(curUser));
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    //-------------------------------------------------------------------------------
    // override
    //-------------------------------------------------------------------------------

    private void loginByLastAccount(final Callback<User> callback) {

        String account = PreferenceUtils.getString(LastAccount, null);
        if (Strings.isNullOrEmpty(account)) {
            Helper.onFailure(callback, new Exception("not found any user"));
            return;
        }

        boolean isOwnUser = PreferenceUtils.getBool(IsOwnUser, true);
        if (isOwnUser) {
            String pwd = PreferenceUtils.getString(account, null);
            login(account, pwd, callback);
        } else {
            int platId = PreferenceUtils.getInt(LastPlatId, 0);
            String nickname = PreferenceUtils.getString(LastPlat3rd_Nickname,
                    null);
            String faceUrl = PreferenceUtils.getString(LastPlat3rd_FaceUrl,
                    null);
            String cert = PreferenceUtils.getString(LastPlat3rd_Cert, null);

            CloudHelper.loginFrom3rd(platId, account, nickname, faceUrl, cert,
                    new Callback<User>() {

                        @Override
                        public void onSuccess(User user) {
                            Helper.onSuccess(callback, user);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Helper.onFailure(callback, t);
                        }
                    });
        }
    }

    private void bindThirdPlatAccount(Context cx, final int platId,
                                      final Callback<PlatformUser> callback) {

        if (Plat.appOAuthService != null) {
            Plat.appOAuthService.removeAuth(platId);
        }

        authorizeBy3rd(cx, platId, new AuthCallback() {

            @Override
            public void onError(Throwable t) {
                Helper.onFailure(callback, t);
            }

            @Override
            public void onAuthCompleted(PlatformUser platUser) {
                Helper.onSuccess(callback, platUser);
            }

            @Override
            public void onCancel() {
                Helper.onFailure(callback, ExceptionHelper
                        .newPlatException(ResultCodeManager.EC_CancelAuth3rd));
            }
        });
    }


    private void authorizeBy3rd(Context cx, int platId,
                                AuthCallback callback) {

        if (Plat.appOAuthService != null) {
            Plat.appOAuthService.authorize(cx, platId, callback);
        } else {
            if (callback != null) {
                callback.onError(new Throwable("Plat.appOAuthService is null"));
            }
        }
    }

    public void onLoginForOwnUser(String account, User user) {
        user.loginPlatId = IAppOAuthService.PlatId_Self;

        PreferenceUtils.setBool(IsOwnUser, true);
        PreferenceUtils.setString(LastOwnAccount, account);
        PreferenceUtils.setString(account, user.password);
        onLogin(user);
    }

    private void onLoginForThirdParty(User user, String account,
                                      String nickname, String faceUrl,
                                      String cert, String pwd) {

        PreferenceUtils.setBool(IsOwnUser, false);
        PreferenceUtils.setString(LastPlat3rd_Nickname, nickname);
        PreferenceUtils.setString(LastOwnAccount, account);
        PreferenceUtils.setString(LastPlat3rd_FaceUrl, faceUrl);
        PreferenceUtils.setString(LastPlat3rd_Cert, cert);
        PreferenceUtils.setString(Plat3rd_Pwd, pwd);
        onLogin(user);
    }

    public void onLogin(User user) {
        curUser = user;
        user.loginPlatId = IAppOAuthService.PlatId_Self;
        saveUser2Preference(user);

        EventUtils.postEvent(new UserLoginEvent(user));
    }

    private void onLogout(User user) {

        saveUser2Preference(user, null);
        curUser = null;
        EventUtils.postEvent(new UserLogoutEvent(user));
    }

    private void saveUser2Preference(User user) {
        PreferenceUtils.setLong(LastUserId, user != null ? user.id : 0);
        PreferenceUtils.setString(LastAccount, user != null ? user.getAccount() : null);
        PreferenceUtils.setString(LastOwnAccount, user != null ? user.getAccount() : null);
        PreferenceUtils.setString(Authorization, user != null ? user.authorization : null);
        PreferenceUtils.setString(UserPhone, user.phone);
        PreferenceUtils.setBool(PrefsKey.Guided, true);
        PreferenceUtils.setBool(IsOwnUser, true);
    }

    private void saveUser2Preference(User user, String str) {
        PreferenceUtils.setString(LastOwnAccount, user != null ? user.getAccount() : null);
        if (TextUtils.isEmpty(str)) {
            user = null;
        }
        PreferenceUtils.setLong(LastUserId, user != null ? user.id : 0);
        PreferenceUtils.setString(LastAccount, user != null ? user.getAccount() : null);
    }

    //---------------------------------3.6----------------------------------------

    /**
     * 获取手机号
     *
     * @param token
     * @param callback
     */
    public void getPhoneNum(final String token, final Callback<String> callback) {
        CloudHelper.getPhoneNum(token, new Callback<String>() {

            @Override
            public void onSuccess(String phoneNum) {
                Helper.onSuccess(callback, phoneNum);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    /**
     * 获取token
     *
     * @param loginType
     * @param sjhm
     * @param callback
     */
    public void token(String loginType, String sjhm,
                      final Callback<Reponses.TokenReponse> callback) {
        CloudHelper.token(loginType, sjhm, "roki_client", "test", IAppType.RKDRD, new Callback<Reponses.TokenReponse>() {
            @Override
            public void onSuccess(Reponses.TokenReponse tokenReponse) {
                Helper.onSuccess(callback, tokenReponse);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

//    public void getToken(String loginType, String sjhm, String smsCode
//            , String password
//            , String accessToken
//            , String refreshToken
//            , String openId
//            , final Callback<Reponses.TokenReponse> callback) {
//        CloudHelper.getToken(loginType, sjhm, smsCode, password, accessToken, refreshToken, openId, "roki_client", "test", IAppType.RKDRD, new Callback<Reponses.TokenReponse>() {
//            @Override
//            public void onSuccess(Reponses.TokenReponse tokenReponse) {
//                Helper.onSuccess(callback, tokenReponse);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onFailure(callback, t);
//            }
//        });
//    }

    /**
     * 根据code获取第三方登录token
     *
     * @param loginType
     * @param code
     * @param callback
     */
    public void getThirdAccessToken(String loginType, String code, final Callback<Reponses.GetThirdAccessTokenReponse> callback) {
        CloudHelper.getThirdAccessToken(loginType, code, new Callback<Reponses.GetThirdAccessTokenReponse>() {

            @Override
            public void onSuccess(Reponses.GetThirdAccessTokenReponse data) {
                Helper.onSuccess(callback, data);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void isBindSjhm(String accessToken, String openId,
                           final Callback<Boolean> callback) {
        CloudHelper.isBindSjhm("wx", accessToken, openId, new Callback<Reponses.IsFirstLoginResponse>() {
            @Override
            public void onSuccess(Reponses.IsFirstLoginResponse isFirstLoginResponse) {
                Helper.onSuccess(callback, isFirstLoginResponse.data);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    /**
     * 判断是否第一次登录
     *
     * @param authorization
     * @param callback
     */
    public void isFirstLogin(String authorization,
                             final Callback<Boolean> callback) {
        CloudHelper.isFirstLogin(authorization, new Callback<Reponses.IsFirstLoginResponse>() {
            @Override
            public void onSuccess(Reponses.IsFirstLoginResponse isFirstLoginResponse) {
                Helper.onSuccess(callback, isFirstLoginResponse.data);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    /**
     * 获取用户信息
     *
     * @param phone
     * @param authorization
     * @param callback
     */
//    public void getOauth(final String phone, final String authorization, final Callback<User> callback) {
//        CloudHelper.getOauth(authorization, new Callback<User>() {
//
//            @Override
//            public void onSuccess(User user) {
//                Helper.onSuccess(callback, user);
//                Log.i("LOGIN", "user--------------------" + user.toString());
//                user.authorization = authorization;
////                Plat.accountService.onLogin(user);
//                getUser2(user.id, callback);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onFailure(callback, t);
//            }
//        });
//    }

    /**
     * 设置新密码
     *
     * @param password
     * @param authorization
     * @param callback
     */
    public void setPassword(String authorization, final String password, final Callback<RCReponse> callback) {
        CloudHelper.setPassword(authorization, password, new Callback<RCReponse>() {

            @Override
            public void onSuccess(RCReponse rcReponse) {
                Helper.onSuccess(callback, rcReponse);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }
}
