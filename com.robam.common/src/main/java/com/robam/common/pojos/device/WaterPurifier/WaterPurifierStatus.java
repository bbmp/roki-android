package com.robam.common.pojos.device.WaterPurifier;

/**
 * Created by RENT on 2016/05/30.
 */
public interface WaterPurifierStatus {
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
     * 制水
     */
    short Purify = 3;

    /**
     * 冲洗
     */
    short Wash = 4;
    /**
     * 报警状态
     */
    short AlarmStatus = 5;
    /**
     * 未知状态 -
     */
    short None=-1;
}
