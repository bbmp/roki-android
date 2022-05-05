package com.robam.common.recipe.step.inter.callback;

import com.legent.plat.pojos.device.IDevice;
import com.robam.common.recipe.step.inter.callto.IRecipeStep;

import java.util.Map;

/**
 * 步骤逻辑反馈做菜逻辑 通知
 * Created by as on 2017-07-14.
 */

public interface IStepCallback {
    /**
     * 运行前检查通知
     */
    void oncheck(Map<String, Object> map, IRecipeStep recipeStep);

    /**
     * 已开启运行通知 t 为 null表示开启成功
     */
    void onStart(Object error);

    /**
     * 正在轮训
     */
    void onPolling(IDevice device);

    /**
     * 已下发预设参数通知 null表示预设成功
     */
    void onSet(Throwable t);

    /**
     * 正在运行通知 回调 倒计时 count 倒计时秒数
     */
    void onRunning(int count);

    /**
     * 设备 断网通知
     */
    void onDisconnect(IDevice device);

    /**
     * 设备 连接通知
     */
    void onConnect();

    /**
     * 异常信息通知 此处异常事件影响做菜逻辑进程， n 异常号码 具体子类设计 n关闭失败（ 断网 断电...）
     */
    void onException(int n);


    /**
     * 暂停通知
     */
    void onPause(Throwable t);

    /**
     * 恢复通知
     */
    void onRestore(Throwable t);

    /**
     * 完成通知 0 关闭成功
     */
    void onComplete(int param);


    /**
     * 步骤重新初始化通知
     */
    void onRefresh();

    /**
     * 关闭并释放资源通知 error 0 正常关闭 1 关闭设备失败
     */
    void onClose(int error);


}
