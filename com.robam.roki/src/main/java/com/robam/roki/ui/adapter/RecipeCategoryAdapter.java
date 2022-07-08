package com.robam.roki.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.Recipe;
import com.robam.common.services.CookbookManager;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.RecipeUtil;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.helper3.DeviceNameHelper;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.view.RoundTransformation;
import com.robam.roki.utils.ListUtils;
import com.robam.roki.utils.UiUtils;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2018/11/13.
 * PS: Not easy to write code, please indicate.
 */
public class RecipeCategoryAdapter extends BaseQuickAdapter<Recipe, BaseViewHolder> implements LoadMoreModule {

    public static final int OTHER_VIEW = 2;
    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.img_default)
            .error(R.mipmap.img_default)
            .override(350*2, 158*2)
            .format(DecodeFormat.PREFER_RGB_565);
//    private MultiTransformation multiTop = new MultiTransformation<>(new CenterCrop(), new RoundedCornersTransformation(40, 0, RoundedCornersTransformation.CornerType.TOP));
    List<Recipe> mList = new ArrayList<>();
//    DisplayImageOptions.Builder options = new DisplayImageOptions.Builder();
//    private List<Recipe> mList1;

    public RecipeCategoryAdapter() {
        super(R.layout.view_home_recommand_recipe);

    }


    private void onItemClick(final Recipe recipe, final RecipeCategoryViewHolder recipeCategoryViewHolder) {

        CookbookManager cm = CookbookManager.getInstance();
        if (recipe != null) {
            if (recipe.collected) {
                cm.deleteFavorityCookbooks(recipe.id, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        recipe.setIsCollected(false);
//                        recipeCategoryViewHolder.iv_collection.setVisibility(View.VISIBLE);
                        recipe.collected = false;
                        recipeCategoryViewHolder.iv_collection.setSelected(false);
                        ToastUtils.showShort("已取消收藏");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showShort(t.getMessage());
                    }
                });
            } else {
                cm.addFavorityCookbooks(recipe.id, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        recipe.setIsCollected(true);
//                        recipeCategoryViewHolder.iv_collection.setVisibility(View.GONE);
                        recipe.collected = true;
                        recipeCategoryViewHolder.iv_collection.setSelected(true);
                        ToastUtils.showShort("收藏成功");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showShort(t.getMessage());
                    }
                });

            }
        }

    }



