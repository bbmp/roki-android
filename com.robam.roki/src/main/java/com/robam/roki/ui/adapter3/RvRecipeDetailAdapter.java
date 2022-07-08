package com.robam.roki.ui.adapter3;

import static com.robam.roki.ui.activity3.recipedetail.PublishListActivityKt.getList;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
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
import com.example.textviewtext.ShowAllSpan;
import com.example.textviewtext.ShowAllTextView;
import com.legent.plat.Plat;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.legent.utils.api.DisplayUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.Categories;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Material;
import com.robam.common.pojos.Materials;
import com.robam.common.pojos.PreSubStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.NumberUtil;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.net.OnRequestListener;
import com.robam.roki.net.request.api.PublishApi;
import com.robam.roki.net.request.bean.AlbumList;
import com.robam.roki.net.request.bean.AlumListBean;
import com.robam.roki.ui.activity3.recipedetail.DeletePublicDialog;
import com.robam.roki.ui.activity3.recipedetail.ImagePreviewActivity;
import com.robam.roki.ui.activity3.recipedetail.PublishListActivity;
import com.robam.roki.ui.activity3.recipedetail.PublishedWorksActivity;
import com.robam.roki.ui.bean3.MaterialSectionItem;
import com.robam.roki.ui.bean3.RecipeDetailItem;
import com.robam.roki.ui.extension.GlideApp;


import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.video.JzvdStdRoundVolume;
import com.robam.roki.ui.view.nineview.NineCookedView;
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
import skin.support.content.res.SkinCompatResources;

/**
 * @author r210190
 * des：菜谱详情列表
 */
