package com.legent.plat.events;

import com.legent.plat.pojos.device.DeviceInfo;

/**
 * 设备发现事件
 *
 * @author sylar
 */
public class DeviceFindEvent extends AbsDeviceInfoEvent {

    public DeviceFindEvent(DeviceInfo deviceInfo) {
        super(deviceInfo);
    }
}
