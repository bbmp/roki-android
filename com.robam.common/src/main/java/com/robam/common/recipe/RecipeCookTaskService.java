package com.robam.common.recipe;

import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.recipe.inter.ICookingCallBack;

import java.util.ArrayList;

/**
 * Created by as on 2017-07-12.  作为未来菜谱逻辑需求变更预留类
 */

public abstract class RecipeCookTaskService extends AbsRecipeCookTask {
    protected RecipeCookTaskService(ArrayList<CookStep> cookSteps, ICookingCallBack callBack) {
        super(cookSteps, callBack);
    }
}
