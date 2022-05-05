package com.legent.plat.events;

import com.legent.plat.pojos.device.IDevice;

/**
 * 设备选中事件
 *
 * @author sylar
 */
public class DeviceSelectedEvent extends AbsDeviceEvent {
    public DeviceSelectedEvent(IDevice device) {
        super(device);
    }
}
