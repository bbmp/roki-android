package com.legent.plat.events;

import com.legent.plat.pojos.device.IDevice;

/**
 * 移除设备事件
 *
 * @author sylar
 */
public class DeviceDeletedEvent extends AbsDeviceEvent {
    public DeviceDeletedEvent(IDevice device) {
        super(device);
    }
}
