package com.legent.plat.events;

import com.legent.plat.pojos.device.IDevice;

import java.util.List;

/**
 * 设备集变更事件
 *
 * @author sylar
 */
public class DeviceCollectionChangedEvent {

    public List<IDevice> devices;

    public DeviceCollectionChangedEvent(List<IDevice> devices) {
        this.devices = devices;
    }
}
