package com.robam.common.pojos.device.steameovenone;

public interface SteamOvenOnePowerStatusNew {

    /**
     * 电源控制
     */
    short powerCtrlKey=2;


    /**
     * 工作控制
     */
    short workCtrlKey =4;


    /**
     * 灯开关
     */
    short lightSwitchKey =8;
    /**
     * 水箱开关
     */
    short SteameOvenWaterChangesKey =11 ;

    /**
     * 发送
     */

    short setSteamTemptureSendKey=102;


    /**
     * 发送时间
     */

    short setSteamTimeSendKey = 104;
    /**
     * 发送模式
     */

    short setSteamModeSendKey = 101;

    /**
     * 段数
     */
    short sectionNumberKey = 100;
}
