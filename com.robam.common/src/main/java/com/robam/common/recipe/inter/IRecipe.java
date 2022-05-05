package com.robam.common.recipe.inter;

/**
 * Created by as on 2017-07-14.
 */

public interface IRecipe {
    /**
     * 设备占用 字段
     */
    String DEVICE_IFHAS = "DEVICE_IFHAS";//是否拥有设备
    String DEVICE_OCCUPY = "DEVICE_OCCUPY";//占用
    String DEVICE_AVAILB = "DEVICE_AVAILB";//可用

    /**
     * 步骤 字段
     */
    String RECIPE_STEP_DC = "RECIPE_STEP_DC";//步骤是否包含DC
}
