package com.legent.plat.io.cloud;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.plat.pojos.AppVersionInfo;
import com.legent.plat.pojos.ChatMsg;
import com.legent.plat.pojos.PayloadDishWasher;
import com.legent.plat.pojos.QureyData;
import com.legent.plat.pojos.RCReponse;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.ConsumablesList;
import com.legent.plat.pojos.device.DeviceGroupInfo;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.device.FunctionMore;
import com.legent.plat.pojos.device.FunctionTop3;
import com.legent.plat.pojos.device.ModelMap;
import com.legent.plat.pojos.device.Payload;
import com.legent.plat.pojos.device.PayloadBean;
import com.legent.plat.pojos.device.Payload;
import com.legent.plat.pojos.device.Payload;
import com.legent.pojos.AbsPojo;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import retrofit.http.Body;
import retrofit.http.PUT;

/**
 * Created by sylar on 15/7/23.
 */
public interface Reponses {

    // ==========================================================Common Start==========================================================


    class GetAppIdReponse extends RCReponse {

        @JsonProperty("appGuid")
        public String appGuid;

    }

    class CheckAppVerReponse extends RCReponse {

        @JsonProperty("ver")
        public AppVersionInfo verInfo;

    }


    class GetStartImagesResponse extends RCReponse {

        @JsonProperty("images")
        public List<String> images;

    }

    class GetReportResponse extends  RCReponse{
        @JsonProperty("msg")
        public String msg;
    }

    class GetLookUpResponse extends RCReponse{
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("functionsTop3")
        public List<FunctionTop3> functionTop3s;

        @JsonProperty("functionsMore")
        public List<FunctionMore> functionMores;
    }

    class GetCheckResponse extends RCReponse{
        @JsonProperty("msg")
        public String msg;
        @JsonProperty("last")
        public boolean isLast;

    }

    class SubmitResponse extends RCReponse{
        @JsonProperty("msg")
        public String msg;
    }

    class QueryResponse extends RCReponse{
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("payload")
        public List<QureyData> payload;
    }

    class ChatGetReponse extends RCReponse {

        @JsonProperty("msgs")
        public List<ChatMsg> msgList;

    }

    class ChatSendReponse extends RCReponse {

        @JsonProperty("id")
        public long id;

        @JsonProperty("time")
        public long time;

    }


    class ChatisExistResponse extends RCReponse {

        @JsonProperty("existed")
        public boolean existed;

    }
    // ==========================================================Common End==========================================================
    // ==========================================================User Start==========================================================

    class IsExistedResponse extends RCReponse {

        @JsonProperty("existed")
        public boolean existed;
    }

    class LoginReponse extends RCReponse {

        @JsonProperty("tgt")
        public String tgt;

        @JsonProperty("user")
        public User user;
    }


    class OtherLoginResponse extends RCReponse{
        @JsonProperty("user")
        public User user;
    }

    class PhoneBindResponse extends RCReponse{
        @JsonProperty("user")
        public User user;
    }


    class GetUserReponse extends RCReponse {

        @JsonProperty("user")
         public User user;
    }

    class UpdateFigureReponse extends RCReponse {

        @JsonProperty("figureUrl")
        public String figureUrl;
    }

    class GetVerifyCodeReponse extends RCReponse {

        @JsonProperty("verifyCode")
        public String verifyCode;

    }

    class GetDynamicPwdRequestReponse extends RCReponse {
        @JsonProperty
        public String dynamicPwd;
    }


    // ==========================================================User End==========================================================
    // ==========================================================Device Start==========================================================

    class AddDeviceGroupResponse extends RCReponse {

        @JsonProperty("groupId")
        public long groupId;
    }

    class GetDeviceGroupsResponse extends RCReponse {

        @JsonProperty("deviceGroups")
        public List<DeviceGroupInfo> deviceGroups;
    }

    class GetDevicesResponse extends RCReponse {

        @JsonProperty("devices")
        public List<DeviceInfo> devices;
    }

    class GetDevicePesponse extends RCReponse {

        @JsonProperty("device")
        public DeviceInfo device;
    }

    class GetDeviceUsersResponse extends RCReponse {

