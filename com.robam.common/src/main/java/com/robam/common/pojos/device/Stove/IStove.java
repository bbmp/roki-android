package com.robam.common.pojos.device.Stove;


import com.legent.VoidCallback;
import com.robam.common.pojos.device.IPauseable;

/**
 * Created by sylar on 15/7/24.
 */
public interface IStove extends IPauseable {

    /**
     * 灶具model查询：R9W70/R9B39/R9B37/R9B39
     */
    String getStoveModel();

    /**
     * 电磁灶状态查询
     *
     * @param callback
     */
    void getStoveStatus(VoidCallback callback);

    /**
     * 设置电磁灶状态
     *
     * @param isCook
     * @param ihId     0-左，1-右
     * @param ihStatus 0-关机，1-待机，2-工作中
     * @param callback
     */
    void setStoveStatus(boolean isCook, short ihId, short ihStatus,
                        VoidCallback callback);

    /**
     * 设置电磁灶档位
     *
     * @param isCook
     * @param ihId
     * @param ihLevel  0~9
     * @param callback
     */
    void setStoveLevel(boolean isCook, short ihId, short ihLevel,
                       VoidCallback callback);

    /**
     * 设置电磁灶定时关机
     *
     * @param ihId
     * @param ihTime   0~6000 秒
     * @param callback
     */
    void setStoveShutdown(short ihId, short ihTime, VoidCallback callback);

    /**
     * 设置电磁灶童锁
     *
     * @param isLock
     * @param callback
     */
    void setStoveLock(boolean isLock, VoidCallback callback);
}
