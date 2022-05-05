package com.legent.plat.io.cloud;

import android.util.Log;

import com.google.gson.Gson;
import com.legent.Callback;
import com.legent.ContextIniter;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.io.RCRetrofitCallback;
import com.legent.plat.io.RCRetrofitCallbackWithVoid;
import com.legent.plat.io.cloud.Reponses.AddDeviceGroupResponse;
import com.legent.plat.io.cloud.Reponses.ChatGetReponse;
import com.legent.plat.io.cloud.Reponses.ChatSendReponse;
import com.legent.plat.io.cloud.Reponses.ChatisExistResponse;
import com.legent.plat.io.cloud.Reponses.CheckAppVerReponse;
import com.legent.plat.io.cloud.Reponses.GetAppIdReponse;
import com.legent.plat.io.cloud.Reponses.GetDeviceGroupsResponse;
import com.legent.plat.io.cloud.Reponses.GetDevicePesponse;
import com.legent.plat.io.cloud.Reponses.GetDeviceUsersResponse;
import com.legent.plat.io.cloud.Reponses.GetDevicesResponse;
import com.legent.plat.io.cloud.Reponses.GetDynamicPwdRequestReponse;
import com.legent.plat.io.cloud.Reponses.GetSnForDeviceResponse;
import com.legent.plat.io.cloud.Reponses.GetStartImagesResponse;
import com.legent.plat.io.cloud.Reponses.GetUserReponse;
import com.legent.plat.io.cloud.Reponses.GetVerifyCodeReponse;
import com.legent.plat.io.cloud.Reponses.IsExistedResponse;
import com.legent.plat.io.cloud.Reponses.LoginReponse;
import com.legent.plat.io.cloud.Reponses.UpdateFigureReponse;
import com.legent.plat.io.cloud.Requests.AccountRequest;
import com.legent.plat.io.cloud.Requests.AddDeviceGroupRequest;
import com.legent.plat.io.cloud.Requests.AppTypeRequest;
import com.legent.plat.io.cloud.Requests.AppUserGuidRequest;
import com.legent.plat.io.cloud.Requests.Bind3rdRequest;
import com.legent.plat.io.cloud.Requests.BindDeviceRequest;
import com.legent.plat.io.cloud.Requests.ChatGetRequest;
import com.legent.plat.io.cloud.Requests.ChatSendRequest;
import com.legent.plat.io.cloud.Requests.ChatisExistRequest;
import com.legent.plat.io.cloud.Requests.DeleteDeviceUsersRequest;
import com.legent.plat.io.cloud.Requests.DeviceByParamsConsumables;
import com.legent.plat.io.cloud.Requests.ExpressLoginRequest;
import com.legent.plat.io.cloud.Requests.GetAppIdRequest;
import com.legent.plat.io.cloud.Requests.GetDeviceBySnRequest;
import com.legent.plat.io.cloud.Requests.GetPurchaseUrlRequest;
import com.legent.plat.io.cloud.Requests.GetVerifyCodeRequest;
import com.legent.plat.io.cloud.Requests.GuidRequest;
import com.legent.plat.io.cloud.Requests.LoginFrom3rdRequest;
import com.legent.plat.io.cloud.Requests.LoginRequest;
import com.legent.plat.io.cloud.Requests.LogoutRequest;
import com.legent.plat.io.cloud.Requests.RegistByEmailRequest;
import com.legent.plat.io.cloud.Requests.RegistByPhoneRequest;
import com.legent.plat.io.cloud.Requests.ReportLogRequest;
import com.legent.plat.io.cloud.Requests.ResetPwdByEmailRequest;
import com.legent.plat.io.cloud.Requests.ResetPwdByPhoneRequest;
import com.legent.plat.io.cloud.Requests.Unbind3rdRequest;
import com.legent.plat.io.cloud.Requests.UnbindDeviceRequest;
import com.legent.plat.io.cloud.Requests.UpdateDeviceNameRequest;
import com.legent.plat.io.cloud.Requests.UpdateFigureRequest;
import com.legent.plat.io.cloud.Requests.UpdateGroupNameRequest;
import com.legent.plat.io.cloud.Requests.UpdatePasswordRequest;
import com.legent.plat.io.cloud.Requests.UpdateUserRequest;
import com.legent.plat.io.cloud.Requests.UserGroupGuidRequest;
import com.legent.plat.io.cloud.Requests.UserGroupRequest;
import com.legent.plat.io.cloud.Requests.UserGuidRequest;
import com.legent.plat.io.cloud.Requests.UserRequest;
import com.legent.plat.pojos.AppVersionInfo;
import com.legent.plat.pojos.ChatMsg;
import com.legent.plat.pojos.RCReponse;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.DeviceGroupInfo;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.device.Payload;
import com.legent.services.RestfulService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.NetworkUtils;
import com.legent.utils.api.PackageUtils;
import com.legent.utils.api.ToastUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Query;

