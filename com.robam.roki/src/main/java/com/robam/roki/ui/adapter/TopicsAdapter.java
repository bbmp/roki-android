package com.robam.roki.ui.adapter;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.common.pojos.RecipeTheme;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.view.RoundTransformation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class TopicsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    private List<String> imageUrlList;
    private MultiTransformation roundedCornerFormation = new MultiTransformation(new RoundedCornersTransformation(200, 0, RoundedCornersTransformation.CornerType.ALL));

    public TopicsAdapter(@Nullable List<String> data) {
        super(R.layout.item_selected_topic, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String imgUrl) {
        ImageView itemTopticsImg = baseViewHolder.itemView.findViewById(R.id.iv_item_topic_img);
        GlideApp.with(getContext())
                .load(imgUrl)
//                .apply(RequestOptions.bitmapTransform(roundedCornerFormation))
                .fitCenter()
                .placeholder(R.mipmap.img_default)
                .error(R.mipmap.img_default)
                .into(itemTopticsImg);
    }

    @NotNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }
}
