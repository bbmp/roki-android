package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by WZTCM on 2016/1/16.
 */
public class RecipeCookNormalView extends FrameLayout {

    @InjectView(R.id.txtStepIndex)
    TextView txtStepIndex;
    @InjectView(R.id.txtStepCount)
    TextView txtStepCount;
    @InjectView(R.id.divStepIndex)
    LinearLayout divStepIndex;
    @InjectView(R.id.txtStepIntro)
    TextView txtStepIntro;
    @InjectView(R.id.img)
    ImageView img;

    private Context context;

    public RecipeCookNormalView(Context context) {
        super(context);
        init(context, null);
    }

    public RecipeCookNormalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecipeCookNormalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_recipe_cook_normal, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

}
