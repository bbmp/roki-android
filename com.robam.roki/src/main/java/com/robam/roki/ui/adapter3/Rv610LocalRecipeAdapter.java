package com.robam.roki.ui.adapter3;

import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.common.pojos.AbsRecipe;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;

import org.jetbrains.annotations.NotNull;

import jp.wasabeef.glide.transformations.MaskTransformation;

/**
 * @author r210190
 * des 菜谱adapter
 */
public class Rv610LocalRecipeAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder> {
    private static RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.icon_recipe_default) //预加载图片
            .error(R.mipmap.icon_recipe_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
            .transform(new CenterCrop() ,new MaskTransformation(R.drawable.shape_bg_white_top_15dp)); //圆角


    public Rv610LocalRecipeAdapter() {
        super(R.layout.item_local_recipe);
    }



    @Override
    protected void convert(@NotNull BaseViewHolder holder, DeviceConfigurationFunctions item) {
        if (item != null){
            holder.setText(R.id.tv_recipe_name , item.functionName);
//                    .setText(R.id.tv_collection_number , "收藏 " + NumberUtil.converString(item.collectCount))
//                    .setText(R.id.tv_recipe_read_number ,"阅读 " + NumberUtil.converString(item.viewCount));
            ImageView ivTagRecipe = (ImageView) holder.getView(R.id.iv_tag_recipe);
            GlideApp.with(getContext())
                    .load(item.backgroundImg)
                    .apply(options)
                    .into(ivTagRecipe);
        }
    }

}
