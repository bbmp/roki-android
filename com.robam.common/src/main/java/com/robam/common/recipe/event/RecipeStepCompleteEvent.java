package com.robam.common.recipe.event;

/**
 * 菜谱步骤完成事件
 * Created by as on 2017-07-14.
 */

public class RecipeStepCompleteEvent {
    public int mStepIdnex;//菜谱完成步骤好
    public int mRecipeInsIndex;//菜谱完成实例号

    public RecipeStepCompleteEvent(int recipeindex, int stepindex) {
        this.mRecipeInsIndex = recipeindex;
        this.mStepIdnex = stepindex;

    }
}
