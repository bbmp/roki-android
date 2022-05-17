package com.robam.roki.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.plat.pojos.RCReponse;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.Recipe;
import com.robam.common.services.CookbookManager;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.RecipeCategoryMultiItem;
import com.robam.roki.model.bean.ThemeRecipeMultipleItem;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.RecipeUtil;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.helper3.DeviceNameHelper;
import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.view.RoundTransformation;
import com.robam.roki.utils.ListUtils;
import com.robam.roki.utils.UiUtils;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2018/11/13.
 * PS: Not easy to write code, please indicate.
 */
public class RecipeCategoryAdapter extends BaseMultiItemQuickAdapter<RecipeCategoryMultiItem, BaseViewHolder> implements LoadMoreModule {//RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int MAIN_VIEW = 1;
    public static final int OTHER_VIEW = 2;
    private LayoutInflater mInflater;
//    private Context mContext;
//    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    private MultiTransformation multiTop = new MultiTransformation<>(new CenterCrop(), new RoundedCornersTransformation(40, 0, RoundedCornersTransformation.CornerType.TOP));

    private String platformCode, guid;


    public RecipeCategoryAdapter(String platformCode, String guid) {
        addItemType(RecipeCategoryMultiItem.MAIN_VIEW, R.layout.view_home_recommand_recipe);
        addItemType(RecipeCategoryMultiItem.OTHER_VIEW, R.layout.view_home_recommandrecipe);
        this.platformCode = platformCode;
        this.guid = guid;
    }

    private void onItemClick(final Recipe recipe, final BaseViewHolder baseViewHolder) {

        CookbookManager cm = CookbookManager.getInstance();
        if (recipe != null) {
            if (recipe.collected) {
                cm.deleteFavorityCookbooks(recipe.id, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        recipe.setIsCollected(false);
                        baseViewHolder.getView(R.id.iv_collection).setVisibility(View.VISIBLE);
                        baseViewHolder.getView(R.id.iv_collection_select).setVisibility(View.GONE);
                        baseViewHolder.setText(R.id.home_recipe_tv_collect, "收藏 "  + recipe.collectCount);
                        recipe.collected = false;
                        ToastUtils.showShort("已取消收藏");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showShort(t.getMessage());
                    }
                });
            } else {
                RokiRestHelper.addFavorityCookbooks(recipe.id, RCReponse.class, new RetrofitCallback<RCReponse>() {
                    @Override
                    public void onSuccess(RCReponse rcReponse) {
                        if (null != rcReponse) {
                            recipe.setIsCollected(true);
                            baseViewHolder.getView(R.id.iv_collection).setVisibility(View.GONE);
                            baseViewHolder.getView(R.id.iv_collection_select).setVisibility(View.VISIBLE);
                            baseViewHolder.setText(R.id.home_recipe_tv_collect, "收藏 "  + recipe.collectCount);
                            recipe.collected = true;
                            ToastUtils.showShort("收藏成功");
                        }
                    }

                    @Override
                    public void onFaild(String err) {
                        ToastUtils.showShort(err);
                    }
                });

            }
        }

    }





    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, RecipeCategoryMultiItem recipeCategoryMultiItem) {
        switch (baseViewHolder.getItemViewType()) {
            case RecipeCategoryMultiItem.MAIN_VIEW: {
                final Recipe recipe = recipeCategoryMultiItem.getRecipe();
                ImageView imageView = baseViewHolder.getView(R.id.iv_image);

                if (null != recipe && !TextUtils.isEmpty(recipe.imgLarge)) {
                    imageView.setVisibility(View.VISIBLE);
                    GlideApp.with(getContext())
                            .load(RecipeUtils.getRecipeImgUrl(recipe))

                            .into(imageView);

                } else {
                    imageView.setVisibility(View.GONE);
                }

                break;
            }
            case RecipeCategoryMultiItem.OTHER_VIEW: {
                final Recipe recipe = recipeCategoryMultiItem.getRecipe();
                if (null != recipe) {
                    if (!TextUtils.isEmpty(recipe.stampLogo)) {
                        ImageView imageView = baseViewHolder.getView(R.id.logo);
                        baseViewHolder.getView(R.id.logo).setVisibility(View.VISIBLE);
                        ImageUtils.displayImage(getContext(), recipe.stampLogo, imageView);
                    } else {
                        baseViewHolder.getView(R.id.logo).setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(recipe.providerImage)) {
                        ImageView imageView = baseViewHolder.getView(R.id.home_recipe_head_ic);
                        ImageUtils.displayImage(getContext(), recipe.providerImage, imageView);
                    }

                    ImageView imgView = baseViewHolder.getView(R.id.iv_img);
                    imgView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RecipeDetailPage.show(recipe, recipe.id, RecipeDetailPage.DeviceRecipePage,
                        RecipeRequestIdentification.RECIPE_SORTED,platformCode,guid);
                        }
                    });
                    if (!StringUtils.isNullOrEmpty(recipe.imgLarge)) {

                        GlideApp.with(getContext())
                                .load(RecipeUtils.getRecipeImgUrl(recipe))
                                .apply(RequestOptions.bitmapTransform(multiTop))
                                .into(imgView);

                    } else {

                        imgView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.img_default));
                    }
                    baseViewHolder.setText(R.id.home_recipe_tv_recipename, recipe.name);

                    TextView collect2 = baseViewHolder.getView(R.id.home_recipe_tv_collect2);
                    collect2.setText("" + NumberUtil.converString(recipe.viewCount));


                    Drawable drawable1 = UiUtils.getResources().getDrawable(R.drawable.hot, null);
                    drawable1.setBounds(0, 0, 35, 35);
                    collect2.setCompoundDrawables(drawable1, null, null, null);


