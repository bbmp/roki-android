package com.legent.plat.io.device.msg;

/**
 * 消息参数码表
 *
 * @author sylar
 */
public interface MsgParams {
    /**
     * 回应码 1B 0-成功，1-失败
     */
    String RC = "RC";

    /**
     * 云端推送内容
     */
    String PushContent = "PushContent";

    /**
     * MAC地址 12B String
     */
    String WifiMac = "WifiMac";

    /**
     * wifi模块信号强度 1B 一般范围在-100Dbm~0Dbm
     */
    String WifiSignal = "WifiSignal";

    /**
     * wifi模块型号 16B String
     */
    String WifiModule = "WifiModule";

    /**
     * 是否设置wifi连接参数 1B 0-不设置 1-设置
     */
    String IsSetWifi = "IsSetWifi";

    /**
     * 是否设置拥有者ID 1B 0-不设置 1-设置
     */
    String IsSetOwner = "IsSetOwner";

    /**
     * SSID String 根据WifiSsid_Len确定长度
     */
    String WifiSsid = "WifiSsid";

    /**
     * Wifi pasword String 根据WifiPwd_Len确定长度
     */
    String WifiPwd = "WifiPwd";

    /**
     * SSID 的字符长度 1B
     */
    String WifiSsid_Len = "WifiSsid_Len";

    /**
     * Wifi pasword 的字符长度 1B
     */
    String WifiPwd_Len = "WifiPwd_Len";

    /**
     * 设备个数 1B
     */
    String DeviceNum = "DeviceNum";

    /**
     * 设备信息 GUID[ASCII, 17字节] + 设备业务编码长度[1Byte] + 设备业务编码[N Byte] + 固件版本［1BYTE］
     */
    String DeviceInfo = "DeviceInfo";

    /**
     * 拥有者ID 10B
     */
    String OwnerId = "OwnerId";

    /**
     * 设备编码 17B
     */
    String Guid = "Guid";

    /**
     * 设备业务编码 根据BID_Len确定长度
     */
    String BID = "BID";

    /**
     * 设备业务编码的字符长度 1B
     */
    String BID_Len = "BID_Len";

    /**
     * 固件版本 1B
     */
    String Ver = "Ver";
}
