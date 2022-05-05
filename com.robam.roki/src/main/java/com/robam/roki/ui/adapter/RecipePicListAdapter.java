package com.robam.roki.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.ThemeRecipeList;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.RecipeUtil;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.page.SelectThemeDetailPage;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class RecipePicListAdapter extends RecyclerView.Adapter {

    private List<Recipe> recipeList;
    private static final String TAG = "RecipePicListAdapter";
    private View.OnClickListener onClickListener;

    private MultiTransformation options = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(30, 0));


    public RecipePicListAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_theme_recipe_list, parent, false);
        RecyclerView.ViewHolder holder = new ThemeRecipePicViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {


        GlideApp.with(((ThemeRecipePicViewHolder) holder).itemView)
                .load(RecipeUtils.getRecipeImgUrl(recipeList.get(position)))
                .apply(RequestOptions.bitmapTransform(options))
                .into(((ThemeRecipePicViewHolder) holder).ivThemeRecipe);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onClickListener != null){
//                    onClickListener.onClick(v);
//                }
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (recipeList.size() < 4) {
            return recipeList.size();
        } else {
            return 4;
        }
    }

    /**
     * 精选专题菜谱图片列表
     */
    static class ThemeRecipePicViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivThemeRecipe;

        ThemeRecipePicViewHolder(View itemView) {
            super(itemView);
            ivThemeRecipe = itemView.findViewById(R.id.iv_theme_recipe_pic);
        }
    }


}
