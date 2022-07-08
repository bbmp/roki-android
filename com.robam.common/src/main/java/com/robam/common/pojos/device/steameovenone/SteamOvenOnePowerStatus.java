package com.robam.common.pojos.device.steameovenone;

/**
 * Created by Administrator on 2017/7/11.
 */

public interface SteamOvenOnePowerStatus {

    /**
     * 待机
     */
   public short Wait = 0;

    /**
     * 关机
     */
    public short Off = 1;
    /**
     * 关机
     */
    public short Off_NEW = 0;
    /**
     * 待机
     */
    public short AWAIT = 1;

    /**
     * 开机
     */
    public  short On = 2;

    /**
     * 用于判断菜谱关机的指令
     */
    public short RecipeOff = 15;

}
