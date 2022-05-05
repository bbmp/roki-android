package com.legent.plat.events;

import com.legent.plat.pojos.device.IDevice;

/**
 * 设备更新事件
 *
 * @author sylar
 */
public class DeviceUpdatedEvent extends AbsDeviceEvent {
    public DeviceUpdatedEvent(IDevice device) {
        super(device);
    }
}