public class RvRecipeDetailAdapter extends BaseMultiItemQuickAdapter<RecipeDetailItem, BaseViewHolder> implements OnRequestListener {
    /**
     * 菜谱详情
     */
    private Recipe cookbook;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.icon_recipe_default) //预加载图片
            .error(R.mipmap.icon_recipe_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .skipMemoryCache(true)
            .format(DecodeFormat.PREFER_RGB_565)
            .diskCacheStrategy(DiskCacheStrategy.NONE);
//            .transform(new MultiTransformation(new CenterCrop(), new RoundedCornersTransformation(30, 0)));

    private int step1;
    private int step2;


    PublishApi mPublishApi;


    private static final String TAG = "RvRecipeDetailAdapter";
    public RvMaterialAdapter rvMaterialAdapter;


    private int headPosition = -1;
    private int firstPotion = -1;
    private boolean isSinglePlayer = false;


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

        mPublishApi = new PublishApi(this);
        addItemType(RecipeDetailItem.IMAGE, R.layout.item_image_h);
        addItemType(RecipeDetailItem.VIDEO, R.layout.item_video);
        addItemType(RecipeDetailItem.VIDEO_H, R.layout.item_video_horizontal);
        addItemType(RecipeDetailItem.MATERIALS, R.layout.item_cook_materials);
        addItemType(RecipeDetailItem.PRE_STEPS, R.layout.item_cook_step);
        addItemType(RecipeDetailItem.STEPS, R.layout.item_cook_step);
        addItemType(RecipeDetailItem.STEPS_VIDEO, R.layout.item_cook_step_detail_video);
        addItemType(RecipeDetailItem.SHOW, R.layout.item_recipe_show);
        addItemType(RecipeDetailItem.RECIPE, R.layout.item_recipe_detail_foot);

    }


    private boolean isPlayer = false;


    @Override
    protected void convert(@NotNull BaseViewHolder holder, RecipeDetailItem multiItemEntity) {
        switch (holder.getItemViewType()) {

            case RecipeDetailItem.VIDEO:
                if (cookbook != null) {
                    holder.setText(R.id.tv_recipe_name, cookbook.name)
                            .setText(R.id.tv_read_number, "阅读 " + NumberUtil.converString(cookbook.viewCount));
                    holder.setText(R.id.tv_collect_number,  "收藏 " + NumberUtil.converString(cookbook.collectCount));
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
                setHeadImage(image, imageView);
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
                        materialSectionItems.add(new MaterialSectionItem(true, "食材"));
                        List<Material> main = materials.getMain();
                        for (Material material :
                                main) {
                            materialSectionItems.add(new MaterialSectionItem(false, material));
                        }
                        materialSectionItems.add(new MaterialSectionItem(true, "佐料"));
                        List<Material> accessory = materials.getAccessory();
                        for (Material material :
                                accessory) {
                            materialSectionItems.add(new MaterialSectionItem(false, material));
                        }
                        rvMaterialAdapter = new RvMaterialAdapter(materialSectionItems);
                        rvMaterials.setAdapter(rvMaterialAdapter);
                    }
                }
                if (cookbook.preStep == null || cookbook.preStep.getPreSubSteps() == null || cookbook.preStep.getPreSubSteps().size() == 0) {
                    holder.setText(R.id.tv_step_type , "烹饪");
                }
                break;
            case RecipeDetailItem.PRE_STEPS:
                PreSubStep preSubStep = multiItemEntity.preSubStep;
                holder.setText(R.id.tv_step, "步骤" + preSubStep.order + "/" + step1);
                holder.setText(R.id.tv_step_desc, preSubStep.desc);
                ImageView ivImage = (ImageView) holder.getView(R.id.iv_image);
                String imageUrl = preSubStep.imageUrl;
                setStepImage(imageUrl, ivImage);


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

                break;
            case RecipeDetailItem.STEPS:
                CookStep cookStep = multiItemEntity.cookStep;
                holder.setText(R.id.tv_step, "步骤" + (cookStep.order) + "/" + step2);
                holder.setText(R.id.tv_step_desc, cookStep.desc);
                ivImage = (ImageView) holder.getView(R.id.iv_image);
                String imageUrl1 = cookStep.imageUrl;
                setStepImage(imageUrl1, ivImage);
                holder.getView(R.id.tv_pengren).setVisibility(View.GONE);
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
                            setViewLayoutParams(jzvdStd2, (int) ((width - DensityUtil.dip2px(getContext(), 26)) * scale));
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
                                            .centerCrop())
                            .load(cookStep_video.stepVideo)
                            .into(jzvdStd2.posterImageView);


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
                            setViewLayoutParams(playerView_steps, (int) ((width - DensityUtil.dip2px(getContext(), 26)) * scale));
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
                RvRecipeAdapter rvRecipeAdapter = new RvRecipeAdapter(getContext());
                rvRecipe.setAdapter(rvRecipeAdapter);
                rvRecipeAdapter.addData(recipes);
                rvRecipeAdapter.addChildClickViewIds(R.id.iv_tag_recipe);
                rvRecipeAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                    switch (view.getId()) {
                        case R.id.iv_tag_recipe:
                            if (onFootItemClickListener != null) {
                                onFootItemClickListener.onItemClick(position);
                            }
                            break;
                        default:
                            break;
                    }
                });
                break;
            case RecipeDetailItem.SHOW:
                //

                AlumListBean mAlumListBean = multiItemEntity.mAlumListBean;

                holder.getView(R.id.item_recipe_show_work).setOnClickListener(v -> {
                    if (Plat.accountService.isLogon()) {
                        Intent intent = new Intent(getContext(), PublishedWorksActivity.class);
                        intent.putExtra(PublishedWorksActivity.getPUBLICRECIPEID(), cookbook.id);
                        getContext().startActivity(intent);
                    } else {
                        CmccLoginHelper.getInstance().toLogin();
                    }
                });

                if (mAlumListBean.getAlbumList() == null || mAlumListBean.getAlbumList().size() == 0) {
                    holder.getView(R.id.item_show_cook_1).setVisibility(View.GONE);
                    holder.getView(R.id.item_show_cook_2).setVisibility(View.GONE);
                    holder.getView(R.id.item_show_cook_rl_empty).setVisibility(View.VISIBLE);
                    holder.getView(R.id.item_show_cook_more_txt).setVisibility(View.GONE);
                } else if (mAlumListBean.getAlbumList().size() == 1) {
                    holder.getView(R.id.item_show_cook_2).setVisibility(View.GONE);
                    render(holder.getView(R.id.item_show_cook_1), holder.getLayoutPosition(), mAlumListBean.getAlbumList().get(0), 0);
                } else if (mAlumListBean.getAlbumList().size() >= 2) {
                    render(holder.getView(R.id.item_show_cook_1), holder.getLayoutPosition(), mAlumListBean.getAlbumList().get(0), 0);
                    render(holder.getView(R.id.item_show_cook_2), holder.getLayoutPosition(), mAlumListBean.getAlbumList().get(1), 1);
                }
                if (mAlumListBean.getAlbumList() != null && !mAlumListBean.getAlbumList().isEmpty()) {
                    ((TextView) holder.getView(R.id.item_show_cook_more_txt)).setText("查看全部" + mAlumListBean.getAlbumList().size() + "个作品");
                }

                holder.getView(R.id.item_show_cook_more_txt).setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), PublishListActivity.class);
                    intent.putExtra(PublishListActivity.Companion.getRECIPENAME(), cookbook.name);
                    intent.putExtra(PublishListActivity.Companion.getRECIPEID(), cookbook.id);
                    getContext().startActivity(intent);
                });
                break;
            default:
                break;
        }
