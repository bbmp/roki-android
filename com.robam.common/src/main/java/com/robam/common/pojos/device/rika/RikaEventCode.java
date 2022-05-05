package com.robam.common.pojos.device.rika;

/**
 * Created by 14807 on 2018/5/15.
 */

public interface RikaEventCode {

    /**
     * 烟机开关控制事件
     */
    short EVENT_FAN_SWITCH = 10;

    /**
     * 烟机定时调整事件
     */
    short EVENT_FAN_TIMING = 11;


    /**
     * 烟机功率调整事件
     */
    short EVENT_FAN_POWER = 12;

    /**
     * 烟机灯光调整事件
     */
    short EVENT_FAN_LINGHT = 13;


    /**
     * 油烟机清洗提示事件
     */
    short EVENT_FAN_CLEANING_TIPS = 14;

    /**
     * 油烟机清洗锁定事件
     */
    short EVENT_FAN_CLEANING_Lock = 15;

    /**
     * 油烟机挡风板拆卸事件
     */
    short EVENT_FAN_WINDSCREEN_REMOVAL = 16;


    /**
     * 油烟机定时工作完成事件
     */
    short EVENT_FAN_TIMING_COMPLETE_WORK = 17;

    /**
     * 油烟机定时工作完成事件
     */
    short EVENT_FAN_DRY_REMIND = 18;

    /**
     * 油烟机烟灶联动事件
     */
    short EVENT_FAN_STOVE_LINK = 19;

    /**
     * 油烟机烟蒸联动事件
     */
    short EVENT_FAN_STEAM_LINK = 20;

    /**
     * 油烟机烟消联动事件
     */
    short EVENT_FAN_STERIL_LINK = 21;

    /**
     * 消毒柜工作结束事件
     */
    short EVENT_STERIL_WORK_FINISH = 12;


    /**
     * 消毒柜童锁事件
     */
    short EVENT_STERIL_CHILD_LOCK = 13;

}
