package com.legent.plat.pojos.device;

/**
 * 设备模型工厂
 * Created by sylar on 15/8/10.
 */
public interface IDeviceFactory {

    /**
     * 生成设备模型
     * @param deviceInfo 设备描述
     * @return IDevice 设备对象接口
     */
    IDevice generate(SubDeviceInfo deviceInfo);

}
