package com.robam.roki.ui.form;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.legent.plat.events.PageBackEvent;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.Recipe;
import com.robam.roki.ui.FormKey;

/**
 * Created by Dell on 2018/8/31.
 */

public class RecipePotActivity extends BaseRokiActivity{
    static public void start(Activity atv, Recipe recipe, Bundle bd, String guid) {
      /*  atv.startActivity(new Intent(atv, RecipeActivity.class).putExtra("RecipeNew",recipe)
        .putExtra("DeviceDt",dt)
        );*/
        LogUtils.i("20171207","ID:"+recipe.getID());
        atv.startActivity(new Intent(atv, RecipePotActivity.class).putExtra("RecipeID",recipe.getID())
                .putExtras(bd) .putExtra("Guid",guid)
        );

        // atv.finish();
    }

    @Override
    protected String createFormKey() {
        return FormKey.RecipePotForm;
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
