package com.legent.plat.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.RemoteException;

import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.pojos.device.IDeviceHub;
import com.legent.services.ScreenPowerService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.AlarmUtils;

import java.util.List;

/**
 * Created by sylar on 15/7/25.
 */
public class DevicePollingReceiver extends BroadcastReceiver {

    private static boolean isFeedDog = false;
    private static boolean isPolling = true;

    //当前需要轮询的设备
    public static String currentPollingDevice;

    public static void setCurrentPollingDevice(String currentPollingDevice) {
        DevicePollingReceiver.currentPollingDevice = currentPollingDevice;
    }


    static public Intent getIntent(Context cx) {
        Intent intent = new Intent(cx, DevicePollingReceiver.class);
        intent.setAction(DevicePollingReceiver.class.getName());
        return intent;
    }

    @Override
    public void onReceive(Context cx, Intent i) {
        try {
            onPolling();
        } catch (Exception e) {
            LogUtils.logFIleWithTime("轮询出错:" + e.getMessage());
            LogUtils.i("test","开始轮询:true");
        }

        if (Build.VERSION.SDK_INT >= 19) {
            /** API 19 后的 AlarmManager 不再提供精准闹钟
             *
             * From API level 19, all repeating alarms are inexact—that is, if our application targets KitKat or above, our repeat alarms will be inexact even if we use setRepeating.
             If we really need exact repeat alarms, we can use setExact instead, and schedule the next alarm while handling the current one.
             *
             * **/
            AlarmUtils.startPollingWithBroadcast(cx,
                    DevicePollingReceiver.getIntent(cx),
                    Plat.deviceService.getPollingPeriod(),
                    Plat.deviceService.getPollingTaskId());
        }

    }


    /**
     * 轮训任务
     */
    protected Runnable AppPollingTask = new Runnable() {

        @Override
        public void run() {
            onPolling();
        }
    };

    void onPolling() {
        if (Plat.LOG_FILE_ENABLE) {
            LogUtils.logFIleWithTime("开始轮询");
            LogUtils.i("test","开始轮询:true");
        }
        //熄屏状态下禁止轮询
        if (Plat.appType.equals(IAppType.RKDRD)) {
            if (!ScreenPowerService.getInstance().isScreenOn()) {

                return;
            }
            LogUtils.i("2020061101","isScreenOn:false");
        }

        List<IDevice> devices = Plat.deviceService.queryAll();
        if (devices != null && devices.size() > 0) {
            for (IDevice dev : devices) {
                if (IDeviceType.RYYJ.equals(dev.getDc())) {
                    if (Plat.fanGuid != null && !Plat.fanGuid.getID().equals(dev.getGuid().getID()))
                        continue;
                }
                onPolling(dev);
                if (dev instanceof IDeviceHub) {
                    IDeviceHub hub = (IDeviceHub) dev;
                    List<IDevice> children = hub.getChildren();
                    if (children != null && children.size() > 0) {
                        for (IDevice device : children) {
                            LogUtils.i("20190215","device::"+device.getGuid());
                            onPolling(device);
                        }
                    }
                }
            }
        }
    }

    void onPolling(IDevice device) {

        if (IDeviceType.RRQZ.equals(device.getDeviceType().getDc()) || IDeviceType.RDCZ.equals(device.getDeviceType().getDc())
                ||  IDeviceType.RPOT.equals(device.getDeviceType().getDc()) || IDeviceType.RZNG.equals(device.getDeviceType().getDc())) {
            device.onPolling();
            device.onCheckConnection();
            return;
        }

        if (currentPollingDevice != null && !(currentPollingDevice.equals(device.getDeviceType().getName()))) {
            if (Plat.DEBUG) {
                LogUtils.i("20170308", "currentPollingDevice:" + currentPollingDevice);
                LogUtils.i("20170308", "device.getDeviceType().getName():" + device.getDeviceType().getName());
            }
            return;
        }
        if (IDeviceType.RJCZ.equals(device.getDeviceType().getDc())){
            device.onPolling();
            device.onCheckConnection();
            return;
        }

        device.onPolling();
        device.onCheckConnection();
    }

}