//                TextView tvCollection = (TextView) holder.getView(R.id.tv_collection_number);
                    List<Dc> dcs = recipe.getJs_dcs();
                    if (dcs != null && dcs.size() != 0) {
                        TextView collect = baseViewHolder.getView(R.id.home_recipe_tv_collect);
                        Drawable drawable = UiUtils.getResources().getDrawable(DeviceNameHelper.getIcon(dcs), null);
                        drawable.setBounds(0, 0, 35, 35);
                        collect.setCompoundDrawables(drawable, null, null, null);
                        collect.setText(DeviceNameHelper.getDeviceName2(dcs));

                    } else {

                    }
                    List<Dc> js_dcs = recipe.getJs_dcs();
                    if (0 != js_dcs.size()) {

                        List<Dc> listBefore = ListUtils.getListBefore(js_dcs);

                        for (int i = 0; i < listBefore.size(); i++) {
                            if (listBefore.size() == 1) {
                                baseViewHolder.getView(R.id.img_device_one).setVisibility(View.VISIBLE);
                                baseViewHolder.getView(R.id.tv_device_name_one).setVisibility(View.VISIBLE);
                                baseViewHolder.getView(R.id.img_device_two).setVisibility(View.GONE);
                                baseViewHolder.getView(R.id.tv_device_name_two).setVisibility(View.GONE);
                                switch (listBefore.get(0).getName()) {
                                    case DeviceType.RDKX:
                                        baseViewHolder.setText(R.id.tv_device_name_one, "烤");
                                        baseViewHolder.setImageResource(R.id.img_device_one, R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.RZQL:
                                        baseViewHolder.setText(R.id.tv_device_name_one, "蒸");
                                        baseViewHolder.setImageResource(R.id.img_device_one, R.mipmap.img_zql_collection);
                                        break;
                                    case DeviceType.RWBL:
                                        baseViewHolder.setText(R.id.tv_device_name_one, "微");
                                        baseViewHolder.setImageResource(R.id.img_device_one, R.mipmap.img_wbl_collection);
                                        break;
                                    case DeviceType.RRQZ:
                                        baseViewHolder.setText(R.id.tv_device_name_one, "灶");
                                        baseViewHolder.setImageResource(R.id.img_device_one, R.mipmap.img_rzz_collection);
                                        break;
                                    case DeviceType.RZKY:
                                        baseViewHolder.setText(R.id.tv_device_name_one, "一体");
                                        baseViewHolder.setImageResource(R.id.img_device_one, R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.RIKA:
                                        baseViewHolder.setText(R.id.tv_device_name_one, "RIKA");
                                        baseViewHolder.setImageResource(R.id.img_device_one, R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.KZNZ:
                                        baseViewHolder.setText(R.id.tv_device_name_one, "智能灶");
                                        baseViewHolder.setImageResource(R.id.img_device_one, R.mipmap.img_dkx_collection);
                                        break;
                                    default:
                                        break;

                                }

                            } else {
                                baseViewHolder.getView(R.id.img_device_one).setVisibility(View.VISIBLE);
                                baseViewHolder.getView(R.id.tv_device_name_one).setVisibility(View.VISIBLE);
                                baseViewHolder.getView(R.id.img_device_two).setVisibility(View.VISIBLE);
                                baseViewHolder.getView(R.id.tv_device_name_two).setVisibility(View.VISIBLE);
                                switch (listBefore.get(0).getName()) {
                                    case DeviceType.RDKX:
                                        baseViewHolder.setText(R.id.tv_device_name_one, "烤");
                                        baseViewHolder.setImageResource(R.id.img_device_one, R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.RZQL:
                                        baseViewHolder.setText(R.id.tv_device_name_one, "蒸");
                                        baseViewHolder.setImageResource(R.id.img_device_one, R.mipmap.img_zql_collection);
                                        break;
                                    case DeviceType.RWBL:
                                        baseViewHolder.setText(R.id.tv_device_name_one, "微");
                                        baseViewHolder.setImageResource(R.id.img_device_one, R.mipmap.img_wbl_collection);
                                        break;
                                    case DeviceType.RRQZ:
                                        baseViewHolder.setText(R.id.tv_device_name_one, "灶");
                                        baseViewHolder.setImageResource(R.id.img_device_one, R.mipmap.img_rzz_collection);
                                        break;
                                    case DeviceType.RZKY:
                                        baseViewHolder.setText(R.id.tv_device_name_one, "一体");
                                        baseViewHolder.setImageResource(R.id.img_device_one, R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.RIKA:
                                        baseViewHolder.setText(R.id.tv_device_name_one, "RIKA");
                                        baseViewHolder.setImageResource(R.id.img_device_one, R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.KZNZ:
                                        baseViewHolder.setText(R.id.tv_device_name_one, "智能灶");
                                        baseViewHolder.setImageResource(R.id.img_device_one, R.mipmap.img_dkx_collection);
                                        break;
                                    default:
                                        break;
                                }
                                switch (listBefore.get(1).getName()) {
                                    case DeviceType.RDKX:
                                        baseViewHolder.setText(R.id.tv_device_name_two, "烤");
                                        baseViewHolder.setImageResource(R.id.img_device_two, R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.RZQL:
                                        baseViewHolder.setText(R.id.tv_device_name_two, "蒸");
                                        baseViewHolder.setImageResource(R.id.img_device_two, R.mipmap.img_zql_collection);
                                        break;
                                    case DeviceType.RWBL:
                                        baseViewHolder.setText(R.id.tv_device_name_two, "微");
                                        baseViewHolder.setImageResource(R.id.img_device_two, R.mipmap.img_wbl_collection);
                                        break;
                                    case DeviceType.RRQZ:
                                        baseViewHolder.setText(R.id.tv_device_name_two, "灶");
                                        baseViewHolder.setImageResource(R.id.img_device_two, R.mipmap.img_rzz_collection);
                                        break;
                                    case DeviceType.RZKY:
                                        baseViewHolder.setText(R.id.tv_device_name_two, "一体");
                                        baseViewHolder.setImageResource(R.id.img_device_two, R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.RIKA:
                                        baseViewHolder.setText(R.id.tv_device_name_two, "RIKA");
                                        baseViewHolder.setImageResource(R.id.img_device_two, R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.KZNZ:
                                        baseViewHolder.setText(R.id.tv_device_name_two, "智能灶");
                                        baseViewHolder.setImageResource(R.id.img_device_two, R.mipmap.img_dkx_collection);
                                        break;
                                    default:
                                        break;
                                }

                            }
                        }

                    }

                    ImageView iv_collection = baseViewHolder.getView(R.id.iv_collection);
                    ImageView iv_collection_select = baseViewHolder.getView(R.id.iv_collection_select);
                    iv_collection.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            boolean isLog = Plat.accountService.isLogon();
                            if (isLog) {
//                                Recipe recipe = (Recipe) view.getTag(R.id.tag_recipe_collected);
//                                RecipeCategoryViewHolder recipeCategoryViewHolder = (RecipeCategoryViewHolder) view.getTag(R.id.tag_recipe_collected_holder);
                                onItemClick(recipe, baseViewHolder);
                            } else {
                                CmccLoginHelper.getInstance().toLogin();
                            }

                        }
                    });
                    iv_collection_select.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            boolean isLog = Plat.accountService.isLogon();
                            if (isLog) {
//                                Recipe recipe = (Recipe) view.getTag(R.id.tag_recipe_collected_select);
//                                RecipeCategoryViewHolder recipeCategoryViewHolder = (RecipeCategoryViewHolder) view.getTag(R.id.tag_recipe_collected_holder);
                                onItemClick(recipe, baseViewHolder);
                            } else {
                                UIService.getInstance().postPage(PageKey.UserLogin);
                            }
                        }
                    });
                    if (recipe.collected) {
                        iv_collection_select.setVisibility(View.VISIBLE);
                        iv_collection.setVisibility(View.GONE);
                    } else {
                        iv_collection_select.setVisibility(View.GONE);
                        iv_collection.setVisibility(View.VISIBLE);
                    }

                }
                break;
            }
        }
    }


}

