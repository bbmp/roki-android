package com.robam.roki.ui.adapter3;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.Categories;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Material;
import com.robam.common.pojos.Materials;
import com.robam.common.pojos.PreSubStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.NumberUtil;
import com.robam.roki.R;
import com.robam.roki.ui.bean3.MaterialSectionItem;
import com.robam.roki.ui.bean3.RecipeDetailItem;
import com.robam.roki.ui.extension.GlideApp;


import com.robam.roki.ui.video.JzvdStdRoundVolume;
import com.robam.roki.ui.video.JzvdStdVolumeAfterFullscreen;
import com.robam.roki.ui.widget.view.PlayerView;
import com.robam.roki.utils.DensityUtil;
import com.robam.roki.utils.DateUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static java.lang.Float.NaN;

/**
 * @author r210190
 * des：菜谱详情列表
 */
public class RvRecipeDetailAdapter extends BaseMultiItemQuickAdapter<RecipeDetailItem, BaseViewHolder> {
    /**
     * 菜谱详情
     */
    private Recipe cookbook;
    private MultiTransformation options = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(40, 0));
    private int step1;
    private int step2;


    private static final String TAG = "RvRecipeDetailAdapter";
    public RvMaterialAdapter rvMaterialAdapter;

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);

        Log.e(TAG, "onViewRecycled");


    }

    private int headPosition = -1;
    private int firstPotion = -1;
    private boolean isSinglePlayer = false;

