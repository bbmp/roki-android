package com.legent.plat.io.cloud;

import com.legent.plat.io.RCRetrofitCallback;
import com.legent.plat.io.cloud.Reponses.AddDeviceGroupResponse;
import com.legent.plat.io.cloud.Reponses.ChatGetReponse;
import com.legent.plat.io.cloud.Reponses.ChatSendReponse;
import com.legent.plat.io.cloud.Reponses.ChatisExistResponse;
import com.legent.plat.io.cloud.Reponses.CheckAppVerReponse;
import com.legent.plat.io.cloud.Reponses.DeviceResponse;
import com.legent.plat.io.cloud.Reponses.ConsumablesResponse;
import com.legent.plat.io.cloud.Reponses.DeviceTypeResponse;
import com.legent.plat.io.cloud.Reponses.ErrorInfoResponse;
import com.legent.plat.io.cloud.Reponses.SetDeviceLinkage;
import com.legent.plat.io.cloud.Reponses.OpenDeviceResponse;
import com.legent.plat.io.cloud.Reponses.GetAppIdReponse;
import com.legent.plat.io.cloud.Reponses.GetCheckResponse;
import com.legent.plat.io.cloud.Reponses.GetDeviceGroupsResponse;
import com.legent.plat.io.cloud.Reponses.GetDevicePesponse;
import com.legent.plat.io.cloud.Reponses.GetDeviceUsersResponse;
import com.legent.plat.io.cloud.Reponses.GetDevicesResponse;
import com.legent.plat.io.cloud.Reponses.GetDynamicPwdRequestReponse;
import com.legent.plat.io.cloud.Reponses.GetLookUpResponse;
import com.legent.plat.io.cloud.Reponses.GetReportResponse;
import com.legent.plat.io.cloud.Reponses.GetSnForDeviceResponse;
import com.legent.plat.io.cloud.Reponses.GetStartImagesResponse;
import com.legent.plat.io.cloud.Reponses.GetUserReponse;
import com.legent.plat.io.cloud.Reponses.GetVerifyCodeReponse;
import com.legent.plat.io.cloud.Reponses.IsExistedResponse;
import com.legent.plat.io.cloud.Reponses.LoginReponse;
import com.legent.plat.io.cloud.Reponses.OtherLoginResponse;
import com.legent.plat.io.cloud.Reponses.PhoneBindResponse;
import com.legent.plat.io.cloud.Reponses.QueryDeviceReact;
import com.legent.plat.io.cloud.Reponses.UnregisterResponse;
import com.legent.plat.io.cloud.Reponses.QueryResponse;
import com.legent.plat.io.cloud.Reponses.SubmitResponse;
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
import com.legent.plat.io.cloud.Requests.CheckRequest;
import com.legent.plat.io.cloud.Requests.DeleteDeviceUsersRequest;
import com.legent.plat.io.cloud.Requests.DeviceByParamsRequest;
import com.legent.plat.io.cloud.Requests.DeviceTypeRequest;
import com.legent.plat.io.cloud.Requests.DeviceByParamsConsumables;
import com.legent.plat.io.cloud.Requests.GetPurchaseUrlRequest;
import com.legent.plat.io.cloud.Requests.DeviceLinkage;
import com.legent.plat.io.cloud.Requests.OpenDeviceReact;
import com.legent.plat.io.cloud.Requests.ExpressLoginRequest;
import com.legent.plat.io.cloud.Requests.GetAppIdRequest;
import com.legent.plat.io.cloud.Requests.GetDeviceBySnRequest;
import com.legent.plat.io.cloud.Requests.GetVerifyCodeRequest;
import com.legent.plat.io.cloud.Requests.GuidRequest;
import com.legent.plat.io.cloud.Requests.LoginFrom3rdRequest;
import com.legent.plat.io.cloud.Requests.LoginRequest;
import com.legent.plat.io.cloud.Requests.LogoutRequest;
import com.legent.plat.io.cloud.Requests.LookUpCodeRequest;
import com.legent.plat.io.cloud.Requests.OtherLoginRequest;
import com.legent.plat.io.cloud.Requests.PhoneBindRequest;
import com.legent.plat.io.cloud.Requests.QueryRequest;
import com.legent.plat.io.cloud.Requests.RegistByEmailRequest;
import com.legent.plat.io.cloud.Requests.RegistByPhoneRequest;
import com.legent.plat.io.cloud.Requests.ReportCodeRequest;
import com.legent.plat.io.cloud.Requests.ReportLogRequest;
import com.legent.plat.io.cloud.Requests.ResetPwdByEmailRequest;
import com.legent.plat.io.cloud.Requests.ResetPwdByPhoneRequest;
import com.legent.plat.io.cloud.Requests.SubmitRequest;
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
import com.legent.plat.pojos.RCReponse;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.Payload;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit2.Call;
import retrofit2.http.Headers;

