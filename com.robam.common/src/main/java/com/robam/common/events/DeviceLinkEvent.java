package com.robam.common.events;

/**
 * Created by WZTCM on 2016/1/11.
 */
public class DeviceLinkEvent {
    String deviceName;

    public DeviceLinkEvent(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }
}
