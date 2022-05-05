package com.legent.plat.events;

import com.legent.plat.pojos.device.IDevice;

/**
 * 添加设备事件
 *
 * @author sylar
 */
public class DeviceAddedEvent extends AbsDeviceEvent {
    public DeviceAddedEvent(IDevice device) {
        super(device);
    }
}