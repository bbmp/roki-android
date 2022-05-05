package com.robam.roki.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.services.DeviceService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.NetworkUtils;
import com.legent.utils.api.PreferenceUtils;
import com.mob.tools.utils.FileUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.ui.helper3.UploadFileHelper;

import java.io.File;

/**
 * 上传日志
 */
public class UploadLogFileUtil {
    public static void uploadLog(Context cx) {
        //网络状态写入文件
        LogUtils.uploadLogFIleWithTime("isLogon:" + Plat.accountService.isLogon());
        String str = NetworkUtils.isConnect(cx) ? "available" : "unavailable";
        LogUtils.uploadLogFIleWithTime("network state:" + str);
        LogUtils.uploadLogFIleWithTime("MQTT state:" + PreferenceUtils.getBool("connect",false));
//        LogUtils.uploadLogFIleWithTime("中文测试");
//        getDeviceState();
        upLoadFile();
    }

    private static String getDeviceState() {
        String deviceState = "";
        for (int i = 0; i < DeviceService.getInstance().queryAll().size(); i++) {
            deviceState = DeviceService.getInstance().queryAll().get(i).getDispalyType() + "   isConnected:  " + DeviceService.getInstance().queryAll().get(i).isConnected() ;
            LogUtils.uploadLogFIleWithTime("device state:" + deviceState);

        }

        return deviceState;
    }

    //上传
    static void upLoadFile() {
        File file = FileUtils.getFileByPath(LogUtils.getUploadPath2());
        if (file == null){
            return;
        }
        UploadFileHelper.upload("10", file, new Callback<Reponses.UploadRepones>() {
            @Override
            public void onSuccess(Reponses.UploadRepones uploadRepones) {
//                ToastUtils.show(uploadRepones.msg);
                if (uploadRepones.rc == 0) {
//                    LogUtils.i("onSuccess", uploadRepones.toString());
                    String currentDate = DateUtil.getCurrentDate();
                    PreferenceUtils.setBool(currentDate , true);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }
}
