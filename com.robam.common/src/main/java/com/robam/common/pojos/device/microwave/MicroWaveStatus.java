package com.robam.common.pojos.device.microwave;

/**
 * Created by WZTCM on 2015/12/17.
 */
public interface MicroWaveStatus {

    /**
     * 报警
     */
    short Alarm = 0;

    /**
     * 待机
     */
    short Wait = 1;

    /**
     * 运行（启动）
     */
    short Run = 2;

    /**
     * 暂停
     */
    short Pause = 3;

    /**
     * 设定状态
     */
    short Setting=4;

    /**
     * 料理结束
     */
    short  RunFinish = 5;

    /**
     * 上电
     */
    short Electrify = 6;

    /**
     * 翻转提醒
     */

    short ReserverRemind = 7;

}
