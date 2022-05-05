package com.robam.common.pojos.device.steameovenone;

/**
 * Created by Administrator on 2017/7/11.
 */

public interface SteamOvenOnePowerStatus {

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
     * 用于判断菜谱关机的指令
     */
    short RecipeOff = 15;

}
