package com.robam.common.recipe.step.inter.callInterface;

import com.legent.plat.pojos.device.IDevice;
import com.robam.common.recipe.step.inter.stepInterface.IRecipeStep;

import java.util.Map;

/**
 * Created by as on 2017-11-15.
 */

public interface IStepCallback {
    void onstart(Map<String, Object> map, IRecipeStep recipeStep);

    void onprecheck(Throwable t);

    void onrunning(IDevice device);

    void onSet(Throwable t);

    void onpolling(int count);

    void onDisconnect();

    void onConnect();

    void onException(int n);

    void onPause(Throwable t);

    void onRestore(Throwable t);

    void onComplete(int param);

    void onRefresh();

    void onClose(int error);


}
