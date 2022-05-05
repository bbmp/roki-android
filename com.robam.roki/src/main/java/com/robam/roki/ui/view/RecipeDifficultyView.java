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

public class RecipeDifficultyView extends FrameLayout {


    @InjectViews({R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item5})
    List<ImageView> items;

    int value;

    public RecipeDifficultyView(Context context) {
        super(context);
        init(context, null);
    }

    public RecipeDifficultyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecipeDifficultyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_recipe_difficulty,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    public void setDifficulty(int value) {
        this.value = value;
        for (View view : items) {
            view.setSelected(false);
        }
        for (int i = 0; i < value && i < items.size(); i++) {
            items.get(i).setSelected(true);
        }
    }
}
