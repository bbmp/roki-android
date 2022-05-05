package com.robam.roki.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.ui.page.TagRecipeFragment;

import java.util.List;

import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipeList;
    private Context context;
    private RecipeViewHolder mRecipeViewHolder;

    private static RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.icon_recipe_default) //预加载图片
            .error(R.mipmap.icon_recipe_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
            .transform(new MaskTransformation(R.mipmap.icon_roki_recipe_bg)); //圆角


    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card, parent, false);
        mRecipeViewHolder = new RecipeViewHolder(itemView);
        return mRecipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, final int position) {
        // do nothing
        int viewCount = recipeList.get(position).viewCount;
        int recipeCollectCount = recipeList.get(position).collectCount;
        String recipeUrl = RecipeUtils.getRecipeImgUrl(recipeList.get(position));
        String recipeName = recipeList.get(position).name;
        mRecipeViewHolder.recipeViewCount.setText("浏览 " + NumberUtil.converString(viewCount));
        GlideApp.with(context)
                .load(recipeUrl)
                .apply(options)
                .into(mRecipeViewHolder.ivTagRecipe);
        mRecipeViewHolder.recipeName.setText(recipeName);
        mRecipeViewHolder.recipeCollectCount.setText("收藏 " + NumberUtil.converString(recipeCollectCount));
        mRecipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeDetailPage.show(recipeList.get(position).id, recipeList.get(position).sourceType);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTagRecipe;
        TextView recipeName;
        TextView recipeCollectCount;
        TextView recipeViewCount;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ivTagRecipe = itemView.findViewById(R.id.iv_tag_recipe);
            recipeName = itemView.findViewById(R.id.tv_recipe_name);
            recipeViewCount = itemView.findViewById(R.id.tv_recipe_read_number);
            recipeCollectCount = itemView.findViewById(R.id.tv_collection_number);
        }
    }

}