class RecipeCategoryViewHolder extends RecyclerView.ViewHolder {

    public ImageView imgView, logo, source_logo,
            iv_collection, iv_collection_select;//背景图
    public TextView name;//标题
    public TextView collect  ,collect2;
    public TextView tv_device_name_one,tv_device_name_two;
    public ImageView img_device_one,img_device_two;


    public RecipeCategoryViewHolder(View itemView) {
        super(itemView);

        imgView = (ImageView) itemView.findViewById(R.id.iv_img);
        img_device_one = (ImageView) itemView.findViewById(R.id.img_device_one);
        img_device_two = (ImageView) itemView.findViewById(R.id.img_device_two);
        tv_device_name_one = itemView.findViewById(R.id.tv_device_name_one);
        tv_device_name_two = itemView.findViewById(R.id.tv_device_name_two);
        iv_collection = (ImageView) itemView.findViewById(R.id.iv_collection);
        iv_collection_select = (ImageView) itemView.findViewById(R.id.iv_collection_select);
        logo = (ImageView) itemView.findViewById(R.id.logo);
        source_logo = (ImageView) itemView.findViewById(R.id.home_recipe_head_ic);
        name = (TextView) itemView.findViewById(R.id.home_recipe_tv_recipename);
        collect = (TextView) itemView.findViewById(R.id.home_recipe_tv_collect);
        collect2 = (TextView) itemView.findViewById(R.id.home_recipe_tv_collect2);
    }
}

class RecipeCategoryTitleViewHolder extends RecyclerView.ViewHolder {

    public ImageView imgView;

    public RecipeCategoryTitleViewHolder(View itemView) {
        super(itemView);
        imgView = (ImageView) itemView.findViewById(R.id.iv_img);
    }
}

