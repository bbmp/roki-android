package com.legent.plat.io.device;

import com.legent.Callback;
import com.legent.plat.pojos.device.DeviceInfo;

/**
 * 设备发现探测器
 *
 * @author sylar
 */
public interface IDeviceFinder {

    /**
     * 激活设备。使设备能连上本地wifi，并返回设备guid
     *
     * @param wifiSsid 当前wifi的ssid
     * @param wifiPwd  当前wifi的连接密码
     * @param timeout  超时时间，单位毫秒
     * @param callback 以回调方式返回设备guid
     */
    void start(String wifiSsid, String wifiPwd, int timeout,
               Callback<DeviceInfo> callback);

    /**
     * 取消配置过程
     */
    void stop();
}
