package com.robam.common.io.device;

import com.google.common.base.Preconditions;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.pojos.device.IDeviceFactory;
import com.legent.plat.pojos.device.SubDeviceInfo;

/**
 * Created by sylar on 15/8/10.  edited by zhaiyuanyi 15/10/16
 */
public class RokiDeviceFactory implements IDeviceFactory {

    @Override
    public IDevice generate(SubDeviceInfo deviceInfo) {
        return generateModel(deviceInfo);
    }

    public static IDevice generateModel(SubDeviceInfo subDeviceInfo) {
        Preconditions.checkNotNull(subDeviceInfo, "deviceInfo is null!");
        IDevice iDevice = DeviceModelFactory.generateModelByTypeAndCategory(subDeviceInfo);
        return iDevice;
    }
}
