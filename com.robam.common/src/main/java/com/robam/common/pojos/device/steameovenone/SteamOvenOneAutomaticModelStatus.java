package com.robam.common.pojos.device.steameovenone;

/**
 * Created by Administrator on 2017/7/11.
 * 一体机自动模式
 */

public interface SteamOvenOneAutomaticModelStatus {


    /**
     * 无模式
     */
    short No_Model = 0;

    /**
     * 解冻
     */
    short Unfreeze = 1;

    /**
     * 保温
     */
    short HeatPreservation = 2;

    /**
     * 果蔬烘干
     */
    short FruitAndVegetableDrying = 3;

    /**
     * 快速预热
     */
    short FastPreHeat = 4;


    /**
     *蒸汽杀菌
     */
    short SteamSterilize = 5;

    /**
     *清洁
     */
    short Clean = 6;

    /**
     *干燥
     */
    short Dry = 7;

    /**
     *除垢
     */
    short Descaling = 8;



}
