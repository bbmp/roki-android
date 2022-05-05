package com.legent.plat.events;


import com.legent.plat.pojos.device.DeviceInfo;

/**
 * 设备上线通知事件
 *
 * @author sylar
 */
public class DeviceConnectedNoticEvent extends AbsDeviceInfoEvent {
    public DeviceConnectedNoticEvent(DeviceInfo deviceInfo) {
        super(deviceInfo);
    }
}
