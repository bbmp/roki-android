package com.robam.common.recipe.step.stove;

import com.legent.utils.StringUtils;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.recipe.inter.IRecipe;
import com.robam.common.recipe.step.inter.callback.IStepCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.robam.common.Utils.getDefaultStove;

/**
 * Created by as on 2017-11-08.
 */

public class RStepStovePad extends RStepStove {

    public RStepStovePad(int cookIns, ArrayList<CookStep> cookSteps, int stepIndex, IStepCallback callback) {
        super(cookIns, cookSteps, stepIndex, callback);
    }

    @Override
    protected Map<String, Object> prerun() {
        if (mPreRunMap == null)
            mPreRunMap = new HashMap<String, Object>();
        mPreRunMap.clear();

        if (StringUtils.isNullOrEmpty(mCookStep.getDc())
                || mCookStep.getjs_PlatformCodes() == null
                || mCookStep.getjs_PlatformCodes().size() <= 0) {
            mPreRunMap.put(IRecipe.RECIPE_STEP_DC, false);
            return mPreRunMap;
        }

        Stove stoves = getDefaultStove();
        if (stoves == null) {
            mPreRunMap.put(IRecipe.RECIPE_STEP_DC, true);
            mPreRunMap.put(IRecipe.DEVICE_IFHAS, false);
            return mPreRunMap;
        }

        mPreRunMap.put(IRecipe.RECIPE_STEP_DC, true);
        mPreRunMap.put(IRecipe.DEVICE_IFHAS, true);
        if (ifContainDevice(stoves.getID())) {
            List list = new ArrayList<String>();
            list.add(stoves.getID());
            mPreRunMap.put(IRecipe.DEVICE_OCCUPY, list);
            return mPreRunMap;
        } else {
            List list = new ArrayList<String>();
            list.add(stoves.getID());
            mPreRunMap.put(IRecipe.DEVICE_AVAILB, list);
            return mPreRunMap;
        }
    }


}
