package com.legent.plat.io.device.finder;

import android.content.Context;

import com.legent.plat.Plat;
import com.legent.utils.LogUtils;
import com.legent.utils.api.WifiUtils;

import io.fogcloud.sdk.easylink.api.EasyLink;
import io.fogcloud.sdk.easylink.helper.EasyLinkCallBack;
import io.fogcloud.sdk.easylink.helper.EasyLinkParams;

/**
 * Created by sylar on 15/7/9.
 */
public class EasyLink31 implements IEasyLink {


    static final String TAG = "ftc";
    private static EasyLink31 instance = new EasyLink31();

    synchronized public static EasyLink31 getInstance() {
        return instance;
    }
    Context cx;
    EasyLink elink;
    private EasyLink31() {
        cx = Plat.activtiyContext;
        elink = new EasyLink(cx);
    }


    @Override
    public void start(String ssid, String wifiPwd, String userInfo) {
        WifiUtils.allowMulticast(Plat.app);
        EasyLinkParams easylinkPara = new EasyLinkParams();
        easylinkPara.ssid = ssid;
        easylinkPara.password = wifiPwd;
        easylinkPara.runSecond = 180000;
        easylinkPara.sleeptime = 20;
        easylinkPara.isSendIP = true;
        easylinkPara.extraData = userInfo;
        LogUtils.i("20170904", "param::" + easylinkPara.ssid + " " + easylinkPara.password + " " + easylinkPara.runSecond + " " + easylinkPara.isSendIP + " " + easylinkPara.extraData);
        elink.startEasyLink(easylinkPara, new EasyLinkCallBack() {
            @Override
            public void onSuccess(int code, String message) {
              if (Plat.DEBUG)
                  LogUtils.i("TAG","code:"+code+" message::"+message);
               // ToastUtils.show("连接成功", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(int code, String message) {
                if (Plat.DEBUG)
                    LogUtils.i("TAG","code:"+code+" message::"+message);
               //  ToastUtils.show("连接失败", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void stop() {
        WifiUtils.disableMulticast();
        elink.stopEasyLink(new EasyLinkCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                if (Plat.DEBUG)
                    LogUtils.i("TAG","code:"+code+" message::"+message);
               // ToastUtils.show("连接成功啦stop", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(int code, String message) {
                if (Plat.DEBUG)
                    LogUtils.i("TAG","code:"+code+" message::"+message);
               // ToastUtils.show("连接失败啦stop", Toast.LENGTH_SHORT);
            }
        });
    }
}
