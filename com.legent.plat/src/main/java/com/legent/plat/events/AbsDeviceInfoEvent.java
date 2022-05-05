package com.legent.plat.events;

import com.legent.plat.pojos.device.DeviceInfo;

abstract public class AbsDeviceInfoEvent {
    public DeviceInfo deviceInfo;

    public AbsDeviceInfoEvent(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}