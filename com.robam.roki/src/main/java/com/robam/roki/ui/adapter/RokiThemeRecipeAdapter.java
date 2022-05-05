package com.robam.roki.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.media.tv.TvContentRating;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.pojos.ThemeRecipeDetail;
import com.robam.common.pojos.ThemeRecipeList;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.ThemeRecipeMultipleItem;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.RecipeUtil;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.ui.page.SelectThemeDetailPage;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class RokiThemeRecipeAdapter extends RecyclerView.Adapter {
    private static final String TAG = "RokiThemeRecipeAdapter";
    private Context context;
    private List<ThemeRecipeMultipleItem> themeRecipeMultipleItemList;


    private static RequestOptions maskOption = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.icon_recipe_default) //预加载图片
            .error(R.mipmap.icon_recipe_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
            .transform(new MaskTransformation(R.mipmap.icon_roki_recipe_bg)); //圆角

    private MultiTransformation multiTop = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(30, 10, RoundedCornersTransformation.CornerType.ALL));


    public RokiThemeRecipeAdapter(Context context, final List<ThemeRecipeMultipleItem> themeRecipeMultipleItemList) {
        this.context = context;
        this.themeRecipeMultipleItemList = themeRecipeMultipleItemList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case ThemeRecipeMultipleItem.IMG_RECIPE_MSG_TEXT:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_item_card, viewGroup, false);
                holder = new RecipeViewHolder(view);
                break;
            case ThemeRecipeMultipleItem.IMG_THEME_RECIPE_MSG_TEXT:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_recipe_thome, viewGroup, false);
                holder = new ThemeRecipeViewHolder(view);
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ThemeRecipeMultipleItem themeRecipeMultipleItem = themeRecipeMultipleItemList.get(position);
        if (themeRecipeMultipleItem == null) {
            return;
        }

        int itemViewType = themeRecipeMultipleItem.getItemType();
        switch (itemViewType) {
            case ThemeRecipeMultipleItem.IMG_RECIPE_MSG_TEXT:
                final Recipe recipe = themeRecipeMultipleItem.getRecipe();
                RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;
                recipeViewHolder.tvRecipeName.setText(recipe.name);
                recipeViewHolder.tvCollectionNumber.setText("阅读 " + NumberUtil.converString(recipe.collectCount));
                recipeViewHolder.tvRecipeReadNumber.setText("收藏 " + NumberUtil.converString(recipe.viewCount));
                GlideApp.with(context)
                        .load(RecipeUtils.getRecipeImgUrl(recipe))
                        .apply(maskOption)
                        .into(((RecipeViewHolder) holder).ivTagRecipe);
                recipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecipeDetailPage.show(recipe.id, recipe.sourceType);
                    }
                });

                break;
            case ThemeRecipeMultipleItem.IMG_THEME_RECIPE_MSG_TEXT:
                final ThemeRecipeList themeRecipeList = themeRecipeMultipleItem.getThemeRecipeList();
                final RecipeTheme recipeTheme = themeRecipeList.getRecipeTheme();
                ThemeRecipeViewHolder themeRecipeViewHolder = (ThemeRecipeViewHolder) holder;
                themeRecipeViewHolder.tvThemeTitle.setText(recipeTheme.name);
                themeRecipeViewHolder.tvThemeSubName.setText(recipeTheme.subName);

                showThemeImg(recipeTheme, themeRecipeList, themeRecipeViewHolder);


                themeRecipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SelectThemeDetailPage.show(recipeTheme, SelectThemeDetailPage.TYPE_THEME_RECIPE);
                    }
                });
                break;
            default:
                break;
        }


    }

    private void showThemeImg(final RecipeTheme recipeTheme, ThemeRecipeList themeRecipeList, ThemeRecipeViewHolder themeRecipeViewHolder) {
        List<Recipe> recipeList = themeRecipeList.getRecipeList();
        themeRecipeViewHolder.rvRecipePicList.setLayoutManager(new GridLayoutManager(context, 2));
        RecipePicListAdapter recipePicListAdapter = new RecipePicListAdapter(recipeList);
        recipePicListAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectThemeDetailPage.show(recipeTheme, SelectThemeDetailPage.TYPE_THEME_RECIPE);
            }
        });
        themeRecipeViewHolder.rvRecipePicList.setAdapter(recipePicListAdapter);

    }

    @Override
    public int getItemCount() {
        return themeRecipeMultipleItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (themeRecipeMultipleItemList.isEmpty()) {
            return 0;
        }
        int itemType = themeRecipeMultipleItemList.get(position).getItemType();
        return itemType;

    }


    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivTagRecipe;

        public TextView tvRecipeName;

        public TextView tvRecipeReadNumber;

        public TextView tvCollectionNumber;

        RecipeViewHolder(View itemView) {
            super(itemView);
            tvRecipeName = itemView.findViewById(R.id.tv_recipe_name);
            ivTagRecipe = itemView.findViewById(R.id.iv_tag_recipe);
            tvRecipeReadNumber = itemView.findViewById(R.id.tv_recipe_read_number);
            tvCollectionNumber = itemView.findViewById(R.id.tv_collection_number);
        }
    }

    /**
     * 精选主题菜谱
     */
    static class ThemeRecipeViewHolder extends RecyclerView.ViewHolder {

        public TextView tvThemeTitle;
        public TextView tvThemeSubName;
        public RecyclerView rvRecipePicList;

        ThemeRecipeViewHolder(View itemView) {
            super(itemView);
            tvThemeTitle = itemView.findViewById(R.id.tv_theme_title);
            tvThemeSubName = itemView.findViewById(R.id.tv_theme_subname);
            rvRecipePicList = itemView.findViewById(R.id.rv_recipe_pic_list);
        }
    }


}
