package com.robam.roki.ui.adapter3;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.helper3.DeviceNameHelper;
import com.robam.roki.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import jp.wasabeef.glide.transformations.MaskTransformation;

/**
 * @author r210190
 * des 专题列表菜谱adapter
 */
public class RvSelectThemeAdapter extends BaseQuickAdapter<Recipe, BaseViewHolder> {
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.icon_recipe_default)
            .error(R.mipmap.icon_recipe_default)
            .priority(Priority.HIGH)
            .skipMemoryCache(true)
            .format(DecodeFormat.PREFER_RGB_565)
            .override(350*2, 158*2)
            .diskCacheStrategy(DiskCacheStrategy.NONE);

    private Reponses.ThemeRecipeDetailResponse themeRecipeDetailResponse;

    public void setThemeRecipeDetailResponse(Reponses.ThemeRecipeDetailResponse themeRecipeDetailResponse) {
        this.themeRecipeDetailResponse = themeRecipeDetailResponse;
        notifyDataSetChanged();
    }

    public RvSelectThemeAdapter() {
        super(R.layout.item_rv_select_theme);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Recipe item) {
        if (item != null) {
            RelativeLayout csl_item = holder.getView(R.id.csl_item);
            if (holder.getLayoutPosition() == 0) {
                csl_item.setVisibility(View.VISIBLE);
                if (themeRecipeDetailResponse != null) {
                    holder.setText(R.id.tv_read_theme_number, "阅读 " + NumberUtil.converString(themeRecipeDetailResponse.themeRecipeDetail.viewCount))
//                            .setText(R.id.tv_collect_theme_number, "收藏 " + NumberUtil.converString(themeRecipeDetailResponse.themeRecipeDetail.collectCount))
                            .setText(R.id.tv_theme_recipe_number, themeRecipeDetailResponse.themeRecipeDetail.recipeList.size() + "道菜谱")
                            .setText(R.id.tv_theme_desc, themeRecipeDetailResponse.themeRecipeDetail.description);
                }

            } else {
                csl_item.setVisibility(View.GONE);
            }

            holder.setText(R.id.tv_recipe_name, item.name);
            ImageView iv_top_week_img = holder.getView(R.id.iv_top_week_img);
            String recipeUrl = RecipeUtils.getRecipeImgUrl(item);
            GlideApp.with(getContext())
                    .load(recipeUrl)
                    .apply(options)
                    .into(iv_top_week_img);
            if (!TextUtils.isEmpty(item.video))
                holder.getView(R.id.iv_play).setVisibility(View.VISIBLE);
            else
                holder.getView(R.id.iv_play).setVisibility(View.GONE);

            holder.setText(R.id.tv_recipe_read_number, "热度 " + NumberUtil.converString(item.viewCount));
            holder.getView(R.id.iv_love_recipe).setSelected(item.collected);
//                    .setImageResource(R.id.iv_love_recipe, item.collected ? R.drawable.ic_baseline_favorite_24 : R.drawable.ic_baseline_favorite_border_24);
//                    .setVisible(R.id.iv_topic_ranking, false);

//            TextView tvCollection = holder.getView(R.id.tv_recipe_collect_number);
//            ImageView imageView = holder.getView(R.id.img_recipe_collect_number_icon);
            List<Dc> dcs = item.getJs_dcs();
            if (dcs != null && dcs.size() != 0) {

//                imageView.setImageResource(DeviceNameHelper.getIcon(dcs));
//                tvCollection.setText(DeviceNameHelper.getDeviceName2(dcs));
                holder.setVisible(R.id.tv_device_name, true);
                holder.setText(R.id.tv_device_name, DeviceNameHelper.getDeviceName2(dcs));
            } else {
//                imageView.setImageDrawable(null);
                holder.setVisible(R.id.tv_device_name, false);
            }


        }
    }
}
