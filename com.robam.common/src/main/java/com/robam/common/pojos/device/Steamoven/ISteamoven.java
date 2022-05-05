package com.robam.common.pojos.device.Steamoven;

import com.legent.VoidCallback;
import com.robam.common.pojos.device.IPauseable;

/**
 * Created by Rosicky on 15/12/14.
 */
public interface ISteamoven extends IPauseable {

    /**
     * model of fan
     */
    String getSteamovenModel();

    /**
     * 设置蒸汽炉工作时间
     *
     * @param time
     * @param callback
     */
    void setSteamWorkTime(short time, VoidCallback callback);


    /**
     * 设置蒸汽炉工作温度
     *
     * @param temp
     * @param callback
     */
    void setSteamWorkTemp(short temp, VoidCallback callback);

    /**
     * 设置蒸汽炉烹饪模式
     *
     * @param time
     * @param cookbook
     * @param callback
     */
    void setSteamWorkMode(short cookbook, short temp, short time, short preflag, VoidCallback callback);


    /**
     * 设置蒸汽炉专业模式
     *
     * @param time
     * @param temp
     * @param callback
     */
    void setSteamProMode(short time, short temp, VoidCallback callback);

    /**
     * 查询蒸汽炉状态
     *
     * @param callback
     */
    void getSteamStatus(VoidCallback callback);

    /**
     * 设置蒸汽炉状态
     *
     * @param status
     * @param callback
     */
    void setSteamStatus(short status, VoidCallback callback);

}
