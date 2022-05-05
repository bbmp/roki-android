package com.robam.common.pojos.device.fan;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.robam.common.pojos.FanStatusComposite;
import com.robam.common.pojos.device.IPauseable;
import com.robam.common.pojos.device.SmartParams;

/**
 * Created by zhaiyuanyi on 15/10/19.
 */
public interface IFan extends IPauseable {

   /* *//**
     * model of fan
     *//*
    String getFanModel();*/

    /**
     * 读取油烟机状态
     *
     * @param callback
     */
    void getFanStatus(VoidCallback callback);

    /**
     * 设置油烟机工作状态
     *
     * @param status   0-关机，1-开机
     * @param callback
     */
    void setFanStatus(short status, VoidCallback callback);

    /**
     * 设置油烟机档位
     *
     * @param level    0、1、2、3、6档
     * @param callback
     */
    void setFanLevel(short level, VoidCallback callback);

    /**
     * 设置油烟机灯的状态
     *
     * @param light
     * @param callback
     */
    void setFanLight(boolean light, VoidCallback callback);

    /**
     * 设置油烟机整体状态
     *
     * @param level
     * @param light
     * @param callback
     */
    void setFanAllParams(short level, boolean light, VoidCallback callback);

    /**
     * 重置烟机清洗计时
     *
     * @param callback
     */
    void restFanCleanTime(VoidCallback callback);

    /**
     * 重启油烟机网络板
     *
     * @param callback
     */
    void RestFanNetBoard(VoidCallback callback);

    /**
     * 设置油烟机定时工作
     *
     * @param level
     * @param time     单位：分钟
     * @param callback
     */
    void setFanTimeWork(short level, short time, VoidCallback callback);


    /**
     * 读取智能互动模式设定
     *
     * @param callback
     */
    void getSmartConfig(Callback<SmartParams> callback);

    /**
     * 设置智能互动模式
     *
     * @param callback
     */
    void setSmartConfig(SmartParams smartParams, VoidCallback callback);


    void setRegularlyRemind(short regularSwitch, short remindTime, VoidCallback callback);


    /**
     * 设置智能烟感状态
     */
    void setFanSmartSmoke(FanStatusComposite fanStatusComposite, short argumentNumber,
                          VoidCallback callback);

    /**
     * 设置定时通风天数
     */
    void setFanTimingVentilationTime(FanStatusComposite fanStatusComposite
            , short argumentNumber, VoidCallback callback);

    /**
     * 设置烟机油网清洗提示开关
     */
    void setFanOilCleanHintSwitch(FanStatusComposite fanStatusComposite
            , short argumentNumber, VoidCallback callback);


    /**
     * 设置开启灶具烟机自动开启开关
     */
    void setPowerLinkageSwitch(FanStatusComposite fanStatusComposite, short argumentNumber,
                               VoidCallback callback);

    /**
     * 设置灶具烟机档位联动开关
     */
    void setLevelLinkageSwitch(FanStatusComposite fanStatusComposite, short argumentNumber,
                               VoidCallback callback);

    /**
     * 灶具关闭后烟机延时时间
     */
    void setShutdownLinkageTime(FanStatusComposite fanStatusComposite, short argumentNumber,
                                VoidCallback callback);

    /**
     * 灶具关闭后烟机延时开关
     */
    void setShutdownLinkageSwitch(FanStatusComposite fanStatusComposite, short argumentNumber,
                                  VoidCallback callback);
    /**
     * 设置礼拜几通风
     */
    void setFanWeekDay(FanStatusComposite fanStatusComposite, short argumentNumber,
                       VoidCallback callback);

    /**
     * 设置爆炒时间
     */
    void setFryStrongTime(FanStatusComposite fanStatusComposite,VoidCallback callback);

    /**
     * 设置倒油杯提示开关
     */
    void setCupOilPower(FanStatusComposite fanStatusComposite, VoidCallback callback);

    /**
     * 设置防干烧提示开关
     */
    void setProtectTipDryPower(FanStatusComposite fanStatusComposite, VoidCallback callback);

    /**
     * 设置防干烧开关
     */
    void setProtectDryPower(FanStatusComposite fanStatusComposite, VoidCallback callback);

    /**
     * 设置手势控制
     */
    void setGestureControl(FanStatusComposite fanStatusComposite,VoidCallback callback);

}