/**
 * Created by sylar on 15/7/23.
 */
public interface ICloudService<T extends RCReponse> {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
            Locale.getDefault());

    // ==========================================================Url Start==========================================================
    String getAppId = "/rest/dms/api/app/guid/get";
    String bindAppGuidAndUser = "/rest/api/app/user/bind";
    String unbindAppGuidAndUser = "/rest/api/app/user/unbind";
    String checkAppVersion = "/rest/dms/api/app/version/check";
    String reportLog = "/rest/dms/api/app/log/report";
    String getStartImages = "/rest/api/app/start-image/get";
    String sendChatMsg = "/rest/api/chat/send";
    String getChatBefore = "/rest/api/chat/before/get";
    String getChatAfter = "/rest/api/chat/after/get";
    String isExistChatMsg = "/rest/api/chat/after/is-exist";
    //-----------------------------------------------------------------------------------------------------------------------------
    String isExisted = "/rest/ums/api/account/is-existed";
    String registByPhone = "/rest/api/cas/account/regist-by-phone";
    String registByEmail = "/rest/api/cas/account/regist-by-email";
    String login = "/rest/api/cas/app/login";
    String otherLogin = "/rest/gateway/api/auth/third/app/login";//第三方登录接口
    String phoneBind = "/rest/gateway/api/auth/third/app/phone/bind";
    String logout = "/rest/api/cas/app/logout";
    //------------------3.6----------------
    String getPhoneNum  = "/rest/auth/api/auth/getPhoneNum";
    String token = "/rest/auth/api/oauth/token?grant_type=roki" ;
    String isFirstLogin = "/rest/auth/api/auth/isFirstLogin" ;
    //获取用户信息3.6
    String getOauth = "/rest/ums/api/user/get/oauth" ;
    //获取用户信息3.7
    String getUser2 = "/rest/ums/api/user/get" ;
    String setPassword = "/rest/ums/api/user/setPassword";
    String expressLogin = "/rest/api/cas/quick-login";
    String getUser = "/rest/api/cas/user/get";
    String updateUser = "/rest/ums/api/user/update";
    String unregistByPhone = "/rest/ums/api/user/cancel";//注销用户接口
    String bindNewPhone = "/rest/ums/api/bind/new/phone";
    String updatePassword = "/rest/api/cas/user/pwd/update";
    String updateFigure = "/rest/api/cas/user/figure/update";
    String getVerifyCode = "/rest/api/cas/verify-code/get";
    String getDynamicPwd = "/rest/api/cas/dynamic-pwd/get";
    String resetPasswordByPhone = "/rest/api/cas/pwd/reset-by-phone";
    String resetPasswordByEmail = "/rest/api/cas/pwd/reset-by-email";
    String loginFrom3rd = "/rest/api/cas/login3rd";
    String getThirdAccessToken = "/rest/auth/api/auth/getThirdAccessToken";
    String isBindSjhm = "/rest/auth/api/auth/isBindSjhm";
    //-------------------3.7绑定微信 解绑微信-----------------------
    String bind3rd = "/rest/ums/api/bind3rd";
    String unbind3rd = "/rest/ums/api/unbind3rd";
    //-----------------------------------------------------------------------------------------------------------------------------
    String getDeviceGroups = "/rest/dms/api/device-group/get";
    String addDeviceGroup = "/rest/dms/api/device-group/add";
    String deleteDeviceGroup = "/rest/dms/api/device-group/delete";
    String updateDeviceGroupName = "/rest/dms/api/device-group/name/update";
    String addDeviceToGroup = "/rest/dms/api/device-group/device/add";
    String deleteDeviceFromGroup = "/rest/dms/api/device-group/device/delete";
    String clearDeviceByGroup = "/rest/dms/api/device-group/device/clear";
    String getDevices = "/rest/dms/api/device/get";
    String getDeviceById = "/rest/dms/api/device/get-by-guid";
    String getDeviceBySn = "/rest/dms/api/device/get-by-sn";
    String updateDeviceName = "/rest/dms/api/device/name/update";
    String bindDevice = "/rest/dms/api/device/bind";
    String unbindDevice = "/rest/dms/api/device/unbind";
    String getSnForDevice = "/rest/dms/api/device/sn/get";
    String getDeviceUsers = "/rest/dms/api/device/user/get";
    String deleteDeviceUsers = "/rest/dms/api/device/user/delete";
    String getDeviceByParams = "/rest/dms/api/device-configuration/get-by-params/new";


    //String getDeviceByParams = "/rest/dms/api/device-configuration/get-by-params/new";
    String getAllDeviceType = "/rest/dms/api/device-type/getAllDeviceType";
    String getAllDeviceErrorInfo = "/rest/ops/api/error/config/get";
    String getReportCode = "/rest/ops/api/functionAcount/add";
    String getLookUpCode = "/rest/ops/api/functionAcount/get";//查询设备模块化Top3和更多菜谱
    String getStoveUpCode = "/rest/ops/api/deviceFunctionAcount/get";
    String getCookerTop3 = "/rest/ops/api/deviceFunctionAcount/get";
    String getCheck = "/rest/dms/api/device-configuration/version/check";

    String submitRecord = "/rest/dms/api/device/operation/submit";
    String queryRecord = "/rest/dms/api/device/operation/query";





    String getConsumablesList = "/rest/ops/api/wechat/mall/getAccessoriesByParams";
    String getPurchaseUrl = "/rest/ops/api/wechat/mall/purchaseUrl";
    String getOrderUrl = "/rest/ops/api/wechat/mall/orderUrl";
    String getHistroyData = "/rest/api/device/washer/history/data";


    String queryDeviceReact = "/rest/dms/api/linkage/get/{deviceGuid}";//查询设备联动
    String setAllDeviceReact = "/rest/dms/api/linkage/rq035/begin/switch";//设置全程联动
    String setOpenDeviceReact = "/rest/dms/api/linkage/rq035/end/switch";//设置开门联动


    String setFanKinkage = "/rest/dms/api/linkage/set/{deviceGuid}";//设置开门联动 和全程联动统一接口


    // ==========================================================Common Start==========================================================

    @POST(submitRecord)
    void submitRecord(@Header("x_current_user_id") String id, @Body SubmitRequest reqBody, Callback<SubmitResponse> callback);

    @POST(queryRecord)
    void queryRecord(@Header("x_current_user_id") String id, @Body QueryRequest reqBody, Callback<QueryResponse> callback);

    @POST(getCheck)
    void getCheck(@Body CheckRequest reqBody, Callback<GetCheckResponse> callback);

    @POST(getLookUpCode)
    void getLookUpCode(@Body LookUpCodeRequest reqBody, Callback<GetLookUpResponse> callback);

    @POST(getStoveUpCode)
    void getStoveUpCode(@Body LookUpCodeRequest reqBody, Callback<GetLookUpResponse> callback);

    @POST(getCookerTop3)
    void getCookerCode3(@Body LookUpCodeRequest reqBody, Callback<GetLookUpResponse> callback);

    @POST(getReportCode)
    void getReportCode(@Body ReportCodeRequest reqBody, Callback<GetReportResponse> callback);

