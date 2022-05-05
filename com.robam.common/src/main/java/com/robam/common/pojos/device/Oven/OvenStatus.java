package com.robam.common.pojos.device.Oven;

/**
 * Created by linxiaobin on 2015/12/27.
 */
public interface OvenStatus {
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
     * 自检
     */
    short SelfTest = 5;
    /**
     * 报警状态
     */
    short AlarmStatus = 6;
    /**
     * 预约
     */
    short Order = 7;
    /**
     * 预设
     */
    short PreSetting = 8;
    /**
     * 预热
     */
    short PreHeat = 9;

    //软件端手动设置完成
    short complete = 10;
}
