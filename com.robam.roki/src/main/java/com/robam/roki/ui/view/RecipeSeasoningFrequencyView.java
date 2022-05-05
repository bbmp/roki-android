package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.robam.roki.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectViews;

public class RecipeSeasoningFrequencyView extends FrameLayout {

    @InjectViews({R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item5})
    List<ImageView> items;

    public RecipeSeasoningFrequencyView(Context context) {
        super(context);
        init(context, null);
    }

    public RecipeSeasoningFrequencyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecipeSeasoningFrequencyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_recipe_seasoning_frequency,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    public void setValue(int value) {
        value = value < 0 ? 0 : value;
        value = value > 10 ? 10 : value;

        for (ImageView img : items) {
            img.setImageResource(R.mipmap.ic_recipe_seasoning_fire_normal);
        }


        int n = value / 2;
        for (int i = 0; i < Math.min(items.size(), n); i++) {
            items.get(i).setImageResource(R.mipmap.ic_recipe_seasoning_fire_selected);
        }

        int m = value % 2;
        if (n < items.size() && m > 0) {
            items.get(n).setImageResource(R.mipmap.ic_recipe_seasoning_fire_half);
        }
    }

}
