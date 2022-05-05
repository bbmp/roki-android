package com.robam.roki.ui.view.recipeclassify;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;
//import com.youth.banner.loader.ImageLoader;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class GlideImageLoader
//        extends ImageLoader
{
//    DisplayImageOptions.Builder options = new DisplayImageOptions.Builder();
    RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.img_default) //预加载图片
            .error(R.mipmap.img_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
            .transform(new RoundedCornersTransformation(12, 0)); //圆角
    public void displayImage(final Context context, Object path, ImageView imageView) {


//        ImageUtils.displayImage((String)path, imageView, options.displayer(new RoundedBitmapDisplayer(20))
//                .showImageOnLoading(R.mipmap.img_default).build());
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        Glide.with(context)
//                .asBitmap()
//                .load( path)
//                .transform(new CenterCrop(), new RoundTransformation(context, 15))
//                .into(imageView);

        GlideApp.with(context)
                .load(path)
                .circleCrop()
                .apply(options)
                .into(imageView);

    }

}
