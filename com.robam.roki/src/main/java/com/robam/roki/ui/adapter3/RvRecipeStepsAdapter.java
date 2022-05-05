package com.robam.roki.ui.adapter3;

import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.common.pojos.CookStep;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.widget.view.PlayerView;

import org.jetbrains.annotations.NotNull;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author r210190
 * des 菜谱详情烹饪步骤adapter
 */
public class RvRecipeStepsAdapter extends BaseMultiItemQuickAdapter<CookStep, BaseViewHolder> implements PlayerView.onPlayListener{
    PlayerView playerView ;

    private MultiTransformation options = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(40, 0));

    public RvRecipeStepsAdapter() {
//        super(R.layout.item_cook_step_image);
        addItemType(CookStep.IMAGE, R.layout.item_cook_step_image);
        addItemType(CookStep.VIDEO, R.layout.item_cook_step_video);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, CookStep item) {
        switch (holder.getItemViewType()){
            case CookStep.IMAGE:
                if (item != null) {
                    ImageView ivImage = (ImageView) holder.getView(R.id.iv_image);
                    GlideApp.with(getContext())
                            .load(item.imageUrl)
                            .placeholder(R.mipmap.icon_recipe_default)
                            .error(R.mipmap.icon_recipe_default)
                            .priority(Priority.HIGH)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .apply(RequestOptions.bitmapTransform(options))
                            .into(ivImage);
                }
                break;
            case CookStep.VIDEO:
                String video = item.stepVideo;
                 playerView = (PlayerView) holder.getView(R.id.pv_video_play_view);
//                playerView.setLifecycleOwner(playerView);
                playerView.setVideoSource(video);
                playerView.start();
                break;
            default:
                break;
        }

    }
}
