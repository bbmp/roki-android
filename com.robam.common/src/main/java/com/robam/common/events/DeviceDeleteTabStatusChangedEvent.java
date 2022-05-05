package com.robam.common.events;

/**
 * Created by Rosicky on 16/3/8.
 */
public class DeviceDeleteTabStatusChangedEvent {
    public String deviceType;

    public DeviceDeleteTabStatusChangedEvent(String deviceType) {
        this.deviceType = deviceType;
    }
}
