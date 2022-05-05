package com.robam.roki.service;

import android.os.Bundle;

import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.utils.speech.SpeechManager;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.services.RuleCookTaskService;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

/**
 * Created by Dell on 2018/5/22.
 */

public class MobPotCooktaskService extends RuleCookTaskService {


    @Override
    public void start(Stove.StoveHead stoveHead, Recipe book, String str) {

    }

    static private MobPotCooktaskService instance = new MobPotCooktaskService();

    static public MobPotCooktaskService getInstance() {
        if (instance == null) {
            synchronized (MobPotCooktaskService.class) {
                if (instance == null) {
                    instance = new MobPotCooktaskService();
                }
            }
        }
        return instance;
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onShowCookingView() {
        Bundle bd = new Bundle();
        bd.putSerializable(PageArgumentKey.BookId, recipeId);
        bd.putInt(PageArgumentKey.RecipeStepIndex, stepIndex);
        UIService.getInstance().postPage(PageKey.RecipeCooking, bd);
    }

    protected MobPotCooktaskService() {
        SpeechManager.getInstance().init(Plat.app);
    }


    @Override
    public void dispose() {
        super.dispose();
        SpeechManager.getInstance().dispose();
    }
}
