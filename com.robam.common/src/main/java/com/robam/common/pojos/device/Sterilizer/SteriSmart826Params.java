package com.robam.common.pojos.device.Sterilizer;

import com.robam.common.R;

import static com.legent.ContextIniter.cx;

/**
 * Created by zhiayuanyi on 2016/3/29.
 */
public class SteriSmart826Params {
    /**
     * 是否开启隔几日消毒[1BYTE]
     */
    public boolean IsInternalDays = false;
    /**
     * 定时消毒间隔天数
     */
    public short InternalDays = 7;
    /**
     * •	是否开启每周消毒
     */
    public boolean IsWeekSteri = true;
    /**
     * 周几消毒
     */
    public short  WeeklySteri_week = 7;
    /**
     * 峰谷电时间
     */
    public short PVCTime = 23;

    @Override
    public String toString() {
        return "SteriSmartParams{" +
                "IsInternalDays=" + IsInternalDays +
                ", InternalDays=" + InternalDays +
                ", IsWeekSteri=" + IsWeekSteri +
                ", WeeklySteri_week=" + WeeklySteri_week +
                ", PVCTime=" + PVCTime +
                '}';
    }
}
