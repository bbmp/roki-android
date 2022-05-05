package com.robam.common.pojos.device.steameovenone;

/**
 * Created by Administrator on 2017/7/11.
 * 一体机工作模式状态
 */

public interface SteamOvenOneWorkModelStatus {


    /**
     * 快速预热
     */
    short FastPreHeat = 1;

    /**
     * 快热
     */
    short FastHeat = 2;

    /**
     * 风焙烤
     */
    short AirBaking = 3;

    /**
     * 焙烤
     */
    short OvenToast = 4;

    /**
     * 风扇烤
     */
    short AirBarbecue = 5;

    /**
     * 烤烧
     */
    short Barbecue = 6;

    /**
     * 强烤烧
     */
    short StrongBarbecue = 7;

    /**
     * 煎烤
     */
    short BakedBarbecue = 8;

    /**
     * 底加热
     */
    short BottomHeat = 9;

    /**
     * EXP
     */
    short ExpModel = 10;

    /**
     * 果蔬烘干
     */
    short FruitAndVegetableDrying = 11;

    /**
     * 保温
     */
    short HeatPreservation = 12;

    /**
     * 发酵
     */
    short Fermentation = 13;

    /**
     * 鲜嫩蒸
     */
    short StrongSteam = 14;

    /**
     * 营养蒸
     */
    short NutritionSteam = 15;

    /**
     * 高温蒸
     */
    short HighTempSteam = 16;

    /**
     * 解冻
     */
    short Unfreeze = 17;

    /**
     * 蒸汽杀菌
     */
    short Steamsterilization = 18;

    /**
     * 干燥 崩溃
     */
    short Dry = 19;

    /**
     * 清洁
     */
    short Clean = 20;

    /**
     * 除垢
     */
    short Descaling = 21;


    /**
     * 无模式
     */
    short NoModel = 255;


}
