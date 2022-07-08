package com.robam.roki.ui.activity3.recipedetail;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.legent.ui.UIService;
import com.legent.utils.api.SoftInputUtils;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.AppActivity;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;

public class RecipeSearchActivity extends AppActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_recipe_search;
    }

    @Override
    protected void initView() {
//        getContentView().findViewById(id);
        findViewById(R.id.recipe_search_frg).findViewById(R.id.title_sear).findViewById(R.id.search_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }


}