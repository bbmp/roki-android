package com.robam.roki.ui.form;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.legent.plat.events.PageBackEvent;
import com.legent.ui.ext.BaseActivity;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.Recipe;
import com.robam.roki.ui.FormKey;

/**
 * Created by yinwei on 2017/12/21.
 */

public class RecipeRRQZActivity extends BaseRokiActivity{

    static public void start(Activity atv, Recipe recipe, Bundle bd, String guid) {
        atv.startActivity(new Intent(atv,
                RecipeRRQZActivity.class)
                .putExtra("RecipeID",recipe.getID())
                .putExtras(bd)
                .putExtra("Guid",guid)
        );
    }
    /**
     * 最小化后进入
     * @param atv
     * @param recipeId
     * @param bd
     * @param guid
     * @param step
     */
    static public void start(Activity atv, long recipeId, Bundle bd, String guid ,int step ,String imgLarge ) {
        atv.startActivity(new Intent(atv, RecipeRRQZActivity.class)
                .putExtra("RecipeID",recipeId)
                .putExtras(bd)
                .putExtra("Guid",guid)
                .putExtra("step",step)
                .putExtra("is_vh_switching" , true )
                .putExtra("imgLarge" , imgLarge )
        );
    }
    @Override
    protected String createFormKey() {
        return FormKey.RecipeRRQZForm;
    }

    @Override
    protected void onKeyDown_Back() {
        EventUtils.postEvent(new PageBackEvent("RecipeSpeakBack"));
    }
//    @Override
//    protected void showTipWhenExit() {
////        ToastUtils.showShort(R.string.exit_the_program);
//        EventUtils.postEvent(new PageBackEvent("RecipeSpeakBack"));
//
//    }
}
