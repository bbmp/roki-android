package com.robam.roki.ui.adapter3;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PotMoreMenuAdapter extends RecyclerView.Adapter {

    private List<String> recipeList;
    private View.OnClickListener onClickListener;
    private View.OnClickListener onAddClickListener;

    private MultiTransformation options = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(30, 0));


    public PotMoreMenuAdapter(List<String> recipeList) {
        this.recipeList = recipeList;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public void setOnAddClickListener(View.OnClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_more_menu_list, parent, false);
        RecyclerView.ViewHolder holder = new PotMoreMenuViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


//        GlideApp.with(((PotMoreMenuViewHolder) holder).itemView)
//                .load(RecipeUtils.getRecipeImgUrl(recipeList.get(position)))
//                .apply(RequestOptions.bitmapTransform(options))
//                .into(((PotMoreMenuViewHolder) holder).ivThemeRecipe);

        GlideApp.with(((PotMoreMenuViewHolder) holder).itemView)
                .load(R.mipmap.img_chufangzhishi)
                .apply(RequestOptions.bitmapTransform(options))
                .into(((PotMoreMenuViewHolder) holder).ivMoreRecipe);
            if(position==2){
                ((PotMoreMenuViewHolder) holder).imgAddCancel.setImageResource(R.mipmap.icon_menu_added);
            }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(position);
                if (onClickListener != null){
                    onClickListener.onClick(v);
                }

            }
        });
        ((PotMoreMenuViewHolder) holder).imgAddCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(position);
                if (onAddClickListener != null){
                    onAddClickListener.onClick(v);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class PotMoreMenuViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivMoreRecipe;
        public ImageView imgAddCancel;
        PotMoreMenuViewHolder(View itemView) {
            super(itemView);
            ivMoreRecipe = itemView.findViewById(R.id.iv_tag_recipe);
            imgAddCancel = itemView.findViewById(R.id.img_add_cancel);

        }
    }


}
