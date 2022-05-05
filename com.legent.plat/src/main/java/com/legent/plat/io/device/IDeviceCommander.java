package com.legent.plat.io.device;

public interface IDeviceCommander extends ICoreCommand {

    /**
     * 初始化设备IO
     *
     * @param deviceId
     */
    void initIO(String deviceId);

    /**
     * 停用设备IO
     *
     * @param deviceId
     */
    void disposeIO(String deviceId);

}