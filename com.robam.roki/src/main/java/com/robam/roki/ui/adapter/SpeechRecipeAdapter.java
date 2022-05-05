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
import com.legent.utils.LogUtils;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.page.RecipeDetailPage;

import java.util.List;

import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class SpeechRecipeAdapter extends RecyclerView.Adapter<SpeechRecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipeList;
    private Context context;
    private RecipeViewHolder mRecipeViewHolder;
    private static final String TAG = "SpeechRecipeAdapter";


    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.img_default) //预加载图片
            .error(R.mipmap.img_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
            .transform(new RoundedCornersTransformation(50, 0)); //圆角


    public SpeechRecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_recipe_list, parent, false);
        mRecipeViewHolder = new RecipeViewHolder(itemView);
        return mRecipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, final int position) {

        String recipeName = recipeList.get(position).name;
        mRecipeViewHolder.tvSearchRecipeResult.setText(recipeName);
        LogUtils.i(TAG, "recipeName:" + recipeName);
        String recipeUrl = RecipeUtils.getRecipeImgUrl(recipeList.get(position));
        GlideApp.with(context)
                .load(recipeUrl)
                .apply(options)
                .into(mRecipeViewHolder.ivSearchRecipeResult);

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
        ImageView ivSearchRecipeResult;
        TextView tvSearchRecipeResult;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ivSearchRecipeResult = itemView.findViewById(R.id.iv_search_recipe_result);
            tvSearchRecipeResult = itemView.findViewById(R.id.tv_search_recipe_result);

        }
    }

}
