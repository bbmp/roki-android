package com.legent.plat.io.device.msg;


/**
 * 消息码表
 *
 * @author sylar
 */
public interface MsgKeys {

    // -------------------------------------------------------------------------------
    // 通知类
    // -------------------------------------------------------------------------------

    /**
     * 设备上线通知
     */
    short DeviceConnected_Noti = 32;

    /**
     * WiFi配置完成通知
     */
    short DeviceActivated_Noti = 33;

    /**
     * 子设备列表变更通知
     */
    short SubDeviceChanged_Noti = 46;

    /**
     * 云端推送通知信息
     */
    short CloudPush_Noti = 73;

    // -------------------------------------------------------------------------------
    // 应答类
    // -------------------------------------------------------------------------------

    /**
     * 获取WiFi信号强度(请求)
     */
    short GetWifiSignal_Req = 34;

    /**
     * 获取WiFi信号强度(应答)
     */
    short GetWifiSignal_Rep = 35;

    /**
     * 设置路由器信息(请求)
     */
    short SetWifiParamsAndOwner_Req = 36;

    /**
     * 设置路由器信息(应答)
     */
    short SetWifiParamsAndOwner_Rep = 37;

    /**
     * 删除子设备(请求)
     */
    short RemoveChildDevice_Req = 38;

    /**
     * 删除子设备(应答)
     */
    short RemoveChildDevice_Rep = 39;

    /**
     * 获取设备列表(请求)
     */
    short GetDevices_Req = 40;

    /**
     * 获取设备列表(应答)
     */
    short GetDevices_Rep = 41;

    /**
     * 触发设备进入配对模式(请求)
     */
    short MakePair_Req = 44;

    /**
     * 触发设备进入配对模式(应答)
     */
    short MakePair_Rep = 45;

    /**
     * 取消配对模式(请求)
     */
    short ExitPair_Req = 47;

    /**
     * 取消配对模式(应答)
     */
    short ExitPair_Rep = 48;

}
