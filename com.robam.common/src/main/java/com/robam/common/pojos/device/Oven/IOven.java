package com.robam.common.pojos.device.Oven;

import com.legent.VoidCallback;
import com.robam.common.pojos.device.IPauseable;

/**
 * Created by linxiaobin on 2015/12/27.
 */
public interface IOven extends IPauseable {

    /**
     * model of fan
     */

    void setOvenWorkTime(final short time, VoidCallback callback);

    void setOvenWorkTemp(final short temp, VoidCallback callback);

    String getOvenModel();

    /**
     * 状态控制
     *
     * @param status
     * @param callback
     */
    void setOvenStatusControl(short status, VoidCallback callback);


    /**
     * 设置烤箱快热
     *
     * @param temp
     * @param callback
     * @param time
     */
    void setOvenQuickHeating(short temp, short time, short preflag, VoidCallback callback);

    /**
     * 设置烤箱风焙烤
     *
     * @param time
     * @param temp
     * @param callback
     */
    void setOvenAirBaking(short time, short temp, short preflag, VoidCallback callback);


    /**
     * 设置烤箱焙烤
     *
     * @param time
     * @param temp
     * @param callback
     */
    void setOvenToast(short time, short temp, short preflag, VoidCallback callback);

    /**
     * 设置烤箱底加热
     *
     * @param callback
     */
    void setOvenBottomHeating(short time, short temp, short preflag, VoidCallback callback);

    /**
     * 设置烤箱解冻
     *
     * @param time
     * @param callback
     * @Param temp;
     */
    void setOvenUnfreeze(short time, short temp, short preflag, VoidCallback callback);

    /**
     * 设置烤箱风扇烤
     *
     * @param time
     * @param callback
     * @Param temp;
     */
    void setOvenAirBarbecue(short time, short temp, short preflag, VoidCallback callback);

    /**
     * 设置烤箱烧烤
     *
     * @param time
     * @param callback
     * @Param temp;
     */
    void setOvenBarbecue(short time, short temp, short preflag, VoidCallback callback);

    /**
     * 设置烤箱强烧烤
     *
     * @param time
     * @param callback
     * @Param temp;
     */
    void setOvenStrongBarbecue(short time, short temp, short preflag, VoidCallback callback);

    /**
     * 设设置烤箱烤叉旋转，灯光控制
     *
     * @param revolve
     * @param light
     * @param callback
     * @Param temp;
     */
    void setOvenSpitRotateLightControl(short revolve, short light, short preflag, VoidCallback callback);

    /**
     * 设置烤箱状态
     *
     * @param status
     * @param callback
     */
    void setOvenStatus(short status, VoidCallback callback);

    /**
     * 查询烤箱状态
     *
     * @param callback
     */
    void getOvenStatus(VoidCallback callback);

}

