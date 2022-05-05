package com.robam.common.pojos.device.Steamoven;

/**
 * Created by WZTCM on 2015/12/16.
 */
public interface SteamStatus {
    /**
     * 待机
     */
    short Wait = 0;
    /**
     * 关机
     */
    short Off = 1;
    /**
     * 开机
     */
    short On = 2;
    /**
     * 暂停
     */
    short Pause = 3;
    /**
     * 运行
     */
    short Working = 4;
    /**
     * 报警状态
     */
    short AlarmStatus = 5;
    /**
     * 预约
     */
    short Order = 6;
    /**
     * 预设
     */
    short PreSetting = 8;
    /**
     * 预热
     */
    short PreHeat = 9;
}
