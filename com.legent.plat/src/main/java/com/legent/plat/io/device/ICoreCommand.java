package com.legent.plat.io.device;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceInfo;

public interface ICoreCommand {


    /**
     * 获取设备连网激活器
     *
     * @return
     */
    IDeviceFinder getDeviceFinder();

    /**
     * 获取设备详情(主设备及子设备)
     *
     * @param deviceId 主设备编码
     * @param callback
     */
    void getDevice(String deviceId, Callback<DeviceInfo> callback);


    /**
     * 设置wifi参数
     *
     * @param deviceId 设备编码
     * @param wifiSsid wifi网络的ssid
     * @param wifiPwd  wifi网络的连接密码
     * @param callback
     */
    void setWifiParam(String deviceId, String wifiSsid, String wifiPwd,
                      final VoidCallback callback);

    /**
     * 设置设备的拥有者
     *
     * @param deviceId 设备编码
     * @param ownerId  拥有者的用户编码
     * @param callback
     */
    void setOwnerId(String deviceId, long ownerId, VoidCallback callback);

}
