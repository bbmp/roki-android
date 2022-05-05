package com.robam.common.recipe;

import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.recipe.inter.IAbsRecipeCookInterface;
import com.robam.common.recipe.inter.ICookingCallBack;

import java.util.ArrayList;

/**
 * Created by as on 2017-07-12.
 */

public class PadRecipeCookTaskService extends RecipeCookTaskService {

    private PadRecipeCookTaskService(ArrayList<CookStep> cookSteps, ICookingCallBack callBack) {
        super(cookSteps, callBack);
    }

    /**
     * pad 菜谱实例生成器
     */
    public static class CookTaskFactory {
        public static IAbsRecipeCookInterface newCookTaskInstance(ArrayList<CookStep> cookSteps, ICookingCallBack cb) throws Exception{
            if (getCookTaskLIst().size() < getMaxCookinstance()) {
                synchronized (CookTaskFactory.class) {//进入锁后，需要再次判断 排除new对象影响
                    //if (getCookTaskLIst().size() < getMaxCookinstance())
                        return new PadRecipeCookTaskService(cookSteps, cb);
                    /*else
                        return null;*/
                }
            } else
                return null;
        }
    }
}