        @JsonProperty("users")
        public List<User> users;

    }

    class GetSnForDeviceResponse extends RCReponse {

        @JsonProperty("sn")
        public String sn;

    }

    class DeviceResponse extends RCReponse{

        @JsonProperty("msg")
        public String msg;

        @JsonProperty("id")
        public long id;

        @JsonProperty("deviceType")
        public String deviceType;

        @JsonProperty("deviceCategory")
        public String deviceCategory;

        @JsonProperty("createdTime")
        public long createdTime;

        @JsonProperty("viewBackgroundImg")
        public String viewBackgroundImg;

        @JsonProperty("viewName")
        public String viewName;

        @JsonProperty("version")
        public String version;

        @JsonProperty("title")
        public String title;

        @JsonProperty("text")
        public String text;

        @JsonProperty("modelMap")
        public ModelMap modelMap;

    }

    class DeviceTypeResponse extends RCReponse{

        @JsonProperty("msg")
        public String msg;

        @JsonProperty("map")
        public Map<String, DeviceInfo> map;

        class DeviceInfo {
            String dp;
            String dc;
        }
    }

    class ErrorInfoResponse extends RCReponse{

        @JsonProperty("msg")
        public String msg;

        @JsonProperty("url")
        public String url;
    }

    class ConsumablesResponse extends RCReponse {
        @JsonProperty("msg")
        public String msg;
        @JsonProperty("list")
        public List<ConsumablesList> list;

    }
    class QueryDeviceReact extends RCReponse{
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("payload")
        public Map<String,Payload> payloadMap;


    }

    class SetDeviceLinkage extends RCReponse{
        @JsonProperty("msg")
        public String msg;


        @JsonProperty("payload")
        public String payload;
    }



    class PurchaseUrlResponse extends RCReponse {
        @JsonProperty("msg")
        public String msg;
        @JsonProperty("payload")
        public PayloadDishWasher payload;

    }

    class GetHistoryDataResponse extends RCReponse{
        @JsonProperty("msg")
        public String msg;
        @JsonProperty("payload")
        public PayloadBean payload;



    }

    class UnregisterResponse extends RCReponse{
        @JsonProperty("msg")
        public String msg;
        @JsonProperty("rc")
        public int payload;

    }

    class OpenDeviceResponse extends RCReponse{
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("payload")
        public String payload;


    }
    //--------------------3.6-----------------

    class BaseLoginReponse extends RCReponse{

        @JsonProperty("code")
        public int code;
        @JsonProperty("msg")
        public String msg;
        @JsonProperty("timestamp")
        public long timestamp;

    }
    /**
     * 第三方登录code 获取openId和token
     */
    class GetThirdAccessTokenReponse extends BaseLoginReponse {
        @JsonProperty("data")
        public ThirdAccessToken data;

        public class ThirdAccessToken extends AbsPojo {
            @JsonProperty("accessToken")
            public String accessToken;
            @JsonProperty("openId")
            public String openId;
            @JsonProperty("retCode")
            public int retCode;
            @JsonProperty("msg")
            public String msg;
            @JsonProperty("appSource")
            public String appSource;
            @JsonProperty("expiresIn")
            public String expiresIn;
            @JsonProperty("refreshToken")
            public String refreshToken;
            @JsonProperty("scope")
            public String scope;
            @JsonProperty("unionId")
            public String unionId;
            @JsonProperty("sessionKey")
            public String sessionKey;
        }

    }
    /**
     * 获取手机号
     */
    class GetPhoneNumReponse extends BaseLoginReponse {

        @JsonProperty("data")
        public String data;

    }
    /**
     * 获取token
     */
    class TokenReponse extends BaseLoginReponse {

        @JsonProperty("access_token")
        public String access_token;
        @JsonProperty("token_type")
        public String token_type;
        @JsonProperty("refresh_token")
        public String refresh_token;
        @JsonProperty("expires_in")
        public String expires_in;
        @JsonProperty("scope")
        public String scope;
    }
    /**
     * 是否第一次登录
     */
    class IsFirstLoginResponse extends BaseLoginReponse {

        @JsonProperty("data")
        public boolean data;

    }
    // ==========================================================Device End==========================================================

}