//    @POST(getAppId)
//    GetAppIdReponse getAppId(@Body GetAppIdRequest reqBody);

    @retrofit2.http.POST(getAppId)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getAppId(@retrofit2.http.Body RequestBody reqBody);

    @POST(bindAppGuidAndUser)
    void bindAppGuidAndUser(@Body AppUserGuidRequest reqBody,
                            Callback<RCReponse> callback);

    @POST(unbindAppGuidAndUser)
    void unbindAppGuidAndUser(@Body AppUserGuidRequest reqBody,
                              Callback<RCReponse> callback);

    @POST(checkAppVersion)
    void checkAppVersion(@Body AppTypeRequest reqBody,
                         Callback<CheckAppVerReponse> callback);

    @POST(reportLog)
    void reportLog(@Body ReportLogRequest reqBody, Callback<RCReponse> callback);

    @POST(getStartImages)
    void getStartImages(@Body AppTypeRequest reqBody,
                        Callback<GetStartImagesResponse> callback);

    @POST(sendChatMsg)
    void sendChatMsg(@Body ChatSendRequest reqBody, Callback<ChatSendReponse> callback);

    @POST(getChatBefore)
    void getChatBefore(@Body ChatGetRequest reqBody,
                       Callback<ChatGetReponse> callback);

    @POST(getChatAfter)
    void getChatAfter(@Body ChatGetRequest reqBody,
                      Callback<ChatGetReponse> callback);

    @POST(isExistChatMsg)
    void isExistChatMsg(@Body ChatisExistRequest reqBody,
                        Callback<ChatisExistResponse> callback);

    // ==========================================================User Start==========================================================

    @POST(isExisted)
    void isExisted(@Body AccountRequest reqBody,
                   Callback<IsExistedResponse> callback);

    @POST(registByPhone)
    void registByPhone(@Body RegistByPhoneRequest reqBody,
                       Callback<RCReponse> callback);

    @POST(registByEmail)
    void registByEmail(@Body RegistByEmailRequest reqBody,
                       Callback<RCReponse> callback);

    @POST(login)
    void login(@Body LoginRequest reqBody, Callback<LoginReponse> callback);




    @POST(otherLogin)
    void otherLogin(@Body OtherLoginRequest reqBody, Callback<OtherLoginResponse> callback);

    @POST(phoneBind)
    void phoneBind(@Body PhoneBindRequest reqBody, Callback<PhoneBindResponse> callback);

    @POST(logout)
    void logout(@Body LogoutRequest reqBody, Callback<RCReponse> callback);

    @POST(expressLogin)
    void expressLogin(@Body ExpressLoginRequest reqBody, Callback<LoginReponse> callback);

    @POST(getUser)
    void getUser(@Body UserRequest reqBody, Callback<GetUserReponse> callback);

    @POST(updateUser)
    void updateUser(@Body UpdateUserRequest reqBody,
                    Callback<RCReponse> callback);

    @POST(bindNewPhone)
    void bindNewPhone(@Body Requests.BindNewPhoneRequest reqBody,
                    Callback<RCReponse> callback);

    @POST(updatePassword)
    void updatePassword(@Body UpdatePasswordRequest reqBody,
                        Callback<RCReponse> callback);

    @POST(updateFigure)
    void updateFigure(@Body UpdateFigureRequest reqBody,
                      Callback<UpdateFigureReponse> callback);

    @retrofit2.http.POST(getVerifyCode)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getVerifyCode(@retrofit2.http.Body RequestBody body);

    @POST(getDynamicPwd)
    void getDynamicPwd(@Body GetVerifyCodeRequest reqBody,
                       Callback<GetDynamicPwdRequestReponse> callback);

    @POST(resetPasswordByPhone)
    void resetPasswordByPhone(@Body ResetPwdByPhoneRequest reqBody,
                              Callback<RCReponse> callback);

    @POST(resetPasswordByEmail)
    void resetPasswordByEmail(@Body ResetPwdByEmailRequest reqBody,
                              Callback<RCReponse> callback);

    @POST(loginFrom3rd)
    void loginFrom3rd(@Body LoginFrom3rdRequest reqBody,
                      Callback<LoginReponse> callback);

    @POST(bind3rd)
    void bind3rd(@Body Bind3rdRequest reqBody, Callback<GetUserReponse> callback);

    @POST(unbind3rd)
    void unbind3rd(@Body Unbind3rdRequest reqBody, Callback<RCReponse> callback);

    // ==========================================================Device Start==========================================================

    @POST(getDeviceGroups)
    void getDeviceGroups(@Body UserRequest reqBody,
                         Callback<GetDeviceGroupsResponse> callback);

    @POST(addDeviceGroup)
    void addDeviceGroup(@Body AddDeviceGroupRequest reqBody,
                        Callback<AddDeviceGroupResponse> callback);

    @POST(deleteDeviceGroup)
    void deleteDeviceGroup(@Body UserGroupRequest reqBody,
                           Callback<RCReponse> callback);

    @POST(updateDeviceGroupName)
    void updateDeviceGroupName(@Body UpdateGroupNameRequest reqBody,
                               Callback<RCReponse> callback);

    @POST(addDeviceToGroup)
    void addDeviceToGroup(@Body UserGroupGuidRequest reqBody,
                          Callback<RCReponse> callback);

    @POST(deleteDeviceFromGroup)
    void deleteDeviceFromGroup(@Body UserGroupGuidRequest reqBody,
                               Callback<RCReponse> callback);

    @POST(clearDeviceByGroup)
    void clearDeviceByGroup(@Body UserGroupRequest reqBody,
                            Callback<RCReponse> callback);

    // ----------------------------------------------------------------

    @POST(getDevices)
    void getDevices(@Body UserRequest reqBody,
                    Callback<GetDevicesResponse> callback);

    @retrofit2.http.POST(getDeviceById)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getDeviceById(@retrofit2.http.Body RequestBody body);

    @POST(getDeviceBySn)
    void getDeviceBySn(@Body GetDeviceBySnRequest reqBody,
                       Callback<GetDevicePesponse> callback);

    @POST(updateDeviceName)
    void updateDeviceName(@Body UpdateDeviceNameRequest reqBody,
                          Callback<RCReponse> callback);

    @POST(bindDevice)
    void bindDevice(@Body BindDeviceRequest reqBody,
                    Callback<RCReponse> callback);

    @POST(unbindDevice)
    void unbindDevice(@Body UnbindDeviceRequest reqBody,
                      Callback<RCReponse> callback);

    @POST(getSnForDevice)
    void getSnForDevice(@Body UserGuidRequest reqBody,
                        Callback<GetSnForDeviceResponse> callback);

    @POST(getDeviceUsers)
    void getDeviceUsers(@Body UserGuidRequest reqBody,
                        Callback<GetDeviceUsersResponse> callback);

    @POST(deleteDeviceUsers)
    void deleteDeviceUsers(@Body DeleteDeviceUsersRequest reqBody,
                           Callback<RCReponse> callback);

    @POST(getDeviceByParams)
    void getDeviceByParams(@Body DeviceByParamsRequest reqBody,
                           Callback<DeviceResponse> callback);

    @retrofit2.http.POST(getAllDeviceType)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getAllDeviceType(@retrofit2.http.Body RequestBody body);

    @retrofit2.http.POST(getAllDeviceErrorInfo)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getAllDeviceErrorInfo(@retrofit2.http.Body RequestBody body);

    @POST(getConsumablesList)
    void getConsumablesListInfo(@Body DeviceByParamsConsumables deviceByParamsConsumables,
                                Callback<ConsumablesResponse> callback);


    @POST(getPurchaseUrl)
    void getPurchaseUrl(@Body GetPurchaseUrlRequest request,
                        Callback<Reponses.PurchaseUrlResponse> callback);

    @POST(getOrderUrl)
    void getOrderUrl(@Body UserRequest request,
                     Callback<Reponses.PurchaseUrlResponse> callback);

    @GET(getHistroyData)
    void getHistoryData(@Query("deviceGuid") String deviceGuid,
                        Callback<Reponses.GetHistoryDataResponse> callback);

    @GET(queryDeviceReact)
    void getQueryDeviceReact(@Path("deviceGuid") String deviceGuid,
                             Callback<QueryDeviceReact> callback);

    @POST(setFanKinkage)
    void setDeviceLinkage(@Path("deviceGuid") String deviceGuid,
                          @Body Map<String, Payload> map,
                          Callback<Reponses.SetDeviceLinkage> callback);
    @POST(setAllDeviceReact)
    void setAllDeviceReact(@Body DeviceLinkage reBody,
                           Callback<SetDeviceLinkage>callback);

    //手机号码注销账号接口
    @GET(unregistByPhone)
    void unregistByPhone(@Query("userId") long userId, @Query("phone") String phone, @Query("verifyCode") String verifyCode,
                         Callback<Reponses.UnregisterResponse> callback);


    @POST(setOpenDeviceReact)
    void setOpenDeviceReact(@Body OpenDeviceReact reBody,
                            Callback<OpenDeviceResponse> callback);

    //--------------------3.6----------------------------

    /**
     * 服务器获取手机号接口
     * @param reqBody
     * @param callback
     */
    @GET(getPhoneNum)
    void getPhoneNum(@Query("token") String reqBody, Callback<Reponses.GetPhoneNumReponse> callback);

    /**
     * 手机一键登录获取token
     * @param loginType
     * @param sjhm
     * @param client_id
     * @param client_secret
     * @param appType
     * @param callback
     */
    @FormUrlEncoded
    @POST(token)
    void token(@Field("loginType") String loginType,@Field("sjhm")String sjhm,@Field("client_id")String client_id,@Field("client_secret")String client_secret,@Field("appType")String appType,
                           Callback<Reponses.TokenReponse>callback);

    /**
     * 获取token
     * @param loginType
     * @param sjhm
     * @param client_id
     * @param client_secret
     * @param appType
     * @param callback
     */
    @retrofit2.http.FormUrlEncoded
    @retrofit2.http.POST(token)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> getToken(@retrofit2.http.Field("loginType") String loginType
            ,@retrofit2.http.Field("sjhm") String sjhm
            ,  @retrofit2.http.Field("smsCode") String smsCode
            , @retrofit2.http.Field("password") String password
            , @retrofit2.http.Field("accessToken") String accessToken
            , @retrofit2.http.Field("refreshToken") String refreshToken
            , @retrofit2.http.Field("openId") String openId
            , @retrofit2.http.Field("client_id") String client_id
            ,@retrofit2.http.Field("client_secret") String client_secret
            ,@retrofit2.http.Field("appType") String appType);

    /**
     * 获取第三方登录的token
     * @param loginType
     * @param code
     * @param callback
     */
    @GET(getThirdAccessToken)
    void getThirdAccessToken(@Query("loginType") String loginType,@Query("code") String code,
                             Callback<Reponses.GetThirdAccessTokenReponse> callback);

    /**
     * 是否绑定手机
     * @param loginType
     * @param accessToken
     * @param callback
     */
    @GET(isBindSjhm)
    void isBindSjhm(@Query("loginType") String loginType,@Query("accessToken") String accessToken,@Query("openId") String openId,
                             Callback<Reponses.IsFirstLoginResponse> callback);
    /**
     * 是否第一次登录
     * @param authorization
     * @param callback
     */
    @GET(isFirstLogin)
    void isFirstLogin(@Header("authorization") String authorization, Callback<Reponses.IsFirstLoginResponse> callback);

    /**
     * 获取用户信息3.6
     * @param authorization
     * @param callback
     */
    @retrofit2.http.GET(getOauth)
    Call<ResponseBody> getOauth(@retrofit2.http.Header("authorization") String authorization);
    /**
     * 获取用户信息3.7
     * @param reqBody
     * @param callback
     */
    @retrofit2.http.POST(getUser2)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getUser2(@retrofit2.http.Body RequestBody body);
    /**
     * 设置新密码
     * @param authorization
     * @param reqBody
     * @param callback
     */
    @GET(setPassword)
    void setPassword(@Header("authorization") String authorization,@Query("password") String reqBody , Callback<RCReponse> callback);

}