//    @Override
//    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
//        Log.e(TAG, "onViewAttachedToWindow");
//
//        Log.e(TAG, "onViewAttachedToWindow" + headPosition + "---" + firstPotion);
//        if (holder.getLayoutPosition() == headPosition) {
//            if (holder.getView(R.id.jz_video) instanceof JzvdStd) {
//                LogUtils.i("jz_video", "state ------onViewAttachedToWindow-------- " + ((JzvdStd) holder.getView(R.id.jz_video)).state);
//                if (((JzvdStd) holder.getView(R.id.jz_video)).state == Jzvd.STATE_PAUSE
//                        || ((JzvdStd) holder.getView(R.id.jz_video)).state == Jzvd.SCREEN_NORMAL
//                ) {
//                    ((JzvdStd) holder.getView(R.id.jz_video)).startVideo();
//                }
//            }
//        }
//    }

    //
    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        try {
            Log.e(TAG, "onViewAttachedToWindow" + headPosition + "---" + firstPotion);
            if (holder.getLayoutPosition() == headPosition) {
                if (holder.getView(R.id.jz_video) instanceof JzvdStd) {
                    LogUtils.i("C", "state -------onViewDetachedFromWindow------- " + ((JzvdStd) holder.getView(R.id.jz_video)).state);
                    if (((JzvdStd) holder.getView(R.id.jz_video)).state == Jzvd.STATE_PLAYING) { //播放中
                        ((JzvdStd) holder.getView(R.id.jz_video)).mediaInterface.pause();
                        ((JzvdStd) holder.getView(R.id.jz_video)).onStatePause();
                    } else if (((JzvdStd) holder.getView(R.id.jz_video)).state == Jzvd.STATE_PREPARING) {//预备中
                        ((JzvdStd) holder.getView(R.id.jz_video)).mediaInterface.pause();
                        ((JzvdStd) holder.getView(R.id.jz_video)).onStatePause();
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    private LifecycleOwner owner;

    public RvRecipeDetailAdapter(LifecycleOwner owner) {


        this.owner = owner;
        addItemType(RecipeDetailItem.IMAGE, R.layout.item_image_h);
        addItemType(RecipeDetailItem.VIDEO, R.layout.item_video);
        addItemType(RecipeDetailItem.VIDEO_H, R.layout.item_video_horizontal);
        addItemType(RecipeDetailItem.MATERIALS, R.layout.item_cook_materials);
        addItemType(RecipeDetailItem.PRE_STEPS, R.layout.item_cook_step);
        addItemType(RecipeDetailItem.STEPS, R.layout.item_cook_step);
        addItemType(RecipeDetailItem.STEPS_VIDEO, R.layout.item_cook_step_detail_video);
        addItemType(RecipeDetailItem.RECIPE, R.layout.item_recipe_detail_foot);
    }

    public RvRecipeDetailAdapter() {

//        addItemType(RecipeDetailItem.IMAGE, R.layout.item_image);
        addItemType(RecipeDetailItem.IMAGE, R.layout.item_image_h);
        addItemType(RecipeDetailItem.VIDEO, R.layout.item_video);
        addItemType(RecipeDetailItem.VIDEO_H, R.layout.item_video_horizontal);
        addItemType(RecipeDetailItem.MATERIALS, R.layout.item_cook_materials);
        addItemType(RecipeDetailItem.PRE_STEPS, R.layout.item_cook_step);
        addItemType(RecipeDetailItem.STEPS, R.layout.item_cook_step);
        addItemType(RecipeDetailItem.STEPS_VIDEO, R.layout.item_cook_step_detail_video);
        addItemType(RecipeDetailItem.RECIPE, R.layout.item_recipe_detail_foot);
    }

    private boolean isPlayer = false;


    @Override
    protected void convert(@NotNull BaseViewHolder holder, RecipeDetailItem multiItemEntity) {
        switch (holder.getItemViewType()) {

            case RecipeDetailItem.VIDEO:
                if (cookbook != null) {
                    holder.setText(R.id.tv_recipe_name, cookbook.name)
                            .setText(R.id.tv_read_number, "阅读 " + NumberUtil.converString(cookbook.viewCount) + "     收藏 " + NumberUtil.converString(cookbook.collectCount));
                }

                String video = multiItemEntity.video;
                PlayerView playerView = (PlayerView) holder.getView(R.id.pv_video_play_view);
                playerView.setVideoSource(video);
                playerView.start();
                break;
            case RecipeDetailItem.VIDEO_H:
                if (headPosition == -1) {
                    headPosition = holder.getLayoutPosition();
                }
                String video_h = multiItemEntity.video;
                JzvdStd jzvdStd = (JzvdStd) holder.getView(R.id.jz_video);
                jzvdStd.setUp(video_h
                        , "", JzvdStd.SCREEN_NORMAL, JZMediaIjk.class);
                jzvdStd.startVideo();
                Glide.with(getContext())
                        .setDefaultRequestOptions(
                                new RequestOptions()
                                        .frame(1000000)
                                        .centerCrop()
//                                            .error(R.mipmap.eeeee)//可以忽略
//                                            .placeholder(R.mipmap.ppppp)//可以忽略
                        )
                        .load(video_h)
                        .into(jzvdStd.posterImageView);
                try {
                    float scale = getScale(video_h);
                    if (scale != 0) {
                        WindowManager wm = (WindowManager) getContext()
                                .getSystemService(Context.WINDOW_SERVICE);
                        int width = wm.getDefaultDisplay().getWidth();
                        int width_n = (int) (width * scale);
                        if (width_n == 0) {
                            setViewLayoutParams(jzvdStd, DensityUtil.dip2px(getContext(), 200));
                        } else {
                            setViewLayoutParams(jzvdStd, (int) (width * scale));
                        }
                    } else {
                        setViewLayoutParams(jzvdStd, DensityUtil.dip2px(getContext(), 200));
                    }
                } catch (Exception e) {
                    e.getMessage();
                    setViewLayoutParams(jzvdStd, DensityUtil.dip2px(getContext(), 200));
                }

                break;
            case RecipeDetailItem.IMAGE:
                String image = multiItemEntity.video;
                ImageView imageView = (ImageView) holder.getView(R.id.iv_image);
                GlideApp.with(getContext())
                        .load(image)
                        .placeholder(R.mipmap.icon_recipe_default)
                        .error(R.mipmap.icon_recipe_default)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageView);
                break;
            case RecipeDetailItem.MATERIALS:
                if (cookbook != null) {
                    holder.setText(R.id.tv_recipe_name, cookbook.name)
                            .setText(R.id.tv_recipe_desc, cookbook.desc)
                            .setText(R.id.tv_read_number, "阅读 " + NumberUtil.converString(cookbook.viewCount) + "     收藏 " + NumberUtil.converString(cookbook.collectCount))
                            .setText(R.id.tv_difficulty, "难度：" + (cookbook.difficulty < 3 ? "简单" : (cookbook.difficulty > 4 ? "较难" : "适中")))
                            .setText(R.id.tv_time, "时间：" + DateUtil.secToTime1(cookbook.needTime));
                    if (cookbook.categories != null && cookbook.categories.size() != 0) {
                        String name = "";
                        for (Categories category : cookbook.categories) {
                            name = name + category.name;
                            if (category.alterNativeCategories != null && category.alterNativeCategories.size() != 0) {
                                for (Categories alterNativeCategory : category.alterNativeCategories) {
                                    name = name + "/" + alterNativeCategory.name;
                                }
                            }
                        }
                        holder.setText(R.id.tv_device, "设备：" + name);
                    } else {
                        holder.setGone(R.id.tv_device, true);
                    }
                    holder.setGone(R.id.tv_time, cookbook.needTime == 0);
                    RecyclerView rvMaterials = (RecyclerView) holder.getView(R.id.rv_materials);
                    rvMaterials.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                    Materials materials = multiItemEntity.materials;
                    if (materials != null) {
                        ArrayList<MaterialSectionItem> materialSectionItems = new ArrayList<>();
                        List<Material> main = materials.getMain();
                        if (main != null && !main.isEmpty()) {
                            materialSectionItems.add(new MaterialSectionItem(true, "食材"));
                        }
                        for (Material material :
                                main) {
                            materialSectionItems.add(new MaterialSectionItem(false, material));
                        }
                        List<Material> accessory = materials.getAccessory();
                        if (accessory != null && !accessory.isEmpty()) {
                            materialSectionItems.add(new MaterialSectionItem(true, "佐料"));
                        }
                        for (Material material :
                                accessory) {
                            materialSectionItems.add(new MaterialSectionItem(false, material));
                        }
                        rvMaterialAdapter = new RvMaterialAdapter(materialSectionItems);
                        rvMaterials.setAdapter(rvMaterialAdapter);
                        if(main.isEmpty()&&accessory.isEmpty()){
                            LinearLayout ll_materials = (LinearLayout) holder.getView(R.id.ll_materials);
                            ll_materials.setVisibility(View.GONE);
                        }
                    } else if (materials == null || ((materials.getMain() == null || materials.getMain().isEmpty()) && (materials.getAccessory() == null || materials.getAccessory().isEmpty()))) {
                        LinearLayout ll_materials = (LinearLayout) holder.getView(R.id.ll_materials);
                        ll_materials.setVisibility(View.GONE);
                    }
                }
                break;
            case RecipeDetailItem.PRE_STEPS:
                PreSubStep preSubStep = multiItemEntity.preSubStep;
                holder.setText(R.id.tv_step, "步骤" + preSubStep.order + "/" + step1);
                holder.setText(R.id.tv_step_desc, preSubStep.desc);
                ImageView ivImage = (ImageView) holder.getView(R.id.iv_image);


                try {
                    float scale = getScale(preSubStep.imageUrl);
                    if (scale != 0) {
                        WindowManager wm = (WindowManager) getContext()
                                .getSystemService(Context.WINDOW_SERVICE);
                        int width = wm.getDefaultDisplay().getWidth();
                        setViewLayoutParams(ivImage, (int) ((width - DensityUtil.dip2px(getContext(), 52)) * scale));
                    } else {
                        setViewLayoutParams(ivImage, DensityUtil.dip2px(getContext(), 200));
                    }
                    GlideApp.with(getContext())
                            .load(preSubStep.imageUrl)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                                    //资源加载失败
                                    ivImage.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                                    return false;
                                }
                            })
                            .placeholder(R.mipmap.icon_recipe_default)
                            .error(R.mipmap.icon_recipe_default)
                            .priority(Priority.HIGH)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .apply(RequestOptions.bitmapTransform(options))
                            .into(ivImage);
                } catch (Exception e) {
                    e.getMessage();
                }
                int itemPosition = holder.getLayoutPosition();
                if (itemPosition < getItemCount() - 1) {
                    RecipeDetailItem item = getItem(itemPosition + 1);
                    if ((item.getItemType() == RecipeDetailItem.STEPS || item.getItemType() == RecipeDetailItem.STEPS_VIDEO) && (item.cookStep != null && !item.cookStep.isPrepareStep)) {
                        holder.setVisible(R.id.tv_pengren, true);
                    }
                    if (!preSubStep.isPrepareStep) {
                        holder.getView(R.id.tv_pengren).setVisibility(View.GONE);
                    }
                }
//                if (itemPosition < getItemCount() - 1) {
//                    RecipeDetailItem item = getItem(itemPosition + 1);
//                    if ((item.getItemType() == RecipeDetailItem.STEPS || item.getItemType() == RecipeDetailItem.STEPS_VIDEO) && item.cookStep != null) {
//                        holder.setVisible(R.id.tv_pengren, true);
//                    }
//                    if (!preSubStep.isPrepareStep) {
//                        holder.getView(R.id.tv_pengren).setVisibility(View.GONE);
//                    }
//                }
                break;
            case RecipeDetailItem.STEPS:
                CookStep cookStep = multiItemEntity.cookStep;

                holder.setText(R.id.tv_step, "步骤" + (cookStep.order) + "/" + step2);
                holder.setText(R.id.tv_step_desc, cookStep.desc);
                ivImage = (ImageView) holder.getView(R.id.iv_image);
                GlideApp.with(getContext())
                        .load(cookStep.imageUrl)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                                //资源加载失败
                                ivImage.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                                return false;
                            }
                        })
                        .placeholder(R.mipmap.icon_recipe_default)
                        .error(R.mipmap.icon_recipe_default)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .apply(RequestOptions.bitmapTransform(options))
                        .into(ivImage);
                itemPosition = holder.getLayoutPosition();
