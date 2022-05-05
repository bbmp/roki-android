package com.robam.roki.ui.form;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.NonNull;

import com.legent.plat.events.PageBackEvent;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.Recipe;
import com.robam.roki.R;
import com.robam.roki.ui.FormKey;

/**
 * Created by yinwei on 2017/11/27.
 */

public class RecipeNoDeviceActivity extends BaseRokiActivity {

    static public void start(Activity atv, Recipe recipe, Bundle bd, String guid) {
        atv.startActivity(new Intent(atv, RecipeNoDeviceActivity.class)
                .putExtra("RecipeID",recipe.getID())
                .putExtras(bd)
                .putExtra("Guid",guid)
        );

    }
    static public void start(Activity atv, Recipe recipe, Bundle bd, String guid , int step) {
        atv.startActivity(new Intent(atv, RecipeNoDeviceActivity.class)
                .putExtra("RecipeID",recipe.getID())
                .putExtras(bd)
                .putExtra("Guid",guid)
                .putExtra("step",step)
        );

    }

    static public void start(Activity atv, long id, Bundle bd, String guid , int step , String imgLarge ) {
        atv.startActivity(new Intent(atv, RecipeNoDeviceActivity.class)
                .putExtra("RecipeID",id)
                .putExtras(bd)
                .putExtra("Guid",guid)
                .putExtra("step",step)
                .putExtra("is_vh_switching" , true )
                .putExtra("imgLarge" , imgLarge )
        );

    }


    @Override
    protected String createFormKey() {
        return FormKey.RecipeCookNoDeviceForm;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iRokiDialogAlarmType_01=null;
        iRokiDialogAlarmType_02=null;
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
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return true ;
//    }
}
