package com.robam.common.recipe.step.inter.callInterface;

/**
 * Created by as on 2017-07-19.
 */

public interface IStepCallback_KZW extends IStepCallback {
    void onWarn(int n);

    void onWarnRecovery(int n);
}
