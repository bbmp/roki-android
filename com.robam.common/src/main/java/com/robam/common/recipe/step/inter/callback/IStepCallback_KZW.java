package com.robam.common.recipe.step.inter.callback;

/**
 * Created by as on 2017-07-19.
 */

public interface IStepCallback_KZW extends IStepCallback {
    /**
     * 报警通知 此处报警指的是实际影响 做菜进程的事件 n 报警id 具体子类设计
     */
    void onWarn(int n);

    /**
     * 报警恢复通知 n 报警恢复id
     */
    void onWarnRecovery(int n);
}
