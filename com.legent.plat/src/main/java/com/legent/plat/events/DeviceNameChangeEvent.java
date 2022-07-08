package com.legent.plat.events;

import com.legent.plat.pojos.device.IDevice;

/**
 * 设备选中事件
 *
 * @author sylar
 */
public class DeviceNameChangeEvent extends AbsDeviceEvent {
    public DeviceNameChangeEvent(IDevice device) {
        super(device);
    }
}
