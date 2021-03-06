package com.robam.common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import com.google.common.base.Objects;
import com.legent.plat.Plat;
import com.legent.plat.PlatApp;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.ReflectUtils;
import com.legent.utils.api.ResourcesUtils;
import com.robam.common.pojos.dictionary.AppExtendDic;
import com.robam.common.services.NotifyService;
import com.robam.common.services.StoreService;
import com.robam.common.ui.BleRssiDevice;

import java.util.UUID;

import cn.com.heaton.blelibrary.ble.Ble;
import cn.com.heaton.blelibrary.ble.model.BleFactory;
import cn.com.heaton.blelibrary.ble.utils.UuidUtils;

abstract public class RobamApp extends PlatApp {
    protected static final String SERVICE_HOST = "api.myroki.com";
    protected static final int ecsPort = 80;
    protected static final String MQTT_HOST = "mqtt.myroki.com";
    protected static final int acsHost = 1883;

    protected static final String Test_SERVICE_HOST = "develop.api.myroki.com";
    protected static final int Test_ecsPort = 8081;
    protected static final String Test_MQTT_HOST = "develop.mqtt.myroki.com";
    protected static final int Test_acsHost = 1883;
    //protected static final String SERVICE_HOST = "115.29.246.216";  115.29.246.216:8081  1883
    //static final String SERVICE_HOST = "172.16.14.32";
    //static final String Test_SERVICE_HOST = "172.27.15.13";

    abstract public NotifyService getNotifyService();

    @Override
    protected void init() {
        super.init();
        initPlat();
        if (Objects.equal(this.getPackageName(),
                "com.robam.roki")) {
            AppExtendDic.init(this);
            StoreService.getInstance().init(this);
            NotifyService.getInstance().init(this);
            startUI();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName(this);
            if (!"com.robam.roki".equals(processName)){//?????????????????????????????????
                WebView.setDataDirectorySuffix(processName);
            }
        }
        initBLE();
    }

    private void initBLE(){
        Ble.options()
                .setLogBleEnable(true)//????????????????????????????????????
                .setThrowBleException(true)//??????????????????????????????
                .setLogTAG("AndroidBLE")//??????????????????????????????TAG
                .setAutoConnect(false)//????????????????????????
                .setIgnoreRepeat(false)//????????????????????????????????????(?????????????????????????????????)
                .setConnectFailedRetryCount(3)//?????????????????????????????????????????????,??????????????????
                .setConnectTimeout(10 * 1000)//????????????????????????
                .setScanPeriod(1800 * 1000)//??????????????????
                .setMaxConnectNum(7)//??????????????????
                .setUuidService(UUID.fromString(UuidUtils.uuid16To128("fd00")))//??????????????????uuid
                .setUuidWriteCha(UUID.fromString(UuidUtils.uuid16To128("fed7")))//?????????????????????uuid
                .setUuidReadCha(UUID.fromString(UuidUtils.uuid16To128("fed8")))//?????????????????????uuid ????????????
                .setUuidNotifyCha(UUID.fromString(UuidUtils.uuid16To128("fd03")))//????????????????????????uuid ???????????????????????????????????????????????????uuid???
                .setFactory(new BleFactory<BleRssiDevice>() {//???????????????BleDevice???????????????
                    @Override
                    public BleRssiDevice create(String address, String name) {
                        return new BleRssiDevice(address, name);//?????????BleDevice?????????
                    }
                })
                .create(this, new Ble.InitCallback() {
                    @Override
                    public void success() {
                        LogUtils.i("MainApplication", "???????????????");
                    }

                    @Override
                    public void failed(int failedCode) {
                        LogUtils.i("MainApplication", "??????????????????" + failedCode);
                    }
                });
    }

    public  String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }

    protected void initPlat() {
          Plat.serverOpt.set(SERVICE_HOST, ecsPort, MQTT_HOST, acsHost);
//         Plat.serverOpt.set(Test_SERVICE_HOST, Test_ecsPort, Test_MQTT_HOST, Test_acsHost);

//        if (Plat.dcMqtt != null) {
//            Plat.dcMqtt.setAsyncLogEnable(true);// 1104 zhaiyuanyi
//            Plat.dcMqtt.setSyncLogEnable(true);
//        }
//        if (Plat.dcSerial != null) {
//            Plat.dcSerial.setAsyncLogEnable(true);// 1104 zhaiyuanyi
//            Plat.dcSerial.setSyncLogEnable(true);
//        }
//
//        Plat.LOG_FILE_ENABLE = true;
//        if (Plat.LOG_FILE_ENABLE) {
//            LogUtils.logFIleWithTime("\n\n");
//            LogUtils.logFIleWithTime("=============================================");
//            LogUtils.logFIleWithTime("App ??????");
//            LogUtils.logFIleWithTime("=============================================\n\n");
//        }
    }




    protected void startUI() {
        String uiConfig = ResourcesUtils.raw2String(R.raw.ui);
        UIService.getInstance().loadConfig(uiConfig);
    }





}