//        if (holder != null) {
//            holder.setIsRecyclable(false);
//        }
    }

    private int mPos = -1;
    private TextView praiseNum;
    private ImageView praiseImgStatus;

    int mPosShow;

    int mHeadPosition;

    private void render(View view, int headPosition, AlbumList mAlbumList, int pos) {
        ImageView headImg = view.findViewById(R.id.item_head_image);
//        this.mHeadPosition=headPosition;

        Glide.with(getContext()).load(mAlbumList.getOwnerFigureUrl())
                .placeholder(R.mipmap.ic_user_default_figure)
                .into(headImg);
//        view.findViewById(R.id.item_nine_image_delete).setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mAlbumList.getOwnerId())
                && Long.parseLong(mAlbumList.getOwnerId()) == (Plat.accountService.getCurrentUserId())) {
            view.findViewById(R.id.item_nine_image_delete).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.item_nine_image_delete).setVisibility(View.GONE);
        }

        view.findViewById(R.id.item_nine_image_delete).setOnClickListener(v -> {


            DeletePublicDialog mDeletePublicDialog = new DeletePublicDialog(getContext(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mPosShow = pos;
                    mHeadPosition = headPosition;
                    mPublishApi.deletePublic(R.layout.cooker_recipe_desc_show_item, Long.parseLong(mAlbumList.getId()));
                }
            });
            mDeletePublicDialog.create();
            mDeletePublicDialog.show();

        });
        ((TextView) view.findViewById(R.id.item_name_show_cook)).setText(mAlbumList.getOwnerName());
        ShowAllTextView txtView = view.findViewById(R.id.item_show_cooked_txt_context);

        txtView.setTextColor(SkinCompatResources.getColor(getContext(), R.color.text_color_net_err));
        if (mAlbumList.getId() != null) {
            if (!(Long.parseLong(mAlbumList.getId()) == Plat.accountService.getCurrentUserId())) {
                txtView.setMaxLineText(mAlbumList.getContent());
                txtView.setMaxShowLines(4);

                txtView.setOnAllSpanClickListener(view1 -> {
                    txtView.setMaxLineText(mAlbumList.getContent());
                    txtView.setMaxShowLines(9999);
                });
            } else {
                txtView.setMaxLineText(mAlbumList.getContent());
                txtView.setMaxShowLines(9999);
            }
        }

        view.findViewById(R.id.img_thumbs_up_publish).setOnClickListener(v -> {
            if (Plat.accountService.isLogon()) {
                praiseNum = ((TextView) view.findViewById(R.id.item_name_show_cook_num));
                praiseImgStatus = view.findViewById(R.id.img_thumbs_up_publish);
                if (view.findViewById(R.id.img_thumbs_up_publish).isSelected()) {

                    mPublishApi.noPraise(R.id.img_thumbs_up_publish, Long.parseLong(mAlbumList.getId()));
                } else {

                    mPublishApi.praisePublic(R.id.img_thumbs_up_publish, Long.parseLong(mAlbumList.getId()));
                }
            } else {
                CmccLoginHelper.getInstance().toLogin();
            }
        });
        view.findViewById(R.id.img_thumbs_up_publish).setSelected(mAlbumList.getHasPraised());


        ((TextView) view.findViewById(R.id.item_name_show_cook_num)).setText(mAlbumList.getPraiseCount());


        NineCookedView nineGridTestLayout = view.findViewById(R.id.item_nine_image);
        nineGridTestLayout.setShowMore(true);
        nineGridTestLayout.setListener((itemPostion, position, url, urlList, imageView) -> {
            Intent intent = new Intent(getContext(), ImagePreviewActivity.class);
            intent.putStringArrayListExtra("imageList", (ArrayList<String>) urlList);
            intent.putExtra(ImagePreviewActivity.P.START_ITEM_POSITION, itemPostion);
            intent.putExtra(ImagePreviewActivity.P.START_IAMGE_POSITION, position);
            getContext().startActivity(intent);


        });
        nineGridTestLayout.setItemPosition(headPosition);
        nineGridTestLayout.setSpacing(15);
        if (mAlbumList.getCookingAlbumFileDtoList() != null) {
            nineGridTestLayout.setUrlList(getList(mAlbumList.getCookingAlbumFileDtoList()));
        }
    }

    public void setCookbook(Recipe cookbook, int step1, int step2) {
        this.cookbook = cookbook;
        this.step1 = step1;
        this.step2 = step2;
    }

    OnFootItemClickListener onFootItemClickListener;

    @Override
    public void onFailure(int requestId, int requestCode, @Nullable String msg, @Nullable Object data) {
        if (R.layout.cooker_recipe_desc_show_item == requestId) {

            ToastUtils.show(msg, Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onSaveCache(int requestId, int requestCode, @Nullable Object paramObject) {

    }

    @Override
    public void onSuccess(int requestId, int requestCode, @Nullable Object paramObject) {

        if (requestId == R.id.img_thumbs_up_publish) {

            if (praiseImgStatus.isSelected()) {
                praiseNum.setText(Long.parseLong(praiseNum.getText().toString()) - 1 + "");
                praiseImgStatus.setSelected(false);
            } else {
                praiseNum.setText(Long.parseLong(praiseNum.getText().toString()) + 1 + "");
                praiseImgStatus.setSelected(true);
            }
        } else if (R.layout.cooker_recipe_desc_show_item == requestId) {
            notifyDataSetChanged();
        }
    }

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

    private float getScaleJpg(String url) {
        Pattern pattern = Pattern.compile("w(\\d+)h(\\d+).jpg");//正则表达式
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

    private void setHeadImage(String imgUrl, ImageView imageView) {
        if (cookbook.length_width != 0){
            WindowManager wm = (WindowManager) getContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            setViewLayoutParams(imageView, (int) ((width) * cookbook.length_width));
        }
        GlideApp.with(MobApp.getInstance())
                .load(imgUrl)
                .listener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        //资源加载失败
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        if (drawable != null) {
                            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                            Log.e("size = ", "size+"+bitmapDrawable.getBitmap().getByteCount());
                            Log.e("size = ", "width+"+bitmapDrawable.getBitmap().getWidth());
                            Log.e("size = ", "height+"+bitmapDrawable.getBitmap().getHeight());
                        }
                        if (cookbook.length_width == 0){
                            float intrinsicWidth = drawable.getMinimumWidth();
                            float intrinsicHeight = drawable.getMinimumHeight();
                            WindowManager wm = (WindowManager) getContext()
                                    .getSystemService(Context.WINDOW_SERVICE);
                            float scale = intrinsicHeight / intrinsicWidth;
//                                int width = wm.getDefaultDisplay().getWidth();
                            int width = imageView.getWidth();
                            setViewLayoutParams(imageView, (int) ((width) * scale));
                        }
                        return false;
                    }
                })
                .apply(options)
                .into(imageView);
    }

    private void setStepImage(String imgUrl, ImageView imageView) {
        try {
            if (imgUrl == null || imgUrl.length() == 0) {
                imageView.setVisibility(View.GONE);
            } else {
                imageView.setVisibility(View.VISIBLE);
                float scale = getScaleJpg(imgUrl);
                if (scale != 0) {
                    WindowManager wm = (WindowManager) getContext()
                            .getSystemService(Context.WINDOW_SERVICE);
                    int width = wm.getDefaultDisplay().getWidth();
//                            int width = ivImage.getWidth();
                    setViewLayoutParams(imageView, (int) ((width - DensityUtil.dip2px(MobApp.getInstance(), 32)) * scale));
                } else {
                    setViewLayoutParams(imageView, DensityUtil.dip2px(MobApp.getInstance(), 200));
                }
                GlideApp.with(getContext())
                        .load(imgUrl)
                        .listener(new RequestListener<Drawable>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                                //资源加载失败
                                imageView.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                                float intrinsicWidth = drawable.getMinimumWidth();
                                float intrinsicHeight = drawable.getMinimumHeight();

                                float scale = intrinsicHeight / intrinsicWidth;
                                int width = imageView.getWidth();
                                setViewLayoutParams(imageView, (int) ((width) * scale));
                                return false;
                            }
                        })
                        .apply(options)
                        .into(imageView);
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }
}
