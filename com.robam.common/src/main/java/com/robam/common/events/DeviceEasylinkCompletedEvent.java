package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.legent.plat.pojos.device.DeviceInfo;

public class DeviceEasylinkCompletedEvent extends AbsEvent<DeviceInfo> {
    public DeviceEasylinkCompletedEvent(DeviceInfo pojo) {
        super(pojo);
    }
}