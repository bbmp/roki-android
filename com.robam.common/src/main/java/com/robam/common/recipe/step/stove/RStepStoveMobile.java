package com.robam.common.recipe.step.stove;

import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.recipe.step.inter.callback.IStepCallback;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by as on 2017-11-08.
 */

public class RStepStoveMobile extends RStepStove {
    public RStepStoveMobile(int cookIns, ArrayList<CookStep> cookSteps, int stepIndex, IStepCallback callback) {
        super(cookIns, cookSteps, stepIndex, callback);
    }

    @Override
    protected void closeDevice() {

    }

    @Override
    protected Map<String, Object> prerun() {
        return null;
    }
}
