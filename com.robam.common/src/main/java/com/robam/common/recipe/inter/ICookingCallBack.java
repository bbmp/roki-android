package com.robam.common.recipe.inter;

import com.legent.plat.pojos.device.IDevice;
import com.robam.common.recipe.AbsRecipeCookTask;

import java.util.Map;

/**
 * 做菜逻辑回调终端
 * Created by as on 2017-07-18.
 */

public interface ICookingCallBack {
    /**
     * 当前步骤号
     */
    void onCurrentIndexStart(int index);

    /**
     * 展示性页面
     */
    void ondisplay(int stepIndex, String dc);

    /**
     * 运行前检查通知
     */
    void onPreStartCheck(Map<String, Object> map, int stepIndex, AbsRecipeCookTask.RecipeDeviceSelect callback3);

    /**
     * 已开启运行通知 t 为 null表示开启成功
     */
    void onStart(Object t);

    /**
     * 已下发预设参数通知 null表示预设成功
     */
    void onPreSet(Throwable t, AbsRecipeCookTask.RecipeDeviceSelect callback3);

    /**
     * 正在运行通知 回调 倒计时 count 倒计时秒数
     */
    void onPolling(IDevice device);

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
     * 异常信息通知 此处异常事件影响做菜逻辑进程， n 异常号码 具体子类设计
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
     * 完成通知 0 关闭成功 1关闭失败（离线 断网 断电...）
     */
    void onComplete(int param);

    /**
     * 报警
     *
     * @param n 报警id
     */
    void onWarn(int n);

    /**
     * 报警恢复
     *
     * @param n 报警恢复id
     */
    void onWarnRecovery(int n);

    /**
     * 步骤重新初始化通知
     */
    void onRefresh();

    /**
     * 关闭并释放资源通知
     */
    void onClose(int error);
}
