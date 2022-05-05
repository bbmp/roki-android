package com.robam.common.pojos.device.integratedStove;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.robam.common.pojos.device.IPauseable;
import com.robam.common.pojos.device.rika.RikaSmartParams;

/**
 * Created by 14807 on 2018/4/3.
 */

public interface IintegStove extends IPauseable {

    /**
     * 设置烟机开关机状态
     *
     * @param numberCategory    品类个数
     * @param categoryCode      品类编码
     * @param ArgumentNumber    参数个数
     * @param fanHeader_Key     烟机key
     * @param fanHeader_Leng    烟机Leng
     * @param rikaFanWorkStatus 烟机状态
     * @param callback          成功，失败回调
     */
    void setFanSwitchStatus(short numberCategory, short categoryCode, short ArgumentNumber, short fanHeader_Key,
                            short fanHeader_Leng, short rikaFanWorkStatus, VoidCallback callback);

    /**
     * 设置烟机关闭风量
     *
     * @param numberCategory
     * @param categoryCode
     * @param ArgumentNumber
     * @param fanHeader_Key
     * @param fanHeader_Leng
     * @param rikaFanPower
     * @param callback
     */
    void setCloseFanVolume(short numberCategory, short categoryCode, short ArgumentNumber, short fanHeader_Key,
                           short fanHeader_Leng, short rikaFanPower, VoidCallback callback);

    /**
     * 设置蒸汽炉工作状态
     *
     * @param numberCategory
     * @param categoryCode
     * @param argumentNumber
     * @param steamHeader_Key
     * @param steamHeader_Leng
     * @param steamWorkStatus
     * @param callback
     */
    void setSteamWorkStatus(short numberCategory, short categoryCode, short argumentNumber,
                            short steamHeader_Key, short steamHeader_Leng, short steamWorkStatus, VoidCallback callback);

    void setSteamRunWorkModel(short numberCategory, short categoryCode, short argumentNumber, short steamHeader_Key,
                              short steamHeader_Leng, short steamRunModel, short steamWorkTemp, short steamWorkTime, VoidCallback callback);


    /**
     * 读取智能互动模式设定
     *
     * @param callback
     */
    void getSmartConfig(Callback<IntegStoveSmartParams> callback);

    /**
     * 设置智能互动模式
     *
     * @param callback
     */
    void setSmartConfig(IntegStoveSmartParams smartParams, VoidCallback callback);

//    /**
//     * 设置一体机智能互动模式
//     *
//     * @param callback
//     */
//    void setSteamOvenSmartConfig(RikaSmartParams smartParams, VoidCallback callback);


    /**
     * 设置消毒柜工作状态
     *
     * @param cmd
     * @param numberCategory
     * @param categoryCode
     * @param argumentNumber
     * @param sterilizerHeader_Key
     * @param sterilizerHeader_Leng
     * @param sterilizerWorkStatus
     * @param sterilizerWorkTime
     * @param sterilizerWarmDishTemp
     * @param callback
     */
    void setSterilizerWorkStatus(short cmd, short numberCategory, short categoryCode, short argumentNumber,
                                 short sterilizerHeader_Key, short sterilizerHeader_Leng, short sterilizerWorkStatus,
                                 short sterilizerWorkTime, short sterilOrderTime, short sterilizerWarmDishTemp, VoidCallback callback);




    /**
     * 设置消毒柜童锁状态
     *
     * @param cmd
     * @param numberCategory
     * @param categoryCode
     * @param argumentNumber
     * @param sterilizerHeader_Key
     * @param sterilizerHeader_Leng
     * @param callback
     */
    void setSterilizerLockStatus(short cmd, short numberCategory, short categoryCode, short argumentNumber,
                                 short sterilizerHeader_Key, short sterilizerHeader_Leng,
                                 short sterilizerLockStatus, VoidCallback callback);


    /**
     *
     * @param cmd
     * @param numberCategory
     * @param categoryCode
     * @param argumentNumber
     * @param steamOvenModelOne
     * @param steamOvenTempOne
     * @param steamOvenTimeOne
     * @param steamOvenModelTwo
     * @param steamOvenTempTwo
     * @param steamOvenTimeTwo
     * @param callback
     */
    void setSteamOvenMultiStepCooking(short cmd, short numberCategory, short categoryCode, short argumentNumber,
                                      short steamOvenModelOne, short steamOvenTempOne, short steamOvenTimeOne,
                                      short steamOvenModelTwo, short steamOvenTempTwo, short steamOvenTimeTwo, VoidCallback callback);


    /**
     *
     * @param cmd
     * @param numberCategory
     * @param categoryCode
     * @param argumentNumber
     * @param steamOvenHeader_Key
     * @param steamOvenHeader_Leng
     * @param steamOvenModel
     * @param stemOvenTime
     * @param voidCallback
     */
    void setSteamOvenAutoRecipeModel(short cmd, short numberCategory, short categoryCode, short argumentNumber,
                                     short steamOvenHeader_Key, short steamOvenHeader_Leng, short steamOvenModel, short stemOvenTime, VoidCallback voidCallback);

}

