package com.robam.common.pojos.device.rika;

/**
 * Created by 14807 on 2018/4/9.
 */

public interface RikaStatus {


    /**
     * 各品类编码
     */
    short FAN_CATEGORYCODE = 65;    //A

    short STOVE_CATEGORYCODE = 66;  //B

    short STERIL_CATEGORYCODE = 67; //C

    short STEAM_CATEGORYCODE = 68;  //D

    short STEAMQVEN_CATEGORYCODE = 69;  //E

    /**
     * 关机
     */
    short FAN_OFF = 0;
    /**
     * 开机
     */
    short FAN_ON = 1;

    /**
     * 延时关机
     */
    short FAN_DELAY_OFF = 2;

    /**
     * 待机
     */
    short FAN_AWAIT = 3;

    /**
     * 清洗锁定状态
     */
    short FAN_CLEAN_LOCK = 4;

    /**
     * 挡风板拆除
     */
    short FAN_WIND_SCREEN_DISMANDLE = 5;

    short FAN_NULL = 255;

    /**
     * 延时关机
     */
    short FAN_TIME_DELAY_OFF = 2;

    /**
     * 待机
     */
    short FAN_WAIT = 3;


    /**
     * 清洗锁定状态
     */
    short FAN_CLEANING_LOCK = 4;

    /**
     * 烟机挡风板拆除状态
     */
    short FAN_WINDSHIELD_DISMANTLING = 5;

    /**
     * 烟机照明灯关
     */
    short FAN_LIGHT_OFF = 0;

    /**
     * 烟机照明灯开
     */
    short FAN_LIGHT_ON = 1;

    short STOVE_OFF = 0;
    short STOVE_WAIT = 1;
    short STOVE_WORK = 2;

    short STEAM_NOT = -1;
    short STEAM_WAIT = 0;
    short STEAM_OFF = 1;
    short STEAM_ON = 2;
    short STEAM_STOP = 3;
    short STEAM_RUN = 4;
    short STEAM_PRE = 6;
    short STEAM_PREHEAT = 9;//预热
    short STERIL_NOT = -2;
    short STERIL_OFF = 0;//消毒柜关机

    short STERIL_ON = 1;//消毒柜开机

    short STERIL_DISIDFECT = 2; //消毒

    short STERIL_CLEAN = 3; //保洁

    short STERIL_DRYING = 4; //烘干

    short STERIL_PRE = 5; //预约消毒

    short STERIL_ALARM = 6; //报警

    short STERIL_DEGERMING = 7; //除菌

    short STERIL_INTELLIGENT_DETECTION = 8; //智能检测

    short STERIL_INDUCTION_STERILIZATION = 9; //感应杀菌

    short STERIL_WARM_DISH = 10; //暖碟

    short STERIL_APPOINATION = 11; //预约消毒

    short STERIL_APPOINATION_DRYING = 12; //预约烘干

    short STERIL_APPOINATION_CLEAN = 13; //预约快速保洁

    short STERIL_COER_DISIDFECT = 14; //强制消毒

    short STERIL_LOCK_ON = 1; //童锁开

    short STERIL_LOCK_OFF = 0; //童锁关

    short STEAMOVEN_NOT = -3;

    short STEAMOVEN_WAIT = 0;//待机

    short STEAMOVEN_OFF = 1;//关机

    short STEAMOVEN_ON = 2;//开机

    short STEAMOVEN_STOP = 3;//暂停

    short STEAMOVEN_RUN = 4;//运行

    short STEAMOVEN_ALARM = 5;//报警

    short STEAMOVEN_ORDER = 6;//预约状态

    short STEAMOVEN_PREHEAT = 9;//预热状态

    int SUBSET_OFF_COUNT = 3;

}
