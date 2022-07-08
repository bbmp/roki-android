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
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.img_default) //预加载图片
            .error(R.mipmap.img_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存


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
                    .apply(options)
                    .into(ivTagRecipe);
        }
    }
}
