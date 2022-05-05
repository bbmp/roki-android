package com.robam.roki.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.robam.roki.R;

import butterknife.ButterKnife;

/**
 * Created by yinwei on 2017/12/14.
 */

public class RecipeCardView extends FrameLayout {
    Context cx;
    public RecipeCardView(Context context) {
        super(context);
        this.cx = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(cx).inflate(R.layout.recipe_card_view_show,this, true);
        ButterKnife.inject(this, view);
        initView();
    }

    private void initView(){

    }
}
