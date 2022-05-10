package com.robam.roki.ui.adapter;

import android.content.Context;
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
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
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


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2018/11/13.
 * PS: Not easy to write code, please indicate.
 */
public class RecipeCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int MAIN_VIEW = 1;
    public static final int OTHER_VIEW = 2;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    private MultiTransformation multiTop = new MultiTransformation<>(new CenterCrop(), new RoundedCornersTransformation(40, 0, RoundedCornersTransformation.CornerType.TOP));
    List<Recipe> mList = new ArrayList<>();
//    DisplayImageOptions.Builder options = new DisplayImageOptions.Builder();
//    private List<Recipe> mList1;

    public OnRecyclerViewItemClickListener getOnRecyclerViewItemClickListener() {
        return mOnRecyclerViewItemClickListener;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public RecipeCategoryAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        if (OTHER_VIEW == viewType) {
            View view = mInflater.inflate(R.layout.view_home_recommandrecipe, parent, false);

            RecipeCategoryViewHolder recipeCategoryViewHolder = new RecipeCategoryViewHolder(view);
            recipeCategoryViewHolder.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecyclerViewItemClickListener != null) {
                        mOnRecyclerViewItemClickListener.onItemClick(v);
                    }
                }
            });

            recipeCategoryViewHolder.iv_collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    boolean isLog = Plat.accountService.isLogon();
                    if (isLog) {
                        Recipe recipe = (Recipe) view.getTag(R.id.tag_recipe_collected);
                        RecipeCategoryViewHolder recipeCategoryViewHolder = (RecipeCategoryViewHolder) view.getTag(R.id.tag_recipe_collected_holder);
                        onItemClick(recipe, recipeCategoryViewHolder);
                    } else {
                        CmccLoginHelper.getInstance().toLogin();
//                        UIService.getInstance().postPage(PageKey.UserLogin);
                    }

                }
            });
            recipeCategoryViewHolder.iv_collection_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    boolean isLog = Plat.accountService.isLogon();
                    if (isLog) {
                        Recipe recipe = (Recipe) view.getTag(R.id.tag_recipe_collected_select);
                        RecipeCategoryViewHolder recipeCategoryViewHolder = (RecipeCategoryViewHolder) view.getTag(R.id.tag_recipe_collected_holder);
                        onItemClick(recipe, recipeCategoryViewHolder);
                    } else {
                        UIService.getInstance().postPage(PageKey.UserLogin);
                    }
                }
            });
            return recipeCategoryViewHolder;
        } else if (MAIN_VIEW == viewType) {
            View view = mInflater.inflate(R.layout.view_home_recommand_recipe, parent, false);

            RecipeCategoryTitleViewHolder titleViewHolder = new RecipeCategoryTitleViewHolder(view);
            return titleViewHolder;
        }
        return null;
    }

    private void onItemClick(final Recipe recipe, final RecipeCategoryViewHolder recipeCategoryViewHolder) {

        CookbookManager cm = CookbookManager.getInstance();
        if (recipe != null) {
            if (recipe.collected) {
                cm.deleteFavorityCookbooks(recipe.id, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        recipe.setIsCollected(false);
                        recipeCategoryViewHolder.iv_collection.setVisibility(View.VISIBLE);
                        recipeCategoryViewHolder.iv_collection_select.setVisibility(View.GONE);
                        recipeCategoryViewHolder.collect.setText("收藏 "  + recipe.collectCount);
                        recipe.collected = false;
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
                        recipeCategoryViewHolder.iv_collection.setVisibility(View.GONE);
                        recipeCategoryViewHolder.iv_collection_select.setVisibility(View.VISIBLE);
                        recipeCategoryViewHolder.collect.setText("收藏 "  + recipe.collectCount);
                        recipe.collected = true;
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


    @Override
    public int getItemViewType(int position) {
//        if (0 == position) {
//            return MAIN_VIEW;
//        } else {
        return OTHER_VIEW;
//        }
    }

    List<Dc> listBefore = null;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof RecipeCategoryViewHolder) {
            RecipeCategoryViewHolder recipeCategoryViewHolder = (RecipeCategoryViewHolder) holder;
            if (mList != null && mList.size() > 0) {
                if (!TextUtils.isEmpty(mList.get(position).stampLogo)) {
                    recipeCategoryViewHolder.logo.setVisibility(View.VISIBLE);
                    ImageUtils.displayImage(mContext, mList.get(position).stampLogo, recipeCategoryViewHolder.logo);
                } else {
                    recipeCategoryViewHolder.logo.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(mList.get(position).providerImage)) {
                    ImageUtils.displayImage(mContext, mList.get(position).providerImage, recipeCategoryViewHolder.source_logo);
                }

                if (!StringUtils.isNullOrEmpty(mList.get(position).imgLarge)) {

                    GlideApp.with(mContext)
                            .load(RecipeUtils.getRecipeImgUrl(mList.get(position)))
                            .apply(RequestOptions.bitmapTransform(multiTop))
                            .into(recipeCategoryViewHolder.imgView);

                } else {

                    recipeCategoryViewHolder.imgView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_default));
                }
                recipeCategoryViewHolder.name.setText(mList.get(position).name);
//                recipeCategoryViewHolder.collect.setText("收藏 " +  NumberUtil.converString(mList.get(position).collectCount) );
//                recipeCategoryViewHolder.collect2.setText("阅读 " + NumberUtil.converString(mList.get(position).viewCount) );
                recipeCategoryViewHolder.collect2.setText("" + NumberUtil.converString(mList.get(position).viewCount) );




                Drawable drawable1 = UiUtils.getResources().getDrawable(R.drawable.hot, null);
                drawable1.setBounds(0, 0, 35, 35);
                recipeCategoryViewHolder.collect2.setCompoundDrawables(drawable1, null, null, null);


//                TextView tvCollection = (TextView) holder.getView(R.id.tv_collection_number);
                List<Dc> dcs = ((Recipe)mList.get(position)).getJs_dcs();
                if (dcs != null && dcs.size() != 0) {
                    Drawable drawable = UiUtils.getResources().getDrawable(DeviceNameHelper.getIcon(dcs), null);
                    drawable.setBounds(0, 0, 35, 35);
                    recipeCategoryViewHolder.collect.setCompoundDrawables(drawable, null, null, null);
                    recipeCategoryViewHolder.collect.setText(DeviceNameHelper.getDeviceName2(dcs));

                } else {

                }
                List<Dc> js_dcs = mList.get(position).getJs_dcs();
                if (0 != js_dcs.size()) {
                    if (listBefore != null) {
                        listBefore.clear();
                    }
                    listBefore = ListUtils.getListBefore(js_dcs);

                    for (int i = 0; i < listBefore.size(); i++) {
                        if (listBefore.size()==1) {
                            recipeCategoryViewHolder.img_device_one.setVisibility(View.VISIBLE);
                            recipeCategoryViewHolder.tv_device_name_one.setVisibility(View.VISIBLE);
                            recipeCategoryViewHolder.img_device_two.setVisibility(View.GONE);
                            recipeCategoryViewHolder.tv_device_name_two.setVisibility(View.GONE);
                            switch (listBefore.get(0).getName()) {
                                case DeviceType.RDKX:
                                    recipeCategoryViewHolder.tv_device_name_one.setText("烤");
                                    recipeCategoryViewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                    break;
                                case DeviceType.RZQL:
                                    recipeCategoryViewHolder.tv_device_name_one.setText("蒸");
                                    recipeCategoryViewHolder.img_device_one.setImageResource(R.mipmap.img_zql_collection);
                                    break;
                                case DeviceType.RWBL:
                                    recipeCategoryViewHolder.tv_device_name_one.setText("微");
                                    recipeCategoryViewHolder.img_device_one.setImageResource(R.mipmap.img_wbl_collection);
                                    break;
                                case DeviceType.RRQZ:
                                    recipeCategoryViewHolder.tv_device_name_one.setText("灶");
                                    recipeCategoryViewHolder.img_device_one.setImageResource(R.mipmap.img_rzz_collection);
                                    break;
                                case DeviceType.RZKY:
                                    recipeCategoryViewHolder.tv_device_name_one.setText("一体");
                                    recipeCategoryViewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                    break;
                                case DeviceType.RIKA:
                                    recipeCategoryViewHolder.tv_device_name_one.setText("RIKA");
                                    recipeCategoryViewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                    break;
                                case DeviceType.KZNZ:
                                    recipeCategoryViewHolder.tv_device_name_one.setText("智能灶");
                                    recipeCategoryViewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                    break;
                                default:
                                    break;

                            }

                        }else{
                            recipeCategoryViewHolder.img_device_one.setVisibility(View.VISIBLE);
                            recipeCategoryViewHolder.tv_device_name_one.setVisibility(View.VISIBLE);
                            recipeCategoryViewHolder.img_device_two.setVisibility(View.VISIBLE);
                            recipeCategoryViewHolder.tv_device_name_two.setVisibility(View.VISIBLE);
                            switch (listBefore.get(0).getName()) {
                                case DeviceType.RDKX:
                                    recipeCategoryViewHolder.tv_device_name_one.setText("烤");
                                    recipeCategoryViewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                    break;
                                case DeviceType.RZQL:
                                    recipeCategoryViewHolder.tv_device_name_one.setText("蒸");
                                    recipeCategoryViewHolder.img_device_one.setImageResource(R.mipmap.img_zql_collection);
                                    break;
                                case DeviceType.RWBL:
                                    recipeCategoryViewHolder.tv_device_name_one.setText("微");
                                    recipeCategoryViewHolder.img_device_one.setImageResource(R.mipmap.img_wbl_collection);
                                    break;
                                case DeviceType.RRQZ:
                                    recipeCategoryViewHolder.tv_device_name_one.setText("灶");
                                    recipeCategoryViewHolder.img_device_one.setImageResource(R.mipmap.img_rzz_collection);
                                    break;
                                case DeviceType.RZKY:
                                    recipeCategoryViewHolder.tv_device_name_one.setText("一体");
                                    recipeCategoryViewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                    break;
                                case DeviceType.RIKA:
                                    recipeCategoryViewHolder.tv_device_name_one.setText("RIKA");
                                    recipeCategoryViewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                    break;
                                case DeviceType.KZNZ:
                                    recipeCategoryViewHolder.tv_device_name_one.setText("智能灶");
                                    recipeCategoryViewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                    break;
                                default:
                                    break;
                            }
                            switch (listBefore.get(1).getName()) {
                                case DeviceType.RDKX:
                                    recipeCategoryViewHolder.tv_device_name_two.setText("烤");
                                    recipeCategoryViewHolder.img_device_two.setImageResource(R.mipmap.img_dkx_collection);
                                    break;
                                case DeviceType.RZQL:
                                    recipeCategoryViewHolder.tv_device_name_two.setText("蒸");
                                    recipeCategoryViewHolder.img_device_two.setImageResource(R.mipmap.img_zql_collection);
                                    break;
                                case DeviceType.RWBL:
                                    recipeCategoryViewHolder.tv_device_name_two.setText("微");
                                    recipeCategoryViewHolder.img_device_two.setImageResource(R.mipmap.img_wbl_collection);
                                    break;
                                case DeviceType.RRQZ:
                                    recipeCategoryViewHolder.tv_device_name_two.setText("灶");
                                    recipeCategoryViewHolder.img_device_two.setImageResource(R.mipmap.img_rzz_collection);
                                    break;
                                case DeviceType.RZKY:
                                    recipeCategoryViewHolder.tv_device_name_two.setText("一体");
                                    recipeCategoryViewHolder.img_device_two.setImageResource(R.mipmap.img_dkx_collection);
                                    break;
                                case DeviceType.RIKA:
                                    recipeCategoryViewHolder.tv_device_name_two.setText("RIKA");
                                    recipeCategoryViewHolder.img_device_two.setImageResource(R.mipmap.img_dkx_collection);
                                    break;
                                case DeviceType.KZNZ:
                                    recipeCategoryViewHolder.tv_device_name_two.setText("智能灶");
                                    recipeCategoryViewHolder.img_device_two.setImageResource(R.mipmap.img_dkx_collection);
                                    break;
                                default:
                                    break;
                            }

                        }
                    }

                }

                if (mList.get(position).collected) {
                    recipeCategoryViewHolder.iv_collection_select.setVisibility(View.VISIBLE);
                    recipeCategoryViewHolder.iv_collection.setVisibility(View.GONE);
                } else {
                    recipeCategoryViewHolder.iv_collection_select.setVisibility(View.GONE);
                    recipeCategoryViewHolder.iv_collection.setVisibility(View.VISIBLE);
                }

            }
            recipeCategoryViewHolder.imgView.setTag(R.id.tag_recipe_bg, mList.get(position));
            recipeCategoryViewHolder.iv_collection.setTag(R.id.tag_recipe_collected, mList.get(position));
            recipeCategoryViewHolder.iv_collection_select.setTag(R.id.tag_recipe_collected_select, mList.get(position));
            recipeCategoryViewHolder.iv_collection.setTag(R.id.tag_recipe_collected_holder, recipeCategoryViewHolder);
            recipeCategoryViewHolder.iv_collection_select.setTag(R.id.tag_recipe_collected_holder, recipeCategoryViewHolder);
        } else if (holder instanceof RecipeCategoryTitleViewHolder) {
            RecipeCategoryTitleViewHolder titleViewHolder = (RecipeCategoryTitleViewHolder) holder;
            if (mList != null && mList.size() > 0) {
                if (!TextUtils.isEmpty(mList.get(position).imgLarge)) {
                    titleViewHolder.imgView.setVisibility(View.VISIBLE);
                    GlideApp.with(mContext)
                            .load(RecipeUtils.getRecipeImgUrl(mList.get(position)))
//                            .apply(RequestOptions.bitmapTransform(multiTop))
                            .into(titleViewHolder.imgView);

                } else {
                    titleViewHolder.imgView.setVisibility(View.GONE);
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }


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

        if (listBefore != null) {
            listBefore.clear();
        }
        notifyDataSetChanged();
    }

    public void addData(List<Recipe> recipeList){
        if (recipeList == null || recipeList.size() == 0) {
            return;
        }
        mList.addAll(recipeList);
        notifyDataSetChanged();
    }

//    public void imgBg(String url) {
//        Recipe recipe = new Recipe();
//        recipe.imgLarge = url;
//        mList1 = new ArrayList<>();
//        mList1.add(recipe);
//    }

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

