package com.robam.roki.ui.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.legent.utils.api.DisplayUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.CookStepTip;
import com.robam.common.pojos.CookStepTipMaterial;
import com.robam.roki.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecipeCookingMaterialsView extends FrameLayout {

    @InjectView(R.id.layout)
    HorizontalScrollView layout;
    @InjectView(R.id.divMain)
    LinearLayout divMain;

    List<ViewHolder> holders = Lists.newArrayList();


    public RecipeCookingMaterialsView(Context context) {
        super(context);
        init(context, null);
    }

    public RecipeCookingMaterialsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecipeCookingMaterialsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_recipe_cooking_materials,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    public void loadData(CookStep step) {
        holders.clear();
        divMain.removeAllViews();
        if (step == null) return;

        List<CookStepTip> tips = step.getJs_tips();
        if (tips == null || tips.size() == 0) return;

        Context cx = getContext();

        View view;
        ViewHolder vh;
        LayoutInflater inflater = LayoutInflater.from(cx);

        for (CookStepTip tip : tips) {
            List<CookStepTipMaterial> items = tip.getTipMaterials();
            if (items == null || items.size() == 0) continue;
            for (CookStepTipMaterial ctm : items) {
                view = inflater.inflate(R.layout.view_recipe_cooking_materials_item, this, false);
                vh = new ViewHolder(view, tip, ctm);
                view.setTag(vh);
                view.setVisibility(View.GONE);

                holders.add(vh);
                divMain.addView(view);
            }
        }

        for (int i = 1; i < holders.size(); i++) {
            ((LinearLayout.LayoutParams) holders.get(i).view.getLayoutParams()).leftMargin = DisplayUtils.dip2px(getContext(), 15f);
        }

    }

    public void onTipTime(int tipTime) {
        List<ViewHolder> list = matchHolders(tipTime);
        if (list == null || list.size() == 0) return;

        for (ViewHolder holder : list) {
            scrollToView(holder.view);
            startAnim(holder.view);
        }
    }

    void scrollToView(final View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                layout.scrollTo(view.getLeft(), 0);
            }
        }, 5);
    }

    void startAnim(final View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(360);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setRotationY((Float) animation
                        .getAnimatedValue());
                view.setVisibility(View.VISIBLE);

            }
        });

        animatorSet.play(valueAnimator).with(
                ObjectAnimator.ofFloat(view, "RotationY", 360)
                        .setDuration(2000));
        animatorSet.start();
    }

    List<ViewHolder> matchHolders(int tipTime) {
        List<ViewHolder> list = Lists.newArrayList();
        for (ViewHolder holder : holders) {
            if (holder.isMatchTipTime(tipTime)) {
                list.add(holder);
            }
        }
        return list;
    }


    class ViewHolder {
        @InjectView(R.id.img)
        ImageView img;
        @InjectView(R.id.edtName)
        TextView txtName;
        @InjectView(R.id.txtValue)
        TextView txtValue;

        View view;
        CookStepTip tip;

        ViewHolder(View view, CookStepTip tip, CookStepTipMaterial material) {
            this.view = view;
            this.tip = tip;
            ButterKnife.inject(this, view);

            txtName.setText(material.name);
            txtValue.setText(String.format("%s%s", material.weight, material.unit));

            img.setImageDrawable(null);
            ImageUtils.displayImage(getContext(), material.imgUrl, img);
        }

        boolean isMatchTipTime(int tipTime) {
            return tip.time == tipTime;
        }
    }
}