//                if (itemPosition < getItemCount() - 1) {
//                    RecipeDetailItem item = getItem(itemPosition + 1);
//                    if ((item.getItemType() == RecipeDetailItem.STEPS || item.getItemType() == RecipeDetailItem.STEPS_VIDEO) && !item.cookStep.isPrepareStep) {
//                        holder.setVisible(R.id.tv_pengren, true);
//                    }
//                    if (!cookStep.isPrepareStep) {
                holder.getView(R.id.tv_pengren).setVisibility(View.GONE);
//                    }
//                }
                break;
            case RecipeDetailItem.STEPS_VIDEO:
                if (firstPotion == -1) {
                    firstPotion = holder.getLayoutPosition();
                }
                if (multiItemEntity.cookStep != null) {
                    CookStep cookStep_video = multiItemEntity.cookStep;
                    holder.setText(R.id.tv_step, "步骤" + cookStep_video.order + "/" + step2);
                    holder.setText(R.id.tv_step_desc, cookStep_video.desc);

                    JzvdStdRoundVolume jzvdStd2 = (JzvdStdRoundVolume) holder.getView(R.id.jz_video);
                    jzvdStd2.setUp(cookStep_video.stepVideo
                            , "", JzvdStd.SCREEN_NORMAL, JZMediaIjk.class);
                    try {
                        float scale = getScale(cookStep_video.stepVideo);
                        if (scale != 0) {
                            WindowManager wm = (WindowManager) getContext()
                                    .getSystemService(Context.WINDOW_SERVICE);
                            int width = wm.getDefaultDisplay().getWidth();
                            setViewLayoutParams(jzvdStd2, (int) ((width - DensityUtil.dip2px(getContext(), 52)) * scale));
                        } else {
                            setViewLayoutParams(jzvdStd2, DensityUtil.dip2px(getContext(), 200));
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    Glide.with(getContext())
                            .setDefaultRequestOptions(
                                    new RequestOptions()
                                            .frame(1000000)
                                            .centerCrop()
//                                            .error(R.mipmap.eeeee)//可以忽略
//                                            .placeholder(R.mipmap.ppppp)//可以忽略
                            )
                            .load(cookStep_video.stepVideo)
                            .into(jzvdStd2.posterImageView);

//                    int itemPosition1 = holder.getLayoutPosition();
//                    if (itemPosition1 < getItemCount() - 1) {
//                        RecipeDetailItem item1 = getItem(itemPosition1 + 1);
//                        if ((item1.getItemType() == RecipeDetailItem.STEPS || item1.getItemType() == RecipeDetailItem.STEPS_VIDEO) && item1.cookStep.isPrepareStep) {
//                            holder.setVisible(R.id.tv_pengren, true);
//                        }
//                        if (cookStep_video.isPrepareStep) {
//                            holder.getView(R.id.tv_pengren).setVisibility(View.GONE);
//                        }
//                    }
                }
                if (multiItemEntity.preSubStep != null) {
                    PreSubStep cookStep_video = multiItemEntity.preSubStep;
                    holder.setText(R.id.tv_step, "步骤" + cookStep_video.order + "/" + step1);
                    holder.setText(R.id.tv_step_desc, cookStep_video.desc);

                    JzvdStdRoundVolume playerView_steps = (JzvdStdRoundVolume) holder.getView(R.id.jz_video);
                    playerView_steps.setUp(cookStep_video.stepVideo
                            , "", JzvdStd.SCREEN_NORMAL, JZMediaIjk.class);
                    try {
                        float scale = getScale(cookStep_video.stepVideo);
                        if (scale != 0) {
                            WindowManager wm = (WindowManager) getContext()
                                    .getSystemService(Context.WINDOW_SERVICE);
                            int width = wm.getDefaultDisplay().getWidth();
                            setViewLayoutParams(playerView_steps, (int) ((width - DensityUtil.dip2px(getContext(), 52)) * scale));
                        } else {
                            setViewLayoutParams(playerView_steps, DensityUtil.dip2px(getContext(), 200));
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    Glide.with(getContext())
                            .setDefaultRequestOptions(
                                    new RequestOptions()
                                            .frame(1000000)
                                            .centerCrop()
//                                            .error(R.mipmap.eeeee)//可以忽略
//                                            .placeholder(R.mipmap.ppppp)//可以忽略
                            )
                            .load(cookStep_video.stepVideo)
                            .into(playerView_steps.posterImageView);
                    if (cookStep_video.order == step1) {
                        holder.setVisible(R.id.tv_pengren, true);
                    } else {
                        holder.setGone(R.id.tv_pengren, true);
                    }
                }
                break;
            case RecipeDetailItem.RECIPE:
                List<Recipe> recipes = multiItemEntity.recipes;
                RecyclerView rvRecipe = (RecyclerView) holder.getView(R.id.rv_recipe);
                rvRecipe.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                RvRecipeAdapter rvRecipeAdapter = new RvRecipeAdapter();
                rvRecipe.setAdapter(rvRecipeAdapter);
                rvRecipeAdapter.addData(recipes);
                rvRecipeAdapter.addChildClickViewIds(R.id.iv_tag_recipe);
                rvRecipeAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                        switch (view.getId()) {
                            case R.id.iv_tag_recipe:
                                if (onFootItemClickListener != null) {
                                    onFootItemClickListener.onItemClick(position);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
//                holder.setIsRecyclable(false);
                break;
            default:
                break;
        }
//        if (holder != null) {
//            holder.setIsRecyclable(false);
//        }
    }

    public void setCookbook(Recipe cookbook, int step1, int step2) {
        this.cookbook = cookbook;
        this.step1 = step1;
        this.step2 = step2;
    }

    OnFootItemClickListener onFootItemClickListener;

    public interface OnFootItemClickListener {
        void onItemClick(int position);
    }

    public void addOnFootItemClickListener(OnFootItemClickListener onFootItemClickListener) {
        this.onFootItemClickListener = onFootItemClickListener;
    }


    /**
     * 重设 view 的高度
     */
    public void setViewLayoutParams(View view, int nHeight) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
//        int height = DensityUtil.dip2px(getContext(), nHeight);
        if (lp.height != nHeight) {
            lp.height = nHeight;
            view.setLayoutParams(lp);
        }


    }

    private float getScale(String url) {
        Pattern pattern = Pattern.compile("w(\\d+)h(\\d+).mp4");//正则表达式
        Matcher matcher = pattern.matcher(url);
//判断是否匹配到子串
        if (matcher.find()) {
            //宽(w)=1980,高(h)=1080
            String w = matcher.group(1);
            String h = matcher.group(2);
            LogUtils.i("getScale", w + "-----" + h);
            return Float.parseFloat(h) / Float.parseFloat(w);
        } else {
            return 0;
        }
    }


    public void setStopVideo() {
        try {
            JzvdStd.releaseAllVideos();
        } catch (Exception e) {
            e.getMessage();
        }

    }
}
