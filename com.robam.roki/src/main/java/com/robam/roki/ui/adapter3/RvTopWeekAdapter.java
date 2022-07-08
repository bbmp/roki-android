package com.robam.roki.ui.adapter3;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
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
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author r210190
 * des Top榜单列表adapter
 */
public class RvTopWeekAdapter extends BaseQuickAdapter<Recipe , BaseViewHolder> implements LoadMoreModule {
//    private MultiTransformation multiTop = new MultiTransformation<>(new CenterCrop(),
//            new RoundedCornersTransformation(12, 0,
//                    RoundedCornersTransformation.CornerType.TOP));
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.img_default)
            .error(R.mipmap.img_default)
            .format(DecodeFormat.PREFER_RGB_565)
            .override(350*2, 158*2);


    public RvTopWeekAdapter() {
        super(R.layout.item_topic_week_recipe_2);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Recipe item) {
        if (item != null){
            holder.setText(R.id.tv_recipe_name , item.name)
                      .setText(R.id.tv_recipe_read_number,  "上周热度 "+ NumberUtil.converString(item.viewCount));
//                    .setText(R.id.tv_recipe_collect_number ,"收藏 "  + NumberUtil.converString(item.collectCount))
//                    .setImageResource(R.id.iv_love_recipe , item.collected ? R.drawable.ic_baseline_favorite_24 :
//                            R.drawable.ic_baseline_favorite_border_24);
            holder.getView(R.id.iv_love_recipe).setSelected(item.collected);

            TextView tv_topic_ranking = holder.getView(R.id.tv_topic_ranking);
            int itemPosition = getItemPosition(item);
            if (itemPosition < 10) {
                tv_topic_ranking.setText("TOP" + itemPosition);
                tv_topic_ranking.setVisibility(View.VISIBLE);
            } else {
                tv_topic_ranking.setVisibility(View.INVISIBLE);
            }
            ImageView iv_top_week_img = (ImageView) holder.getView(R.id.iv_top_week_img);
            String recipeUrl = RecipeUtils.getRecipeImgUrl(item);
            GlideApp.with(getContext())
                    .load(recipeUrl)
                    .apply(options)
                    .into(iv_top_week_img);
            if (!TextUtils.isEmpty(item.video))
                holder.getView(R.id.iv_play).setVisibility(View.VISIBLE);
            else
                holder.getView(R.id.iv_play).setVisibility(View.GONE);

//            TextView tv_recipe_read_number = (TextView) holder.getView(R.id.tv_recipe_read_number);
//            Drawable drawable1 = UiUtils.getResources().getDrawable(R.drawable.icon_hot, null);
//            drawable1.setBounds(0, 0, 40, 40);
//            tv_recipe_read_number.setCompoundDrawables(drawable1, null, null, null);


//            TextView tvCollection = (TextView) holder.getView(R.id.tv_recipe_collect_number);
//            List<Dc> dcs = item.getJs_dcs();
//            if (dcs != null && dcs.size() != 0) {
//                Drawable drawable = UiUtils.getResources().getDrawable(DeviceNameHelper.getIcon(dcs), null);
//                drawable.setBounds(0, 0, 35, 35);
//                tvCollection.setCompoundDrawables(drawable, null, null, null);
//                tvCollection.setText(DeviceNameHelper.getDeviceName2(dcs));
//
//            } else {
//
//            }

            addChildClickViewIds(R.id.iv_top_week_img , R.id.iv_love_recipe);
        }
    }
}