//    List<Dc> listBefore = null;



    public void updateData(ArrayList<Recipe> recipeList) {
        if (recipeList == null || recipeList.size() == 0) {
            return;
        }
        Iterator<Recipe> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
//        if (mList1 != null){
//            LogUtils.i("20181115", " mList1:" + mList1.size());
//            mList.addAll(mList1);
//        }
        mList.addAll(recipeList);
        LogUtils.i("20181115", " size:" + mList.size());

//        if (listBefore != null) {
//            listBefore.clear();
//        }
        notifyDataSetChanged();
    }


    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Recipe recipe) {
        if (null != recipe) {
            if (!TextUtils.isEmpty(recipe.stampLogo)) {
                baseViewHolder.setVisible(R.id.logo, true);
                ImageView imageView = baseViewHolder.getView(R.id.logo);
                GlideApp.with(getContext()).load(recipe.stampLogo).into(imageView);
            } else {
                baseViewHolder.setVisible(R.id.logo, false);
            }
            //modify by wang 22/04/21
            if (!TextUtils.isEmpty(recipe.video))
                baseViewHolder.setVisible(R.id.iv_play, true);
            else
                baseViewHolder.setVisible(R.id.iv_play, false);


            if (!StringUtils.isNullOrEmpty(recipe.imgLarge)) {
                ImageView imgView = baseViewHolder.getView(R.id.iv_img);
                GlideApp.with(getContext())
                        .load(RecipeUtils.getRecipeImgUrl(recipe))
                        .apply(options)
                        .into(imgView);

            } else {
                baseViewHolder.setImageResource(R.id.iv_img, R.mipmap.img_default);
            }
            baseViewHolder.setText(R.id.home_recipe_tv_recipename, recipe.name);
//                recipeCategoryViewHolder.collect.setText("收藏 " +  NumberUtil.converString(mList.get(position).collectCount) );
//                recipeCategoryViewHolder.collect2.setText("阅读 " + NumberUtil.converString(mList.get(position).viewCount) );
            baseViewHolder.setText(R.id.home_recipe_tv_collect2, "热度 " + NumberUtil.converString(recipe.viewCount));


//                Drawable drawable1 = UiUtils.getResources().getDrawable(R.drawable.hot, null);
//                drawable1.setBounds(0, 0, 35, 35);
//                recipeCategoryViewHolder.collect2.setCompoundDrawables(drawable1, null, null, null);


//                TextView tvCollection = (TextView) holder.getView(R.id.tv_collection_number);
            List<Dc> dcs = recipe.getJs_dcs();
            if (dcs != null && dcs.size() != 0) {
//                    Drawable drawable = UiUtils.getResources().getDrawable(DeviceNameHelper.getIcon(dcs), null);
//                    drawable.setBounds(0, 0, 35, 35);

            } else {

            }
            List<Dc> js_dcs = recipe.getJs_dcs();
            if (0 != js_dcs.size()) {

                    if (js_dcs.size() == 1) {
                        baseViewHolder.setVisible(R.id.tv_device_name_one, true);
                        baseViewHolder.setVisible(R.id.tv_device_name_two, false);
                        switch (js_dcs.get(0).getName()) {
                            case DeviceType.RDKX:
                                baseViewHolder.setText(R.id.tv_device_name_one, "烤");
                                break;
                            case DeviceType.RZQL:
                                baseViewHolder.setText(R.id.tv_device_name_one,"蒸");
                                break;
                            case DeviceType.RWBL:
                                baseViewHolder.setText(R.id.tv_device_name_one,"微");
                                break;
                            case DeviceType.RRQZ:
                                baseViewHolder.setText(R.id.tv_device_name_one,"灶");
                                break;
                            case DeviceType.RZKY:
                                baseViewHolder.setText(R.id.tv_device_name_one,"一体");
                                break;
                            case DeviceType.RIKA:
                                baseViewHolder.setText(R.id.tv_device_name_one,"RIKA");
                                break;
                            case DeviceType.KZNZ:
                                baseViewHolder.setText(R.id.tv_device_name_one,"智能灶");
                                break;
                            default:
                                break;

                        }

                    } else {
                        baseViewHolder.setVisible(R.id.tv_device_name_one, true);
                        baseViewHolder.setVisible(R.id.tv_device_name_two, true);
                        switch (js_dcs.get(0).getName()) {
                            case DeviceType.RDKX:
                                baseViewHolder.setText(R.id.tv_device_name_one,"烤");
                                break;
                            case DeviceType.RZQL:
                                baseViewHolder.setText(R.id.tv_device_name_one,"蒸");
                                break;
                            case DeviceType.RWBL:
                                baseViewHolder.setText(R.id.tv_device_name_one,"微");
                                break;
                            case DeviceType.RRQZ:
                                baseViewHolder.setText(R.id.tv_device_name_one,"灶");
                                break;
                            case DeviceType.RZKY:
                                baseViewHolder.setText(R.id.tv_device_name_one,"一体");
                                break;
                            case DeviceType.RIKA:
                                baseViewHolder.setText(R.id.tv_device_name_one,"RIKA");
                                break;
                            case DeviceType.KZNZ:
                                baseViewHolder.setText(R.id.tv_device_name_one,"智能灶");
                                break;
                            default:
                                break;
                        }
                        switch (js_dcs.get(1).getName()) {
                            case DeviceType.RDKX:
                                baseViewHolder.setText(R.id.tv_device_name_two,"烤");
                                break;
                            case DeviceType.RZQL:
                                baseViewHolder.setText(R.id.tv_device_name_two,"蒸");
                                break;
                            case DeviceType.RWBL:
                                baseViewHolder.setText(R.id.tv_device_name_two,"微");
                                break;
                            case DeviceType.RRQZ:
                                baseViewHolder.setText(R.id.tv_device_name_two,"灶");
                                break;
                            case DeviceType.RZKY:
                                baseViewHolder.setText(R.id.tv_device_name_two,"一体");
                                break;
                            case DeviceType.RIKA:
                                baseViewHolder.setText(R.id.tv_device_name_two,"RIKA");
                                break;
                            case DeviceType.KZNZ:
                                baseViewHolder.setText(R.id.tv_device_name_two,"智能灶");
                                break;
                            default:
                                break;
                        }

                    }


            }

            ImageView ivCollection = baseViewHolder.getView(R.id.iv_collection);
            ivCollection.setSelected(recipe.collected);
        }
    }

//    public void imgBg(String url) {
//        Recipe recipe = new Recipe();
//        recipe.imgLarge = url;
//        mList1 = new ArrayList<>();
//        mList1.add(recipe);
//    }

}

class RecipeCategoryViewHolder extends RecyclerView.ViewHolder {

    public ImageView imgView, logo,
            iv_collection;//背景图
    public TextView name;//标题
    public TextView collect2;
    public TextView tv_device_name_one,tv_device_name_two;
    public ImageView  img_play;


    public RecipeCategoryViewHolder(View itemView) {
        super(itemView);

        imgView = (ImageView) itemView.findViewById(R.id.iv_img);

        tv_device_name_one = itemView.findViewById(R.id.tv_device_name_one);
        tv_device_name_two = itemView.findViewById(R.id.tv_device_name_two);
        iv_collection = (ImageView) itemView.findViewById(R.id.iv_collection);
        logo = (ImageView) itemView.findViewById(R.id.logo);
        name = (TextView) itemView.findViewById(R.id.home_recipe_tv_recipename);
        collect2 = (TextView) itemView.findViewById(R.id.home_recipe_tv_collect2);
        img_play = itemView.findViewById(R.id.iv_play);
    }
}

class RecipeCategoryTitleViewHolder extends RecyclerView.ViewHolder {

    public ImageView imgView;

    public RecipeCategoryTitleViewHolder(View itemView) {
        super(itemView);
        imgView = (ImageView) itemView.findViewById(R.id.iv_img);
    }
}

