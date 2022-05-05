package com.legent.plat.events;

import com.legent.plat.pojos.device.IDevice;

/**
 * 设备连接状态变更事件
 */
public class DeviceConnectionChangedEvent extends AbsDeviceEvent {

    public boolean isConnected;

    public DeviceConnectionChangedEvent(IDevice device, boolean isConnected) {
        super(device);
        this.isConnected = isConnected;
    }
}