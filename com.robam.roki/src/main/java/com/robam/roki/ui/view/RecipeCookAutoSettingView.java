package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robam.roki.R;
import com.robam.roki.ui.dialog.RecipeOvenSetDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by WZTCM on 2016/1/16.
 */
public class RecipeCookAutoSettingView extends FrameLayout {

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
    @InjectView(R.id.linSetMsg)
    LinearLayout linSetMsg;
    @InjectView(R.id.txtBtn)
    TextView txtBtn;
    @InjectView(R.id.txtStart)
    TextView txtStart;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.linPro)
    RelativeLayout linPro;

    public RecipeCookAutoSettingView(Context context) {
        super(context);
        init(context, null);
    }

    public RecipeCookAutoSettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecipeCookAutoSettingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_recipe_cook_auto_setting, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        initViews();
    }

    private void initViews() {

    }

    @OnClick(R.id.txtBtn)
    public void onClickAuto() {
        RecipeOvenSetDialog.show(getContext());
        txtBtn.setVisibility(View.GONE);
        txtStart.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.txtStart)
    public void onClickStart() {
        txtStart.setVisibility(View.GONE);
        linPro.setVisibility(View.VISIBLE);
        initProgressBar();
    }

    private void initProgressBar() {

    }
}
