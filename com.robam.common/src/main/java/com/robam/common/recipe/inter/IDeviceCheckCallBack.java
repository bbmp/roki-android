package com.robam.common.recipe.inter;

import com.robam.common.recipe.AbsRecipeCookTask;

import java.util.Map;

/**
 * Created by Dell on 2018/4/2.
 */

public interface IDeviceCheckCallBack {
    /**
     * 运行前检查通知
     */
    void onPreStartCheck(Map<String, Object> map, int stepIndex, AbsRecipeCookTask.RecipeDeviceSelect callback3);
}
