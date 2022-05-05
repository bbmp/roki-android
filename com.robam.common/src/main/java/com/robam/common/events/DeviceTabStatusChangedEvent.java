package com.robam.common.events;

/**
 * Created by Rosicky on 16/3/8.
 */
public class DeviceTabStatusChangedEvent {
    public String id;
    public boolean isOn;
    public DeviceTabStatusChangedEvent(String id, boolean isOn) {
        this.id = id;
        this.isOn = isOn;
    }
}
