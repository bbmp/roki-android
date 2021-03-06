package com.legent.plat.services;

import android.content.Context;
import android.content.pm.PackageManager;

import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.Callback2;
import com.legent.plat.Plat;
import com.legent.plat.events.AppGuidGettedEvent;
import com.legent.plat.events.DeviceTokenEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.plat.pojos.AppVersionInfo;
import com.legent.plat.pojos.RCReponse;
import com.legent.plat.pojos.device.DeviceGuid;
import com.legent.services.AbsService;
import com.legent.services.CrashLogService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ApiUtils;
import com.legent.utils.api.AppUtils;
import com.legent.utils.api.PreferenceUtils;

import static com.legent.ContextIniter.context;
import static com.legent.plat.Plat.appGuid;
import static com.legent.plat.Plat.appType;

import retrofit2.Call;
import retrofit2.Response;

public class CommonService extends AbsService {

    final static String APP_GUID = "AppGuid";
    final static int LOG_CRASH = 0;
    private static CommonService instance = new CommonService();

    synchronized public static CommonService getInstance() {
        return instance;
    }

    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);
        CrashLogService.getInstance().setOnCrashedListener(crashedListener);
    }


    // -------------------------------------------------------------------------------
    // onEvent
    // -------------------------------------------------------------------------------

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        CloudHelper.bindAppGuidAndUser(appGuid, event.pojo.id, null);
    }

    @Subscribe
    public void onEvent(UserLogoutEvent event) {
        CloudHelper.unbindAppGuidAndUser(appGuid, event.pojo.id, null);
    }


    @Subscribe
    public void onEvent(DeviceTokenEvent event) {
        getAppGuidWithPhone(new Callback2<String>() {
            @Override
            public void onCompleted(String s) {
                Plat.appGuid = s;
                //????????????appId??????, ????????????
                if (Plat.isValidAppGuid()) {
                    postEvent(new AppGuidGettedEvent(appGuid));
                    if (Plat.accountService.isLogon()) {
                        LogUtils.i("2020032009","appGuid:::"+appGuid);
                        CloudHelper.bindAppGuidAndUser(appGuid, Plat.accountService.getCurrentUserId(), null);
                    }
                }
            }
        }, event.deviceToken);
    }

    private String phone;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    // -------------
    // --------------------------------------------------------------
    // public
    // --------------------------------------------------------------

    private void getAppGuidWithPhone(final Callback2<String> callback, String phone) {
        try {
            String packageName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
            String token = ApiUtils.getNewClientId(Plat.app);
            CloudHelper.getAppGuid(Plat.appType, token, phone, versionName, Reponses.GetAppIdReponse.class, new RetrofitCallback<Reponses.GetAppIdReponse>() {
                @Override
                public void onSuccess(Reponses.GetAppIdReponse getAppIdReponse) {
                    if (null != getAppIdReponse) {
                        String guid = getAppIdReponse.appGuid;
                        LogUtils.i("20171219", "success:" + guid);
                        setAppId(guid);
                        callback.onCompleted(guid);
                    }
                }

                @Override
                public void onFaild(String err) {
                    callback.onCompleted(DeviceGuid.ZeroGuid);
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getAppGuid(final Callback2<String> callback) {

        try {
            String packageName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
            String guid = getAppId();
            if (Plat.DEBUG) {
                LogUtils.i("20170926", "commonguid:" + guid);
            }
            if (!Strings.isNullOrEmpty(guid)) {
                callback.onCompleted(guid);
                return;
            }
           // String token = ApiUtils.getClientId(Plat.app);
            String token = ApiUtils.getNewClientId(Plat.app);
            LogUtils.i("20181119","appTYtpe::"+appType+"token::"+token+"phone::"+phone+"versonName:"+versionName);
            CloudHelper.getAppGuid(appType, token, phone, versionName, Reponses.GetAppIdReponse.class, new RetrofitCallback<Reponses.GetAppIdReponse>() {
                @Override
                public void onSuccess(Reponses.GetAppIdReponse getAppIdReponse) {
                    if (null != getAppIdReponse) {
                        String guid = getAppIdReponse.appGuid;
                        LogUtils.i("20170926", "success:" + guid);
                        setAppId(guid);
                        callback.onCompleted(guid);
                    }
                }

                @Override
                public void onFaild(String err) {
                    LogUtils.i("20170926", "onFailure:" + err);
                    callback.onCompleted(DeviceGuid.ZeroGuid);
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void checkAppVersion(Callback<AppVersionInfo> callback) {
        CloudHelper.checkAppVersion(appType, callback);
    }

    // -------------------------------------------------------------------------------
    // override
    // -------------------------------------------------------------------------------

    @Override
    protected void onConnected(boolean isWifi) {
        LogUtils.i("20170926", "phonetoken:---:" + phone);

        //??????????????????appId?????????????????????
        if (!Plat.isValidAppGuid()) {
            getAppGuid(new Callback2<String>() {
                @Override
                public void onCompleted(String guid) {
                    LogUtils.i("20170926", "guid::" + guid);
                    Plat.appGuid = guid;
                    //????????????appId??????, ????????????
                    if (Plat.isValidAppGuid()) {
                        postEvent(new AppGuidGettedEvent(appGuid));
                        if (Plat.accountService.isLogon()) {
                            CloudHelper.bindAppGuidAndUser(appGuid, Plat.accountService.getCurrentUserId(), null);
                        }
                    }
                }
            });
        }
    }

    // -------------------------------------------------------------------------------
    // other
    // -------------------------------------------------------------------------------

    private CrashLogService.OnCrashedListener crashedListener = new CrashLogService.OnCrashedListener() {

        @Override
        public void onCrashed(String log) {
            if (!AppUtils.isDebug(cx)) {
                CloudHelper.reportLog(appGuid, LOG_CRASH, log, null);
            }
            if (commonCrashListener != null)
                commonCrashListener.onCrashed();
        }
    };

    public void setCommonCrashListener(OnCommonCrashListener commonCrashListener) {
        this.commonCrashListener = commonCrashListener;
    }

    private OnCommonCrashListener commonCrashListener;

    public interface OnCommonCrashListener {
        void onCrashed();
    }

    private void setAppId(String appId) {
        if (Strings.isNullOrEmpty(appId))
            PreferenceUtils.remove(APP_GUID);
        else
            PreferenceUtils.setString(APP_GUID, appId);
    }

    public String getAppId() {
        return PreferenceUtils.getString(APP_GUID, null);
    }

}
