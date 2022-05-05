package com.robam.roki.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.utils.UiUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class RecipeTopicAdapter extends BaseMultiItemQuickAdapter<Recipe, BaseViewHolder> {

    RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.icon_recipe_default) //预加载图片
            .error(R.mipmap.icon_recipe_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) //缓存
            .transform(new RoundedCornersTransformation(30, 0,RoundedCornersTransformation.CornerType.TOP)); //圆角

    private Integer weekTopRanking[] = {R.mipmap.icon_week_top1, R.mipmap.icon_week_top2,
            R.mipmap.icon_week_top3, R.mipmap.icon_week_top4,
            R.mipmap.icon_week_top5, R.mipmap.icon_week_top6,
            R.mipmap.icon_week_top7, R.mipmap.icon_week_top8,
            R.mipmap.icon_week_top9, R.mipmap.icon_week_top10
    };


    public RecipeTopicAdapter(@Nullable List<Recipe> data) {
        super(data);
        addItemType(Recipe.IMG, R.layout.item_last_week_topic);
        addItemType(Recipe.TEXT, R.layout.item_more_footer);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Recipe recipe) {
        switch (baseViewHolder.getItemViewType()) {
            case Recipe.IMG:
                ImageView ivItemTopic = baseViewHolder.itemView.findViewById(R.id.iv_item_topic_img);
                TextView recipeName = baseViewHolder.itemView.findViewById(R.id.tv_recipe_name);
                TextView recipeReadNumber = baseViewHolder.itemView.findViewById(R.id.tv_recipe_read_number);
                ImageView ivWeekTopicRanking = baseViewHolder.itemView.findViewById(R.id.iv_week_topic_ranking);
                recipeName.setText(recipe.name);
                recipeReadNumber.setText("上周 " + NumberUtil.converString(recipe.viewCount));


//                Drawable drawable1 = UiUtils.getResources().getDrawable(R.drawable.icon_hot, null);
//                drawable1.setBounds(0, 0, 35, 35);
//                recipeReadNumber.setCompoundDrawables(drawable1, null, null, null);
                GlideApp.with(getContext())
                        .load(RecipeUtils.getRecipeImgUrl(recipe))
                        .apply(options)
                        .into(ivItemTopic);
                GlideApp.with(getContext())
                        .load(weekTopRanking[baseViewHolder.getBindingAdapterPosition()])
                        .into(ivWeekTopicRanking);
                break;
            case Recipe.TEXT:
                break;
            default:
                break;
        }
    }

}
