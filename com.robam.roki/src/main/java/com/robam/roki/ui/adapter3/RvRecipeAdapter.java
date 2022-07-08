package com.robam.roki.ui.adapter3;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.utils.api.DisplayUtils;
import com.robam.common.pojos.AbsRecipe;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.helper3.DeviceNameHelper;
import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author r210190
 * des 菜谱adapter
 */
public class RvRecipeAdapter extends BaseQuickAdapter<AbsRecipe, BaseViewHolder> {
    private RequestOptions options;


    public RvRecipeAdapter(Context context) {
        super(R.layout.list_item_card_3);
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.icon_recipe_default) //预加载图片
                .error(R.mipmap.icon_recipe_default) //加载失败图片
                .priority(Priority.HIGH) //优先级
                .skipMemoryCache(true)
                .override(167*2, 100*2)
                .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存
//                .transform(new RoundedCornersTransformation(30, 0,RoundedCornersTransformation.CornerType.TOP)); //圆角

    }



    @Override
    protected void convert(@NotNull BaseViewHolder holder, AbsRecipe item) {
        if (item != null){

            Recipe recipe = (Recipe) item ;
            holder.setText(R.id.tv_recipe_name, recipe.name)
                    .setText(R.id.tv_recipe_read_number, NumberUtil.converString(recipe.viewCount));


            GlideApp.with(getContext())
                    .load(RecipeUtils.getRecipeImgUrl(recipe))
                    .apply(options)
                    .into((ImageView) holder.getView(R.id.iv_tag_recipe));
            if (!TextUtils.isEmpty(recipe.video))
                holder.getView(R.id.iv_play).setVisibility(View.VISIBLE);
            else
                holder.getView(R.id.iv_play).setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecipeDetailPage.show(recipe.id, recipe.sourceType);
                }
            });

            List<Dc> dcs = recipe.getJs_dcs();
            if (dcs != null && dcs.size() != 0) {

                holder.setVisible(R.id.tv_device_name, true);
                holder.setText(R.id.tv_device_name, DeviceNameHelper.getDeviceName2(dcs));
            } else {
                holder.setVisible(R.id.tv_device_name, false);
            }

        }
    }

}
