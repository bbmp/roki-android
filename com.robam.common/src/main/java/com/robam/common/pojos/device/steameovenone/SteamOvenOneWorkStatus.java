package com.robam.common.pojos.device.steameovenone;

/**
 * Created by Administrator on 2017/7/11.
 */

public interface SteamOvenOneWorkStatus {

    /**
     * 预热
     */
    short PreHeat = 0;

    /**
     * 运行
     */
    short Working = 1;

    /**
     * 加热完成
     */
    short HeatFish = 2;

    /**
     * 余水回收
     */
    short WaterRecovery = 3;

    /**
     * 无状态
     */
    short NoStatus = 255;

}
