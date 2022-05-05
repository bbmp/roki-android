package com.robam.roki.ui.adapter3;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.common.pojos.AbsRecipe;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.helper3.DeviceNameHelper;
import com.robam.roki.utils.UiUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import jp.wasabeef.glide.transformations.MaskTransformation;

/**
 * @author r210190
 * des 搜索成功界面头部adapter
 */
public class RvRecipeSearchHeadAdapter extends BaseQuickAdapter<AbsRecipe, BaseViewHolder> {
    private static RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.icon_recipe_default) //预加载图片
            .error(R.mipmap.icon_recipe_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存
            .transform(new CenterCrop() ,new MaskTransformation(R.mipmap.icon_roki_recipe_bg)); //圆角


//    public RvRecipeSearchHeadAdapter() {
//        super(R.layout.list_item_card_search);
//    }
public RvRecipeSearchHeadAdapter() {
    super(R.layout.list_item_card);
}



    @Override
    protected void convert(@NotNull BaseViewHolder holder, AbsRecipe item) {
        if (item != null){
            holder.setText(R.id.tv_recipe_name , item.name)
//                    .setText(R.id.tv_collection_number , "收藏 " + NumberUtil.converString(item.collectCount))
//                    .setText(R.id.tv_recipe_read_number ,"阅读 " + NumberUtil.converString(item.viewCount))
                    .setText(R.id.tv_recipe_read_number ,"" + NumberUtil.converString(item.viewCount));



            ImageView ivTagRecipe =  holder.getView(R.id.iv_tag_recipe);
            String recipeUrl = RecipeUtils.getRecipeImgUrl(item);
            GlideApp.with(getContext())
                    .load(recipeUrl)
                    .apply(options)
                    .into(ivTagRecipe);


            Recipe recipe = (Recipe) item ;
            TextView tv_recipe_read_number = (TextView) holder.getView(R.id.tv_recipe_read_number);
            ImageView imageView=holder.getView(R.id.img_collection_number);
            TextView tvCollection = (TextView) holder.getView(R.id.tv_collection_number);
            List<Dc> dcs = ((Recipe)item).getJs_dcs();
            if (dcs != null && dcs.size() != 0&&!((Recipe) item).getCookbookType().equals("10")) {
                tvCollection.setText(DeviceNameHelper.getDeviceName2(dcs));
                imageView.setImageDrawable( UiUtils.getResources().getDrawable(DeviceNameHelper.getIcon(dcs), null));
            } else {
//                imageView.setImageDrawable(null);
                Drawable drawable = UiUtils.getResources().getDrawable(R.drawable.hot, null);
                imageView.setImageDrawable( drawable);
                tv_recipe_read_number.setVisibility(View.GONE);
                holder.setText(R.id.tv_collection_number, NumberUtil.converString(recipe.viewCount));
                holder.setVisible(R.id.img_recipe_read_number,false);
            }
        }
    }

}
