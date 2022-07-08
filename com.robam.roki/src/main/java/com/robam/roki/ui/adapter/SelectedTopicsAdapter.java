package com.robam.roki.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.utils.api.DisplayUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.ThemeRecipeMultipleItem;
import com.robam.roki.model.bean.TopicMultipleItem;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.utils.GlideCircleTransform;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class SelectedTopicsAdapter extends BaseMultiItemQuickAdapter<TopicMultipleItem, BaseViewHolder> {

    private List<TopicMultipleItem> topicMultipleItemList;
    RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.img_default) //预加载图片
            .error(R.mipmap.img_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .skipMemoryCache(true)
            .override((int) (190*2), (int) (115*2))
            .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存
//            .transform(new MultiTransformation(new CenterCrop(), new RoundedCornersTransformation(30, 0))); //圆角
    private RequestOptions maskOption = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.icon_recipe_default) //预加载图片
            .error(R.mipmap.icon_recipe_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .skipMemoryCache(true)
            .override(167*2, 160*2)
            .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存
//            .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCornersTransformation(30, 0,RoundedCornersTransformation.CornerType.TOP)));  //圆角


    public SelectedTopicsAdapter() {
        super();
        addItemType(TopicMultipleItem.IMG, R.layout.item_selected_topic);
        addItemType(TopicMultipleItem.TEXT, R.layout.item_more_footer_2);
        addItemType(TopicMultipleItem.IMG_THEME_RECIPE_COLLECT, R.layout.item_recipe_theme_collect);
    }

    public SelectedTopicsAdapter(@Nullable List<TopicMultipleItem> data) {
        super(data);
        this.topicMultipleItemList = data;
        addItemType(TopicMultipleItem.IMG, R.layout.item_selected_topic);
        addItemType(TopicMultipleItem.TEXT, R.layout.item_more_footer_2);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, TopicMultipleItem topicMultipleItem) {
        switch (topicMultipleItem.getItemType()) {
            case TopicMultipleItem.IMG: {
                    ImageView itemTopticsImg = baseViewHolder.getView(R.id.iv_item_topic_img);
                    GlideApp.with(getContext())
                            .load(topicMultipleItem.getContent())
                            .apply(options)
                            .into(itemTopticsImg);
                }
                break;
            case TopicMultipleItem.TEXT:


                break;
            case TopicMultipleItem.IMG_THEME_RECIPE_COLLECT: {
                    ImageView itemTopticsImg = baseViewHolder.getView(R.id.iv_item_topic_img);

                    GlideApp.with(getContext())
                            .load(topicMultipleItem.getContent())
                            .apply(maskOption)
                            .into(itemTopticsImg);
                    baseViewHolder.setText(R.id.tv_recipe_name, topicMultipleItem.getTitle());
                }
                break;
            default:
                break;
        }

    }
}
