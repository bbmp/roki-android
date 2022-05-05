package com.robam.roki.ui.adapter3;

import android.widget.ImageView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;

import org.jetbrains.annotations.NotNull;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author r210190
 * des 专题列表土图片adapter
 */
public class RvThemePicAdapter extends BaseQuickAdapter<Recipe, BaseViewHolder> {
    private MultiTransformation options = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(30, 0));

    public RvThemePicAdapter() {
        super(R.layout.item_theme_recipe_list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Recipe item) {
        if (item != null) {
            ImageView iv_theme_item_img = (ImageView) holder.getView(R.id.iv_theme_recipe_pic);
            GlideApp.with(getContext())
                    .load(RecipeUtils.getRecipeImgUrl(item))
                    .apply(RequestOptions.bitmapTransform(options))
                    .into(iv_theme_item_img);
        }
    }
}
