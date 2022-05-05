package com.robam.common.pojos.device.integratedStove;

import java.io.Serializable;

/**
 * Created by sylar on 15/6/10.
 */
public class IntegStoveSmartParams implements Serializable{


    /**
     * 烟机灶具开关联动(0关，1开)[1Byte]
     */
    public boolean fanAndStoveSwitchLinkage = true;

    /**
     * 烟机档位联动开关（0关，1开）[1Byte]
     */
    public boolean fanPowerSwitchLinkage;

    /**
     * 电磁灶关机后烟机延时关机开关（0关，1开）[1Byte]
     */
    public boolean fanTimeDelayShutdownSwitch;


    /**
     * 电磁灶关机后烟机延时关机时间（延时时间，单位分钟，0~10分钟）[1Byte]
     */
    public short fanDelaySwitchTime = 3;

    /**
     * 一体机开始工作,烟机联动工作开关(0关，1开)[1Byte]
     */
    public short fanSteamOvenLinkage;

    /**
     * 一体机结束工作,烟机延迟关闭开关(0关，1开)[1Byte]
     */
    public short fanSteamOvenDelayShutdownSwitch;

    /**
     * 油烟机清洗提示开关[1Byte], 0不提示，1提示
     */
    public boolean fanCleaningPromptSwitch;

    /**
     * 是否开启定时通风[1BYTE],0关，1开
     */
    public boolean fanOpenRegularVentilation;
    /**
     * 定时通风间隔时间[1BYTE],单位天
     */
    public short fanRegularVentilationIntervalTime = 3;

    /**
     * 是否开启每周通风[1BYTE],0关，1开
     */
    public boolean fanOpenWeeklyVentilation;

    /**
     * 每周通风的时间--周几
     */
    public short WeeklyVentilationDate_Week = 3;

    /**
     * 每周通风的时间--小时
     */
    public short WeeklyVentilationDate_Hour = 12;

    /**
     * 每周通风的时间--分钟
     */
    public short WeeklyVentilationDate_Minute = 30;

    @Override
    public String toString() {
        return "RikaSmartParams{" +
                "fanAndStoveSwitchLinkage=" + fanAndStoveSwitchLinkage +
                ", fanPowerSwitchLinkage=" + fanPowerSwitchLinkage +
                ", fanTimeDelayShutdownSwitch=" + fanTimeDelayShutdownSwitch +
                ", fanDelaySwitchTime=" + fanDelaySwitchTime +
                ", fanCleaningPromptSwitch=" + fanCleaningPromptSwitch +
                ", fanOpenRegularVentilation=" + fanOpenRegularVentilation +
                ", fanRegularVentilationIntervalTime=" + fanRegularVentilationIntervalTime +
                ", fanOpenWeeklyVentilation=" + fanOpenWeeklyVentilation +
                ", WeeklyVentilationDate_Week=" + WeeklyVentilationDate_Week +
                ", WeeklyVentilationDate_Hour=" + WeeklyVentilationDate_Hour +
                ", WeeklyVentilationDate_Minute=" + WeeklyVentilationDate_Minute +
                '}';
    }
}
