package com.robam.roki.ui.adapter3;

import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.common.pojos.CookStep;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;

import org.jetbrains.annotations.NotNull;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author r210190
 * des 菜谱详情烹饪步骤adapter
 */
public class RvRecipeSteps2Adapter extends BaseQuickAdapter<CookStep, BaseViewHolder> {
    private MultiTransformation options = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(40, 0));

    public RvRecipeSteps2Adapter() {
        super(R.layout.item_cook_step_desc);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, CookStep item) {
        if (item != null) {
//            holder.setText(R.id.tv_step , "步骤" + item.order + "/" +getItemCount());
            holder.setText(R.id.tv_step_desc , item.desc);
//            ImageView ivImage = (ImageView) holder.getView(R.id.iv_image);
//            GlideApp.with(getContext())
//                    .load(item.imageUrl)
//                    .placeholder(R.mipmap.icon_recipe_default)
//                    .error(R.mipmap.icon_recipe_default)
//                    .priority(Priority.HIGH)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .apply(RequestOptions.bitmapTransform(options))
//                    .into(ivImage);
        }
    }
}
