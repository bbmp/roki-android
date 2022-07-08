package com.robam.common.pojos.device;

import java.io.Serializable;

/**
 * Created by sylar on 15/6/10.
 *
 * des : 烟机只能设定状态
 */
public class SmartParams implements Serializable {


    /**
     * 烟机电磁灶开关联动(0关，1开)[1Byte]
     */
    public boolean IsPowerLinkage = true;
    /**
     * 烟机过热保护开关(0关，1开)[1Byte]
     */
    public boolean IsOverTempProtectSwitch = true;
    /**
     * 烟机过热保护设置时间 默认280℃
     */
    public short IsOverTempProtectSet = 280;

    /**
     * 烟机档位联动开关（0关，1开）[1Byte]
     */
    public boolean IsLevelLinkage = true;

    /**
     * 电磁灶关机后烟机延时关机开关（0关，1开）[1Byte]
     */
    public boolean IsShutdownLinkage = true;


    /**
     * 电磁灶关机后烟机延时关机时间（延时时间，单位分钟，1~5分钟）[1Byte]
     */
    public short ShutdownDelay = 1;


    /**
     * 油烟机清洗提示开关[1Byte], 0不提示，1提示
     */
    public boolean IsNoticClean = true;

    /**
     * 是否开启定时通风[1BYTE]
     */
    public boolean IsTimingVentilation = false;
    /**
     * 定时通风间隔时间[1BYTE],单位天
     */
    public short TimingVentilationPeriod = 3;

    /**
     * 是否开启每周通风[1BYTE]
     */
    public boolean IsWeeklyVentilation = false;

    /**
     * 每周通风的时间--周几
     */
    public short WeeklyVentilationDate_Week = 1;

    /**
     * 每周通风的时间--小时
     */
    public short WeeklyVentilationDate_Hour = 12;

    /**
     * 每周通风的时间--分钟
     */
    public short WeeklyVentilationDate_Minute = 30;

    public short R8230S_Switch = 1;//0 _ 是关 1 _ 是开

    public short R8230S_Time = 3;

    public short gesture = 1;//0 _ 是关 1 _ 是开
    /**
     * 灶具最小火力持续5分联动烟机最小档(烟机自动)
     */
    public short fanStoveAuto = 0 ;

    @Override
    public String toString() {
        return "SmartParams{" +
                "IsPowerLinkage=" + IsPowerLinkage +
                ", IsOverTempProtectSwitch=" + IsOverTempProtectSwitch +
                ", IsOverTempProtectSet=" + IsOverTempProtectSet +
                ", IsLevelLinkage=" + IsLevelLinkage +
                ", IsShutdownLinkage=" + IsShutdownLinkage +
                ", ShutdownDelay=" + ShutdownDelay +
                ", IsNoticClean=" + IsNoticClean +
                ", IsTimingVentilation=" + IsTimingVentilation +
                ", TimingVentilationPeriod=" + TimingVentilationPeriod +
                ", IsWeeklyVentilation=" + IsWeeklyVentilation +
                ", WeeklyVentilationDate_Week=" + WeeklyVentilationDate_Week +
                ", WeeklyVentilationDate_Hour=" + WeeklyVentilationDate_Hour +
                ", WeeklyVentilationDate_Minute=" + WeeklyVentilationDate_Minute +
                ", R8230S_Switch=" + R8230S_Switch +
                ", R8230S_Time=" + R8230S_Time +
                ", gesture=" + gesture +
                '}';
    }

    /*    @Override
    public String toString() {
        return "SmartParams{" +
                "linkage_stove_chbox=" + IsPowerLinkage +
                ", linkage_stove_chbox=" + IsLevelLinkage +
                ", linkage_stove_chbox=" + IsShutdownLinkage +
                ", ShutdownDelay=" + ShutdownDelay +
                ", 油网清洁=" + IsNoticClean +
                ", linkage_air_chbox=" + IsTimingVentilation +
                ", linkage_air_chbox=" + IsWeeklyVentilation +
                ", 天数" + TimingVentilationPeriod +
                ", 每周=" + WeeklyVentilationDate_Week +
                ", 小时=" + WeeklyVentilationDate_Hour +
                ", 分钟=" + WeeklyVentilationDate_Minute +
                ", R8230S_Switch=" + R8230S_Switch +
                ", R8230S_Time=" + R8230S_Time +
                ", gesture=" + gesture +
                '}';
    }*/
}
