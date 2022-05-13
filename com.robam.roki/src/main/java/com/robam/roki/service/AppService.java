package com.robam.roki.service;

import android.content.Context;
import android.util.Log;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceLoadCompletedEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.pojos.device.DeviceGroupInfo;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.services.DeviceService;
import com.legent.services.AbsService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.io.device.RokiDeviceFactory;

import java.util.List;

/**
 * Created by sylar on 15/7/24.
 */
public class AppService extends AbsService {

    private static AppService instance = new AppService();

    synchronized public static AppService getInstance() {
        return instance;
    }

    long userId;
    DeviceService ds;

    private AppService() {

    }


    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);

        ds = Plat.deviceService;
        if (Plat.accountService.isLogon()) {
            onLogin();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        userId = event.pojo.id;
        onLogin();
    }

    @Subscribe
    public void onEvent(UserLogoutEvent event) {
        userId = 0;
        ds.clear();
    }

    // -------------------------------------------------------------------------------
    // private
    // -------------------------------------------------------------------------------


    public void onLogin() {
//        onLoadGroup();
        onLoadDevice();
    }


    DeviceInfo deviceInfo1;

    private void onLoadDevice() {
        Log.i("20180306", "userid:" + userId);
        if (userId == 0) return;

        //deviceInfo1 = new DeviceInfo("RQ03593463175fe00","d0bae4647d37",295827149);
        //deviceInfo1.setDc("RDKX");
        //deviceInfo1.setDp("DKX02");
        //deviceInfo1.setDt("RQ035");
        //deviceInfo1.setDisplayType("RQ035");
        //deviceInfo1.setCategoryEnglishName("Oven");
        //deviceInfo1.setCategoryIconUrl("http://roki-test.oss-cn-qingdao.aliyuncs.com/app/maintenance/1e549ea9-3cc8-474a-baee-9f214afd9185");
        //deviceInfo1.setCategoryName("电烤箱");
        //deviceInfo1.ver=39;

        CloudHelper.getDevices(userId, new Callback<List<DeviceInfo>>() {

            @Override
            public void onSuccess(List<DeviceInfo> result) {

                List<IDevice> devices = Lists.newArrayList();
                //暂时注释
                if (result != null) {
                    IDevice defaultFan = null;
                     //result.add(deviceInfo1);
                    for (DeviceInfo deviceInfo : result) {
                        boolean isFan = Utils.isFan(deviceInfo.guid);
//                        if (isFan && defaultFan != null) {
//                            continue;
//                        }
                        LogUtils.i("20190624", "deviceInfo::" + deviceInfo.getID() + "   bid:" + deviceInfo.bid);
                        IDevice dev = RokiDeviceFactory.generateModel(deviceInfo);
                        devices.add(dev);
                        if (isFan) {
                            defaultFan = dev;
                        }
                    }
                    LogUtils.out("roki_rent:" + devices.toString() + " ll");
                    ds.clear();
                    ds.batchAdd(devices);
                    EventUtils.postEvent(new DeviceLoadCompletedEvent());
                    if (defaultFan != null) {
                        ds.setDefault(defaultFan);
                    }
                } else {
                    ds.clear();
                    EventUtils.postEvent(new DeviceLoadCompletedEvent());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
