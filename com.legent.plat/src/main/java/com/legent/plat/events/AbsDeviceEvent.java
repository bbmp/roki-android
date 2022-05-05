package com.legent.plat.events;

import com.legent.plat.pojos.device.IDevice;

abstract public class AbsDeviceEvent {
    public IDevice device;

    public AbsDeviceEvent(IDevice device) {
        this.device = device;
    }
}