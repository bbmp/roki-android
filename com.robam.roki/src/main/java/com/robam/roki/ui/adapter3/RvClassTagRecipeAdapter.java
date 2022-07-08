package com.robam.roki.ui.adapter3;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.constant.IDeviceType;
import com.robam.common.pojos.Categories;
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

import static com.robam.roki.utils.UiUtils.getResources;

/**
 * @author r210190
 * des 全部菜谱adapter
 */
public class RvClassTagRecipeAdapter extends BaseQuickAdapter<Recipe , BaseViewHolder> implements LoadMoreModule {
    private boolean isShowDevice = true ;

    private static RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.icon_recipe_default) //预加载图片
            .error(R.mipmap.icon_recipe_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存
//            .transform(new CenterCrop(),new MaskTransformation(R.mipmap.icon_roki_recipe_bg)); //圆角

    public RvClassTagRecipeAdapter() {

        super(R.layout.list_item_card);

        Log.e("结果",isShowDevice+"---");
    }
    public  int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    @Override
    protected void convert(@NotNull BaseViewHolder holder, Recipe item) {
        if (item != null){
            holder.setText(R.id.tv_recipe_name , item.name)
//                    .setText(R.id.tv_collection_number , "收藏" + NumberUtil.converString(item.collectCount))
                    .setText(R.id.tv_recipe_read_number , NumberUtil.converString(item.viewCount));
            ImageView ivTagRecipe = (ImageView) holder.getView(R.id.iv_tag_recipe);
            String recipeUrl = RecipeUtils.getRecipeImgUrl(item);
            GlideApp.with(getContext())
                    .load(recipeUrl)
                    .apply(options)
                    .into(ivTagRecipe);
            if (!TextUtils.isEmpty(item.video))
                holder.getView(R.id.iv_play).setVisibility(View.VISIBLE);
            else
                holder.getView(R.id.iv_play).setVisibility(View.GONE);
            if (!isShowDevice){



                holder.setText(R.id.tv_recipe_name , item.name)
                        .setText(R.id.tv_recipe_read_number , "收藏 " + NumberUtil.converString(item.collectCount));
//                        .setText(R.id.tv_collection_number , "阅读 " + NumberUtil.converString(item.viewCount));
//                holder.getView(R.id.img_recipe_read_number).setVisibility(View.INVISIBLE);
//                holder.setVisible(R.id.img_collection_number,false);
//                holder.getView(R.id.img_collection_number).getLayoutParams();

//                holder.getView(R.id.img_collection_number).setVisibility(View.GONE);
//                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.getView(R.id.tv_collection_number).getLayoutParams();
//                lp.setMargins(Dp2Px(getContext(),8)
//                        ,holder.getView(R.id.tv_collection_number).getTop(),holder.getView(R.id.tv_collection_number).getRight(),
//                        holder.getView(R.id.tv_collection_number).getBottom());

//                lp.leftMargin=Dp2Px(getContext(),8);

//                holder.getView(R.id.tv_collection_number).setLayoutParams(lp);


                return;
            }

//            TextView tv_recipe_read_number = (TextView) holder.getView(R.id.tv_recipe_read_number);
//            Drawable drawable1 = UiUtils.getResources().getDrawable(R.drawable.ic_eye, null);
//            drawable1.setBounds(0, 0, 35, 35);
//            tv_recipe_read_number.setCompoundDrawables(drawable1, null, null, null);

//            TextView tvCollection = (TextView) holder.getView(R.id.tv_collection_number);
//            ImageView imageView=holder.getView(R.id.img_collection_number);
            List<Dc> dcs = item.getJs_dcs();
            if (dcs != null && dcs.size() != 0) {
                holder.setVisible(R.id.tv_device_name, true);
                holder.setText(R.id.tv_device_name, DeviceNameHelper.getDeviceName2(dcs));

            } else {
                holder.setVisible(R.id.tv_device_name, false);
            }

        }
    }

    public void setShowDevice(boolean showDevice) {
        isShowDevice = showDevice;
    }
}
