package com.robam.common.recipe.step.inter.stepInterface;

/**
 * Created by as on 2017-11-30.
 */

public interface IRecipeStep {


    void run(String id);

    void closeservice();

    void refreshInit();

    void pause();

    void restore();
}
