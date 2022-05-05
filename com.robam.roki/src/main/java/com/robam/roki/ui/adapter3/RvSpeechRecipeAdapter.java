package com.robam.roki.ui.adapter3;

import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;

import org.jetbrains.annotations.NotNull;

import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author r210190
 * des 语音搜索菜谱成功 菜谱adapter
 */
public class RvSpeechRecipeAdapter extends BaseQuickAdapter<Recipe , BaseViewHolder> {
    private MultiTransformation options = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(30, 0));


    public RvSpeechRecipeAdapter() {
        super(R.layout.item_search_recipe_list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Recipe item) {
        if (item != null){
            holder.setText(R.id.tv_search_recipe_result , item.name);
            ImageView ivTagRecipe = (ImageView) holder.getView(R.id.iv_search_recipe_result);
            String recipeUrl = RecipeUtils.getRecipeImgUrl(item);
            GlideApp.with(getContext())
                    .load(recipeUrl)
                    .apply(RequestOptions.bitmapTransform(options))
                    .into(ivTagRecipe);
        }
    }
}