import static com.legent.ContextIniter.context;


/**
 * Created by sylar on 15/7/23.
 */
public class CloudHelper {

    static ICloudService svr = getRestfulApi(ICloudService.class);

    static public <T> T getRestfulApi(Class<T> apiClazz) {
        return RestfulService.getInstance().createApi(apiClazz);
    }

    // ==========================================================Common Start==========================================================

    static public String getAppGuid(String appType, String token, String phoneToken, String versionName) {
        GetAppIdReponse res = svr.getAppId(new GetAppIdRequest(appType, token, phoneToken, versionName));
        return res != null ? res.appGuid : null;
    }

    static public void getAppGuid(String appType, String token, String phoneToken, String versionName,
                                  final Callback<String> callback) {
        svr.getAppId(new GetAppIdRequest(appType, token, phoneToken, versionName),
                new RCRetrofitCallback<GetAppIdReponse>(callback) {
                    @Override
                    protected void afterSuccess(GetAppIdReponse result) {
                        LogUtils.i("20170926", "result" + result);
                        callback.onSuccess(result.appGuid);
                    }
                });
    }

    static public void bindAppGuidAndUser(String appGuid, long userId,
                                          final VoidCallback callback) {
        svr.bindAppGuidAndUser(new AppUserGuidRequest(appGuid, userId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void unbindAppGuidAndUser(String appGuid, long userId,
                                            final VoidCallback callback) {
        svr.unbindAppGuidAndUser(new AppUserGuidRequest(appGuid, userId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void checkAppVersion(String appType,
                                       final Callback<AppVersionInfo> callback) {
        AppTypeRequest appTypeRequest;
        if (IAppType.RKPAD.equals(appType)) {
            appTypeRequest = new AppTypeRequest(appType, Plat.fanGuid.getDeviceTypeId());
        } else {
            appTypeRequest = new AppTypeRequest(appType);
        }
        svr.checkAppVersion(appTypeRequest,
                new RCRetrofitCallback<CheckAppVerReponse>(callback) {
                    @Override
                    protected void afterSuccess(CheckAppVerReponse result) {
                        callback.onSuccess(result.verInfo);
                    }
                });
    }

    static public void reportLog(String appGuid, int logType, String log,
                                 VoidCallback callback) {
        String version = null;
        if (IAppType.RKDRD.equals(Plat.appType))
            version = PackageUtils.getVersionName(context);
        else
            version = String.valueOf(PackageUtils.getAppVersionCode(Plat.app));
        svr.reportLog(new ReportLogRequest(appGuid, version, logType, log),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getStartImages(String appType,
                                      final Callback<List<String>> callback) {
        svr.getStartImages(new AppTypeRequest(appType),
                new RCRetrofitCallback<GetStartImagesResponse>(
                        callback) {
                    @Override
                    protected void afterSuccess(GetStartImagesResponse result) {
                        callback.onSuccess(result.images);
                    }
                });
    }

    static public void sendChatMsg(long userId, String msg,
                                   final Callback<ChatSendReponse> callback) {
        svr.sendChatMsg(new ChatSendRequest(userId, msg), new RCRetrofitCallback<ChatSendReponse>(callback) {
            @Override
            protected void afterSuccess(ChatSendReponse result) {
                Helper.onSuccess(callback, result);
            }
        });
    }

    static public void getChatBefore(long userId, Date date, int count,
                                     final Callback<List<ChatMsg>> callback) {
        svr.getChatBefore(new ChatGetRequest(userId, date, count),
                new RCRetrofitCallback<ChatGetReponse>(callback) {
                    @Override
                    protected void afterSuccess(ChatGetReponse result) {
                        callback.onSuccess(result.msgList);
                    }
                });
    }

    static public void getChatAfter(long userId, Date date, int count,
                                    final Callback<List<ChatMsg>> callback) {
        svr.getChatAfter(new ChatGetRequest(userId, date, count),
                new RCRetrofitCallback<ChatGetReponse>(callback) {
                    @Override
                    protected void afterSuccess(ChatGetReponse result) {
                        callback.onSuccess(result.msgList);
                    }
                });
    }

    static public void isExistChatMsg(long userId, Date date,
                                      final Callback<Boolean> callback) {
        svr.isExistChatMsg(new ChatisExistRequest(userId, date),
                new RCRetrofitCallback<ChatisExistResponse>(callback) {
                    @Override
                    protected void afterSuccess(ChatisExistResponse result) {
                        callback.onSuccess(result.existed);
                    }
                });
    }

    // ==========================================================User Start==========================================================
    static public void isExisted(String account,
                                 final Callback<Boolean> callback) {
        svr.isExisted(new AccountRequest(account),
                new RCRetrofitCallback<IsExistedResponse>(callback) {
                    @Override
                    protected void afterSuccess(IsExistedResponse result) {
                        callback.onSuccess(result.existed);
                    }
                });
    }

    static public void registByPhone(String phone, String nickname,
                                     String password, String figure, boolean gender, String verifyCode,
                                     VoidCallback callback) {
        svr.registByPhone(new RegistByPhoneRequest(phone, nickname, password,
                        figure, gender, verifyCode),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void registByEmail(String email, String nickname,
                                     String password, String figure, boolean gender,
                                     VoidCallback callback) {
        svr.registByEmail(new RegistByEmailRequest(email, nickname, password,
                figure, gender), new RCRetrofitCallbackWithVoid<RCReponse>(
                callback));

    }

    static public void login(String account, String password,
                             final Callback<User> callback) {
        svr.login(new LoginRequest(account, password),
                new RCRetrofitCallback<LoginReponse>(callback) {
                    @Override
                    protected void afterSuccess(LoginReponse result) {
                        if (result != null) {
                            if (result.user != null) {
                                result.user.TGT = result.tgt;
                                callback.onSuccess(result.user);
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
                        callback.onFailure(e);
                    }
                });
    }
    //------------------------------------3.6------------------------------------------------
    /**
     * 获取手机号
     * @param token
     * @param callback
     */
    static public void getPhoneNum(String token,
                             final Callback<String> callback) {
        svr.getPhoneNum(token,
                new RCRetrofitCallback<Reponses.GetPhoneNumReponse>(callback) {
                    @Override
                    protected void afterSuccess(Reponses.GetPhoneNumReponse result) {
                        if (result != null) {
                            if (result.data != null) {
                                callback.onSuccess(result.data);
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
                        callback.onFailure(e);
                    }
                });
    }
    /**
     * 根据token获取第三方登录token
     * @param
     * @param callback
     */
    static public void getThirdAccessToken(String loginType,String code,
                                   final Callback<Reponses.GetThirdAccessTokenReponse> callback) {
        svr.getThirdAccessToken(loginType, code,
                new RCRetrofitCallback<Reponses.GetThirdAccessTokenReponse>(callback){
                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
                    }

                    @Override
                    protected void afterSuccess(Reponses.GetThirdAccessTokenReponse result) {
                        if (result != null) {
                                callback.onSuccess(result);
                        }
                    }
                });
    }

    /**
     * 判断是否绑定手机号
     * @param
     * @param callback
     */
    static public void isBindSjhm(String loginType ,String accessToken, String openId,
                                           final Callback<Reponses.IsFirstLoginResponse> callback) {
        svr.isBindSjhm(loginType, accessToken, openId ,
                new RCRetrofitCallback<Reponses.IsFirstLoginResponse>(callback) {
                    @Override
                    protected void afterSuccess(Reponses.IsFirstLoginResponse result) {
                        if (result != null) {
                            if (result != null) {
                                callback.onSuccess(result);
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
                        callback.onFailure(e);
                    }
                });
    }
    /**
     * 获取token
     * @param loginType
     * @param sjhm
     * @param client_id
     * @param client_secret
     * @param appType
     * @param callback
     */
    public static void token(String loginType,String sjhm,String client_id,String client_secret,String appType,
                                   final Callback<Reponses.TokenReponse> callback) {
        svr.token(loginType ,sjhm ,client_id ,client_secret ,appType,
                new RCRetrofitCallback<Reponses.TokenReponse>(callback) {
                    @Override
                    protected void afterSuccess(Reponses.TokenReponse result) {
                        if (result != null) {
                                callback.onSuccess(result);
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
                        callback.onFailure(e);
                    }
                });
    }

    public static void getToken(String loginType
            ,String sjhm
            ,String smsCode
            ,String password
            ,String accessToken
            ,String refreshToken
            ,String openId
            ,String client_id
            ,String client_secret
            ,String appType
            , final Callback<Reponses.TokenReponse> callback) {
        svr.getToken(loginType ,sjhm ,smsCode ,password ,accessToken ,refreshToken,openId , client_id ,client_secret ,appType,
                new RCRetrofitCallback<Reponses.TokenReponse>(callback) {
                    @Override
                    protected void afterSuccess(Reponses.TokenReponse result) {
                        if (result != null) {
                            callback.onSuccess(result);
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
//                        callback.onFailure(e);
                    }
                });
    }
    /**
     * 判断是否第一次登录
     * @param authorization
     * @param callback
     */
    public static void isFirstLogin(String authorization,
                             final Callback<Reponses.IsFirstLoginResponse> callback) {
        svr.isFirstLogin(authorization,
                new RCRetrofitCallback<Reponses.IsFirstLoginResponse>(callback) {
                    @Override
                    protected void afterSuccess(Reponses.IsFirstLoginResponse result) {
                        if (result != null) {
                            callback.onSuccess(result);
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
                        callback.onFailure(e);
                    }
                });
    }
    /**
     * 获取用户信息
     * @param authorization
     * @param callback
     */
    public static void getOauth(String authorization,
                                    final Callback<User> callback) {
        svr.getOauth(authorization,
                new RCRetrofitCallback<LoginReponse>(callback) {
                    @Override
                    protected void afterSuccess(LoginReponse result) {
                        if (result != null) {
                            if (result.user != null) {
                                result.user.TGT = result.tgt;
                                callback.onSuccess(result.user);
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
                        callback.onFailure(e);
                    }
                });
    }
    /**
     * 设置用户密码
     * @param authorization
     * @param callback
     */
    public static void setPassword(String authorization,String password ,
                                final Callback<RCReponse> callback) {
        svr.setPassword(authorization,password ,
                new RCRetrofitCallback<RCReponse>(callback) {
                    @Override
                    protected void afterSuccess(RCReponse result) {
                        if (result != null) {
                                callback.onSuccess(result);
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
                        callback.onFailure(e);
                    }
                });
    }
    //roki用户注销
    static public void unregistByPhone(long userId, String phone, String verifyCode,
                                       final Callback callback) {

        svr.unregistByPhone(userId, phone, verifyCode, new RCRetrofitCallback<Reponses.UnregisterResponse>(callback) {

                    @Override
                    protected void afterSuccess(Reponses.UnregisterResponse result) {
                        callback.onSuccess(result);
                    }
                }
        );
    }


    static public void expressLogin(String phone, String verifyCode,
                                    final Callback<User> callback) {

        svr.expressLogin(new ExpressLoginRequest(phone, verifyCode),
                new RCRetrofitCallback<LoginReponse>(callback) {
                    @Override
                    protected void afterSuccess(LoginReponse result) {
                        result.user.TGT = result.tgt;
                        callback.onSuccess(result.user);
                    }
                });

    }

    static public void logout(String tgt, VoidCallback callback) {
        svr.logout(new LogoutRequest(tgt),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getUser(long userId, final Callback<User> callback) {
        svr.getUser(new UserRequest(userId),
                new RCRetrofitCallback<GetUserReponse>(callback) {
                    @Override
                    protected void afterSuccess(GetUserReponse result) {
                        callback.onSuccess(result.user);
                    }
                });
    }

    /**
     * 获取用户信息3.7
     * @param userId
     * @param callback
     */
    public static void getUser2(long userId, final Callback<User> callback) {
        svr.getUser2(new UserRequest(userId),
                new RCRetrofitCallback<GetUserReponse>(callback) {
                    @Override
                    protected void afterSuccess(GetUserReponse result) {
                        callback.onSuccess(result.user);
                    }
                });
    }
    static public void updateUser(long id, String name, String phone,
                                  String email, boolean gender, VoidCallback callback) {
        svr.updateUser(new UpdateUserRequest(id, name, phone, email, gender),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }
    static public void bindNewPhone(long id,  String phone,
                                  String verifyCode, VoidCallback callback) {
        svr.bindNewPhone(new Requests.BindNewPhoneRequest(id, phone, verifyCode),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }
    /**
     * 修改用户信息,新增生日修改
     * @param id
     * @param name
     * @param phone
     * @param email
     * @param gender
     * @param birthday
     * @param callback
     */
    static public void updateUser(long id, String name, String phone,
                                  String email, boolean gender, Date birthday,String sex , VoidCallback callback) {
        svr.updateUser(new UpdateUserRequest(id, name, phone, email, gender,birthday ,sex),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }
    static public void updatePassword(long userId, String oldPwd,
                                      String newPwd, VoidCallback callback) {
        svr.updatePassword(new UpdatePasswordRequest(userId, oldPwd, newPwd),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void updateFigure(long userId, String figure,
                                    final Callback<String> callback) {
        svr.updateFigure(
                new UpdateFigureRequest(userId, figure),
                new RCRetrofitCallback<UpdateFigureReponse>(callback) {
                    @Override
                    protected void afterSuccess(UpdateFigureReponse result) {
                        callback.onSuccess(result.figureUrl);
                    }
                });
    }

    static public void getVerifyCode(String phone,
                                     final Callback<String> callback) {
        svr.getVerifyCode(
                new GetVerifyCodeRequest(phone),
                new RCRetrofitCallback<GetVerifyCodeReponse>(callback) {
                    @Override
                    protected void afterSuccess(GetVerifyCodeReponse result) {
                        callback.onSuccess(result.verifyCode);
                    }
                });
    }

    static public void getDynamicPwd(String phone, final Callback<String> callback) {
        svr.getDynamicPwd(new GetVerifyCodeRequest(phone),
                new RCRetrofitCallback<GetDynamicPwdRequestReponse>(callback) {
                    @Override
                    protected void afterSuccess(GetDynamicPwdRequestReponse result) {
                        callback.onSuccess(result.dynamicPwd);
                    }
                });
    }

    static public void resetPasswordByPhone(String phone, String newPwd,
                                            String verifyCode, VoidCallback callback) {
        svr.resetPasswordByPhone(new ResetPwdByPhoneRequest(phone, newPwd,
                verifyCode), new RCRetrofitCallbackWithVoid<RCReponse>(
                callback));
    }

    static public void resetPasswordByEmail(String email, VoidCallback callback) {
        svr.resetPasswordByEmail(new ResetPwdByEmailRequest(email),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void loginFrom3rd(int platId, String userId3rd,
                                    String nickname, String figureUrl, String token,
                                    final Callback<User> callback) {
        svr.loginFrom3rd(new LoginFrom3rdRequest(platId, userId3rd, nickname,
                        figureUrl, token),
                new RCRetrofitCallback<LoginReponse>(callback) {
                    @Override
                    protected void afterSuccess(LoginReponse result) {
                        callback.onSuccess(result.user);
                    }
                });
    }

    static public void bind3rd(long userId,  String code, String accessToken, String appleUserId, final Callback<GetUserReponse> callback) {
        svr.bind3rd(new Bind3rdRequest(userId, "wx", IAppType.RKDRD, code ,accessToken , appleUserId),
                new RCRetrofitCallback<GetUserReponse>(callback){
                    @Override
                    public void failure(RetrofitError e) {
                        callback.onFailure(e);
                    }

                    @Override
                    protected void afterSuccess(GetUserReponse result) {
                        callback.onSuccess(result);
//                        super.afterSuccess(result);
                    }
                });
    }

    static public void unbind3rd(long userId,
                                 VoidCallback callback) {
        svr.unbind3rd(new Unbind3rdRequest(userId, "wx"),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));

    }


    static public void otherLogin(String appSource, String appType, String code, String accessToken,
                                  final Callback<Reponses.OtherLoginResponse> callback) {
        //  LogUtils.i("20171023","otherLogin");
        svr.otherLogin(new Requests.OtherLoginRequest(appSource, appType, code, accessToken), new RCRetrofitCallback<Reponses.OtherLoginResponse>(callback) {

            @Override
            protected void afterSuccess(Reponses.OtherLoginResponse result) {
                // ToastUtils.show("请求成功", Toast.LENGTH_SHORT);
               /* if (result!=null){
                    LogUtils.i("20171023","result1::"+result.rc);
                }*/
                callback.onSuccess(result);
            }

        });
    }

    static public void phoneBind(String appSource, String openId, String phone, String verifyCode,
                                 final Callback<User> callback) {
        svr.phoneBind(new Requests.PhoneBindRequest(appSource, openId, phone, verifyCode), new RCRetrofitCallback<Reponses.PhoneBindResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.PhoneBindResponse result) {
                //  ToastUtils.show("请求成功", Toast.LENGTH_SHORT);
               /* if (result!=null){
                    LogUtils.i("20171023","result2::"+result.toString());
                }*/
                callback.onSuccess(result.user);
            }
        });
    }

    static public void getReportCode(long userId, String deviceGuid, String functionCode, String dc, final Callback<Reponses.GetReportResponse> callback) {
        svr.getReportCode(new Requests.ReportCodeRequest(userId, deviceGuid, functionCode, dc), new RCRetrofitCallback<Reponses.GetReportResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.GetReportResponse result) {
                super.afterSuccess(result);
                callback.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError e) {
                super.failure(e);
            }
        });
    }

    static public void getLookUpCode(long userId, String deviceGuid, String dc, final Callback<Reponses.GetLookUpResponse> callback) {
        svr.getLookUpCode(new Requests.LookUpCodeRequest(userId, deviceGuid, dc), new RCRetrofitCallback<Reponses.GetLookUpResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.GetLookUpResponse result) {
                super.afterSuccess(result);
                LogUtils.i("20180727", "result:" + result.toString());
                callback.onSuccess(result);
            }
        });
    }

    static public void getStoveUpCode(long userId, String deviceGuid, String dc, final Callback<Reponses.GetLookUpResponse> callback) {
        svr.getStoveUpCode(new Requests.LookUpCodeRequest(userId, deviceGuid, dc), new RCRetrofitCallback<Reponses.GetLookUpResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.GetLookUpResponse result) {
                super.afterSuccess(result);
                LogUtils.i("20180727", "result:" + result.toString());
                callback.onSuccess(result);
            }
        });
    }

    static public void getCookerCode3(long userId, String deviceGuid, String dc, final Callback<Reponses.GetLookUpResponse> callback) {
        svr.getCookerCode3(new Requests.LookUpCodeRequest(userId, deviceGuid, dc), new RCRetrofitCallback<Reponses.GetLookUpResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.GetLookUpResponse result) {
                super.afterSuccess(result);
                LogUtils.i("20180727", "result:" + result.toString());
                callback.onSuccess(result);
            }
        });
    }


    static public void getCheck(String deviceType, String version, final Callback<Reponses.GetCheckResponse> callback) {
        svr.getCheck(new Requests.CheckRequest(deviceType, version), new RCRetrofitCallback<Reponses.GetCheckResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.GetCheckResponse result) {
                super.afterSuccess(result);
                callback.onSuccess(result);
            }
        });
    }

    static public void submitRecord(long userId, String deviceGuid, String model, String workTime, String orderTime
            , final Callback<Reponses.SubmitResponse> callback) {

        svr.submitRecord(userId+"111",new Requests.SubmitRequest(deviceGuid, model, workTime, orderTime),
                new RCRetrofitCallback<Reponses.SubmitResponse>(callback) {
                    @Override
                    protected void afterSuccess(Reponses.SubmitResponse result) {
                        super.afterSuccess(result);
                        callback.onSuccess(result);
                    }
                });
    }

    static public void queryRecord(long userId, String deviceGuid, String size, final Callback<Reponses.QueryResponse> callback) {

        svr.queryRecord(userId+"111", new Requests.QueryRequest(deviceGuid, size),
                new RCRetrofitCallback<Reponses.QueryResponse>(callback) {
                    @Override
                    protected void afterSuccess(Reponses.QueryResponse result) {
                        super.afterSuccess(result);
                        callback.onSuccess(result);
                    }
                });
    }


    // ==========================================================Device Start==========================================================


    static public void getDeviceGroups(long userId,
                                       final Callback<List<DeviceGroupInfo>> callback) {
        svr.getDeviceGroups(new UserRequest(userId),
                new RCRetrofitCallback<GetDeviceGroupsResponse>(
                        callback) {
                    @Override
                    protected void afterSuccess(GetDeviceGroupsResponse result) {
                        callback.onSuccess(result.deviceGroups);
                    }
                });
    }

    static public void addDeviceGroup(long userId, String groupName,
                                      final Callback<Long> callback) {
        svr.addDeviceGroup(new AddDeviceGroupRequest(userId, groupName),
                new RCRetrofitCallback<AddDeviceGroupResponse>(
                        callback) {
                    @Override
                    protected void afterSuccess(AddDeviceGroupResponse result) {
                        callback.onSuccess(result.groupId);
                    }
                });

    }

    static public void deleteDeviceGroup(long userId, long groupId,
                                         final VoidCallback callback) {
        svr.deleteDeviceGroup(new UserGroupRequest(userId, groupId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));

    }

    static public void updateDeviceGroupName(long userId, long groupId,
                                             String groupName, VoidCallback callback) {
        svr.updateDeviceGroupName(new UpdateGroupNameRequest(userId, groupId,
                groupName), new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void addDeviceToGroup(long userId, long groupId, String guid,
                                        VoidCallback callback) {
        svr.addDeviceToGroup(new UserGroupGuidRequest(userId, groupId, guid),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void deleteDeviceFromGroup(long userId, long groupId,
                                             String guid, VoidCallback callback) {
        svr.deleteDeviceFromGroup(new UserGroupGuidRequest(userId, groupId,
                guid), new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void clearDeviceByGroup(long userId, long groupId,
                                          VoidCallback callback) {
        svr.clearDeviceByGroup(new UserGroupRequest(userId, groupId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    // ----------------------------------------------------------------

    static public void getDevices(long userId,
                                  final Callback<List<DeviceInfo>> callback) {

        svr.getDevices(new UserRequest(userId),
                new RCRetrofitCallback<GetDevicesResponse>(callback) {
                    @Override
                    protected void afterSuccess(GetDevicesResponse result) {
                        if (Plat.DEBUG) {
                            callback.onSuccess(result.devices);
                        }
                    }

                });

    }

    static public void getDeviceById(String guid, final Callback<DeviceInfo> callback) {
        svr.getDeviceById(new GuidRequest(guid),
                new RCRetrofitCallback<GetDevicePesponse>(callback) {
                    @Override
                    protected void afterSuccess(GetDevicePesponse result) {
                        callback.onSuccess(result.device);
                    }
                });
    }

    static public void getDeviceBySn(String sn, final Callback<DeviceInfo> callback) {
        svr.getDeviceBySn(new GetDeviceBySnRequest(sn),
                new RCRetrofitCallback<GetDevicePesponse>(callback) {
                    @Override
                    protected void afterSuccess(GetDevicePesponse result) {
                        callback.onSuccess(result.device);
                    }
                });

    }

    static public void updateDeviceName(long userId, String guid, String name,
                                        VoidCallback callback) {
        svr.updateDeviceName(new UpdateDeviceNameRequest(userId, guid, name),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void bindDevice(long userId, String guid, String name,
                                  boolean isOwner, VoidCallback callback) {
        svr.bindDevice(new BindDeviceRequest(userId, guid, name, isOwner),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void unbindDevice(long userId, String guid,
                                    VoidCallback callback) {
        svr.unbindDevice(new UnbindDeviceRequest(userId, guid),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getSnForDevice(long userId, String guid,
                                      final Callback<String> callback) {
        svr.getSnForDevice(new UserGuidRequest(userId, guid),
                new RCRetrofitCallback<GetSnForDeviceResponse>(
                        callback) {
                    @Override
                    protected void afterSuccess(GetSnForDeviceResponse result) {
                        callback.onSuccess(result.sn);
                    }
                });
    }

    static public void getDeviceUsers(long userId, String guid,
                                      final Callback<List<User>> callback) {
        svr.getDeviceUsers(new UserGuidRequest(userId, guid),
                new RCRetrofitCallback<GetDeviceUsersResponse>(
                        callback) {
                    @Override
                    protected void afterSuccess(GetDeviceUsersResponse result) {
                        callback.onSuccess(result.users);
                    }
                });
    }


    static public void deleteDeviceUsers(long userId, String guid,
                                         List<Long> userIds, VoidCallback callback) {
        svr.deleteDeviceUsers(new DeleteDeviceUsersRequest(userId, guid,
                userIds), new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getDeviceByParams(Long userId, String deviceType, String deviceCategory, final Callback callback) {
        svr.getDeviceByParams(new Requests.DeviceByParamsRequest(userId, deviceType, deviceCategory),
                new RCRetrofitCallback<Reponses.DeviceResponse>(callback) {

                    @Override
                    protected void afterSuccess(Reponses.DeviceResponse result) {
                        if (result != null) {
                            callback.onSuccess(result);
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        LogUtils.i("20180403", "e:" + e);
                    }
                });
    }


    static public void getAllDeviceType(final Callback callback) {
        svr.getAllDeviceType(new Requests.DeviceTypeRequest(),
                new RCRetrofitCallback<Reponses.DeviceTypeResponse>(callback) {

                    @Override
                    protected void afterSuccess(Reponses.DeviceTypeResponse result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        LogUtils.i("20180612", "e:" + e);
                    }
                });
    }

    static public void getAllDeviceErrorInfo(final Callback callback) {
        svr.getAllDeviceErrorInfo(new RCRetrofitCallback<Reponses.ErrorInfoResponse>(callback) {

            @Override
            protected void afterSuccess(Reponses.ErrorInfoResponse result) {
                callback.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError e) {
                LogUtils.i("20180612", "e:" + e);
            }
        });
    }

    //设备联动查询
    static public void DeviceReactQuery(String deviceGuid, final Callback callback) {
        svr.getQueryDeviceReact(deviceGuid, new RCRetrofitCallback<Reponses.QueryDeviceReact>(callback) {
            @Override
            protected void afterSuccess(Reponses.QueryDeviceReact result) {
                callback.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError e) {
                LogUtils.i("20190906",e.getMessage());

            }


        });
    }
    //设置设备联动
    static public void setLinkage(String deviceGuid, Map<String,Payload> payloadMap, final Callback callback) {
        svr.setDeviceLinkage(deviceGuid, payloadMap, new RCRetrofitCallback<Reponses.SetDeviceLinkage>(callback) {
            @Override
            protected void afterSuccess(Reponses.SetDeviceLinkage result) {
                callback.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError e) {
                super.failure(e);
            }
        });
    }

    //设置全程联动
    static public void setAllDeviceLinkage(String deviceGuid,boolean enable,final Callback callback){
        svr.setAllDeviceReact(new Requests.DeviceLinkage(deviceGuid,enable),
                new RCRetrofitCallback<Reponses.SetDeviceLinkage>(callback){
                    @Override
                    protected void afterSuccess(Reponses.SetDeviceLinkage result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
                    }
                });

    }
    //设置开门联动
    static public void setOpenLinkage(String DeviceGuid,boolean enable,int delaytime,final Callback callback){
        svr.setOpenDeviceReact(new Requests.OpenDeviceReact(DeviceGuid,enable,delaytime),
                new RCRetrofitCallback<Reponses.OpenDeviceResponse>(callback){
                    @Override
                    protected void afterSuccess(Reponses.OpenDeviceResponse result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
                    }
                });

    }

    //-----------------------------------洗碗机-------------------------------------------

    //获取品类下的配件列表
    static public void getConsumablesList(String dt, String dc,String name, final Callback callback) {
        svr.getConsumablesListInfo(new DeviceByParamsConsumables(dt, dc,name),
                new RCRetrofitCallback<Reponses.ConsumablesResponse>(callback) {
                    @Override
                    protected void afterSuccess(Reponses.ConsumablesResponse result) {
                        callback.onSuccess(result);
                    }
                }

        );
    }

    //获取配件详情页跳转参数
    static public void getPurchaseUrl(String userId, long accessoryId, final Callback callback) {
        svr.getPurchaseUrl(new GetPurchaseUrlRequest(userId, accessoryId),
                new RCRetrofitCallback<Reponses.PurchaseUrlResponse>(callback) {

                    @Override
                    protected void afterSuccess(Reponses.PurchaseUrlResponse result) {
                        callback.onSuccess(result);
                    }


                }
        );
    }

    //获取微商城个人订单页跳转参数
    static public void getOrderUrl(long userId, final Callback callback) {
        svr.getOrderUrl(new Requests.UserRequest(userId),
                new RCRetrofitCallback<Reponses.PurchaseUrlResponse>(callback) {

                    @Override
                    protected void afterSuccess(Reponses.PurchaseUrlResponse result) {
                        callback.onSuccess(result);
                    }
                }
        );
    }

    //获取洗碗机历史清洗数据
    static public void getHistroyData(String deviceGuid, final Callback callback) {
        svr.getHistoryData(deviceGuid, new RCRetrofitCallback<Reponses.GetHistoryDataResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.GetHistoryDataResponse result) {
                callback.onSuccess(result);
            }
        });
    }


}
