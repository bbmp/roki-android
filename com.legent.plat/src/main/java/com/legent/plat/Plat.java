package com.legent.plat;

import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.legent.Callback2;
import com.legent.ContextIniter;
import com.legent.VoidCallback2;
import com.legent.io.channels.IChannel;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.device.DeviceCommander;
import com.legent.plat.io.device.IAppMsgMarshaller;
import com.legent.plat.io.device.IAppMsgSyncDecider;
import com.legent.plat.io.device.IAppNoticeReceiver;
import com.legent.plat.io.device.mqtt.MqttChannel;
import com.legent.plat.pojos.device.DeviceGuid;
import com.legent.plat.pojos.device.IDeviceFactory;
import com.legent.plat.pojos.dictionary.PlatDic;
import com.legent.plat.pojos.dictionary.ServerOpt;
import com.legent.plat.services.AccountService;
import com.legent.plat.services.CommonService;
import com.legent.plat.services.DeviceService;
import com.legent.plat.services.account.IAppOAuthService;
import com.legent.services.RestfulService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.AppUtils;

import java.util.IllegalFormatCodePointException;
import java.util.Locale;

public class Plat {

    static public boolean LOG_FILE_ENABLE = false;
    private static String packageName = "com.robam.roki";
    static public boolean DEBUG = true;
    static public Application app;
    static public boolean ifAppVersionOld = false;//APP版本是否升级

    static public Context activtiyContext;

    static public String appType;
    static public String appGuid;
    static public DeviceGuid fanGuid;//pad 默认烟机guid
    static private String stoveGuid;//pad 默认灶具guid
    static private String potGuid;//pad 默认温控锅guid
    static public boolean GlobalOnSwitching;//全局切换 32指令上报时候置true 处理完置false


    //    static public IChannel channel;
    static public IDeviceFactory deviceFactory;
    static public IAppMsgMarshaller appMsgMarshaller;
    static public IAppMsgSyncDecider appMsgSyncDecider;
    static public IAppNoticeReceiver appNoticeReceiver;
    static public IAppOAuthService appOAuthService;


    //-------------------------------------------------------------------------------------
    static public CommonService commonService = CommonService.getInstance();
    static public AccountService accountService = AccountService.getInstance();
    static public DeviceService deviceService = DeviceService.getInstance();
    //    static public DeviceCommander commander = DeviceCommander.getInstance();
    static public ServerOpt serverOpt = new ServerOpt();

    static public DeviceCommander dcMqtt, dcSerial;
    //-------------------------------------------------------------------------------------

    static public String language = Locale.getDefault().getLanguage();

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    static public boolean isValidAppGuid() {
        return (!Strings.isNullOrEmpty(Plat.appGuid)
                && !Objects.equal(Plat.appGuid, DeviceGuid.ZeroGuid));
    }


    static public <T> T getCustomApi(Class<T> apiClazz) {
        return CloudHelper.getRestfulApi(apiClazz);
    }


//    static public void init(Application app, int appDicResid, VoidCallback2 callback) {
//        Plat.app = app;
//        ContextIniter.init(app);
//
//        AppDic dic = AppDic.load(app, appDicResid);
//        Plat.appType = dic.commOpt.appType;
//        Plat.serverOpt = dic.serverOpt;
//
//        Plat.channel = dic.getAppChannel();
//        Plat.deviceFactory = dic.getDeviceFactory();
//        Plat.appMsgMarshaller = dic.getAppMsgMarshaller();
//        Plat.appMsgSyncDecider = dic.getAppMsgSyncDecider();
//        Plat.appNoticeReceiver = dic.getAppNoticeReceiver();
//
//        init(callback);
//    }


    // for mobile
    static public void init(Application app, String appType,
                            IDeviceFactory deviceFactory,
                            IAppMsgMarshaller deviceMsgMarshaller,
                            IAppMsgSyncDecider syncDecider,
                            IAppNoticeReceiver noticeReceiver,
                            VoidCallback2 callback) {

        init(app, appType, deviceFactory, deviceMsgMarshaller, syncDecider, noticeReceiver,
                MqttChannel.getInstance(), null, callback, null);
    }

//    static public void init(Application app,
//                            String appType,
//                            IDeviceFactory deviceFactory,
//                            IAppMsgMarshaller msgMarshaller,
//                            IAppMsgSyncDecider syncDecider,
//                            IAppNoticeReceiver noticeReceiver,
//                            IChannel channel,
//                            VoidCallback2 callback) {
//
//        // -------------------------------------------------------------------------------
//        Plat.app = app;
//        Plat.appType = appType;
//        ContextIniter.init(app);
//        PlatDic.loadPlatDic(app);
//
//        Plat.deviceFactory = deviceFactory;
//        Plat.appMsgMarshaller = msgMarshaller;
//        Plat.appMsgSyncDecider = syncDecider;
//        Plat.appNoticeReceiver = noticeReceiver;
//        Plat.channel = channel != null ? channel : MqttChannel.getInstance();
//
//        init(callback);
//    }

    // for pad
    static public void init(Application app,
                            String appType,
                            IDeviceFactory deviceFactory,
                            IAppMsgMarshaller msgMarshaller,
                            IAppMsgSyncDecider syncDecider,
                            IAppNoticeReceiver noticeReceiver,
                            IChannel chMqtt,
                            IChannel chSerial,
                            VoidCallback2 callback, VoidCallback2 callback2) {
        Plat.app = app;
        Plat.appType = appType;
        ContextIniter.init(app, Plat.appType);
        PlatDic.loadPlatDic(app);

        Plat.deviceFactory = deviceFactory;
        Plat.appMsgMarshaller = msgMarshaller;
        Plat.appMsgSyncDecider = syncDecider;
        Plat.appNoticeReceiver = noticeReceiver;
        int pid = Process.myPid();
        String name = AppUtils.getAppProcessName(app, pid);
        if (packageName.equals(name)) {
            dcMqtt = new DeviceCommander(chMqtt);
        }
        if (chSerial != null) {
            dcSerial = new DeviceCommander(chSerial);
            dcSerial.init(app);
            callback2.onCompleted();
        }
        init(callback);
    }

    static private void init(final VoidCallback2 callback) {
        RestfulService.getInstance().setDefaultHost(
                serverOpt.getRestfulBaseUrl());
        commonService.getAppGuid(new Callback2<String>() {
            @Override
            public void onCompleted(String guid) {
                Plat.appGuid = guid;
                LogUtils.i("2020061004","guid:::"+guid);
                if (dcMqtt != null) {
                    dcMqtt.init(app);
                }
                commonService.init(app);
                accountService.init(app);
                deviceService.init(app);

                if (callback != null) {
                    callback.onCompleted();
                }
            }
        });
    }


    public static void setAppType(String appType) {
        Plat.appType = appType;
    }

    public static DeviceGuid getFanGuid() {
        return fanGuid;
    }

    public static void setFanGuid(DeviceGuid fanGuid) {
        Plat.fanGuid = fanGuid;
    }

    public static String getStoveGuid() {
        return stoveGuid;
    }

    public static void setStoveGuid(String stoveGuid) {
        Plat.stoveGuid = stoveGuid;
    }

    public static String getPotGuid() {
        return potGuid;
    }

    public static void setPotGuid(String potGuid) {
        Plat.potGuid = potGuid;
    }
}
