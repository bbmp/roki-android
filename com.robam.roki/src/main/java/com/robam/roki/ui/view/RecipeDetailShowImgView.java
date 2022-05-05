package com.robam.roki.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.legent.plat.pojos.device.IDevice;
import com.legent.utils.LogUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.robam.roki.R.id.recipe_step_pic;

/**
 * Created by yinwei on 2017/11/29.
 */

public class RecipeDetailShowImgView extends FrameLayout {
    @InjectView(recipe_step_pic)
    ImageView recipeStepPic;


    Context cx;
    Recipe recipe;
    int step;
    public CookStep cookStep;//菜谱步骤对象
    public List<String> cookSteps;
    IDevice iDevice;
    String imageUrl ;
    private MultiTransformation options = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(30, 0));
    public RecipeDetailShowImgView(Context context, List<String> cookSteps, int step, IDevice iDevice) {
        super(context);
        this.cx = context;
        this.cookSteps=cookSteps;
        this.step = step;
        this.iDevice = iDevice;
        init(context);
    }
    public RecipeDetailShowImgView(Context context, String imageUrl) {
        super(context);
        this.cx = context;
        this.imageUrl = imageUrl ;
        init(context);
    }
    private void init(Context cx){
        View view = LayoutInflater.from(cx).inflate(R.layout.recipe_show_img_view, this, true);
        ButterKnife.inject(this, view);
        onRefresh();
    }

    public void onRefresh(){
        LogUtils.i("20171129","onfresh");
       // ImageUtils.displayImage(cookSteps.get(step), recipeStepPic);
//        Glide.with(cx).load(cookSteps.get(step)).centerCrop().into(recipeStepPic);
        if (imageUrl != null){
            GlideApp.with(cx)
                    .load(imageUrl)
                    .apply(RequestOptions.bitmapTransform(options))
                    .into(recipeStepPic);
        }else {
            GlideApp.with(cx)
                    .load(cookSteps.get(step))
                    .apply(RequestOptions.bitmapTransform(options))
                    .into(recipeStepPic);
        }

        /*RecipeParamShowView recipeParamShowView =new RecipeParamShowView(cx,cookStep,iDevice);
        paramShow.addView(recipeParamShowView);*/
    }

}
