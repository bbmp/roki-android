package com.legent.plat.events;


import com.legent.plat.pojos.device.IDevice;

/**
 * 设备状态变更事件
 */
public class DeviceStatusChangedEvent extends AbsDeviceEvent {
    public DeviceStatusChangedEvent(IDevice device) {
        super(device);
    }
}