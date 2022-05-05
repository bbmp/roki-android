package com.robam.roki.ui.adapter3;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;

import org.jetbrains.annotations.NotNull;

import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author r210190
 * des 精选专题列表adapter
 */
public class RvThemeAdapter extends BaseQuickAdapter<RecipeTheme, BaseViewHolder> {
    private MultiTransformation multiTop = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(20, 0,
                    RoundedCornersTransformation.CornerType.TOP));

    public RvThemeAdapter() {
        super(R.layout.item_theme_recipe);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, RecipeTheme item) {
        if (item != null) {
            int i  = 0 ;
            if (item.relateCookbookId != null){
                String[] split = item.relateCookbookId.split(",");
                i = split.length ;
            }
            holder.setText(R.id.tv_theme_recipe_name, item.name)
                    .setText(R.id.tv_theme_recipe_desc, item.subName)
                    .setText(R.id.tv_theme_recipe_number, i+ "道菜谱")
                .setImageResource(R.id.iv_love_theme , item.isCollect ? R.drawable.ic_baseline_favorite_24 : R.drawable.ic_baseline_favorite_border_24)
            ;

            ImageView iv_theme_item_img = (ImageView) holder.getView(R.id.iv_theme_item_img);
            GlideApp.with(getContext())
                    .load(item.imageUrl)
                    .apply(RequestOptions.bitmapTransform(multiTop))
                    .into(iv_theme_item_img);
        }
    }
}
