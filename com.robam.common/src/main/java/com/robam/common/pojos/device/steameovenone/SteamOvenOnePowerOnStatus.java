package com.robam.common.pojos.device.steameovenone;

/**
 * Created by Administrator on 2017/7/11.
 */

public interface SteamOvenOnePowerOnStatus {

    /**
     * 操作状态
     */
    short OperatingState = 0;

    /**
     * 暂停
     */
    short Pause = 1;

    /**
     * 预约
     */
    short Order = 2;

    /**
     * 工作状态
     */
    short WorkingStatus = 3;

    /**
     * 报警状态
     */
    short AlarmStatus = 4;

    /**
     * 时钟显示
     */
    short TimeDisplay = 5;

    /**
     * 无状态
     */
    short NoStatus = 255;


}
