/**
 * // DON'T COPY THIS CODE TO YOUR PROJECT! This is just example of ALL options using.
 * // See the sample project how to use ImageLoader correctly.
 * <p/>
 * <p/>
 * ImageLoaderConfiguration 示例
 * <p/>
 * <p/>
 * File cacheDir = StorageUtils.getCacheDirectory(context);
 * ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
 * .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
 * .diskCacheExtraOptions(480, 800, null)
 * .taskExecutor(...)
 * .taskExecutorForCachedImages(...)
 * .threadPoolSize(3) // default
 * .threadPriority(Thread.NORM_PRIORITY - 2) // default
 * .tasksProcessingOrder(QueueProcessingType.FIFO) // default
 * .denyCacheImageMultipleSizesInMemory()
 * .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
 * .memoryCacheSize(2 * 1024 * 1024)
 * .memoryCacheSizePercentage(13) // default
 * .diskCache(new UnlimitedDiscCache(cacheDir)) // default
 * .diskCacheSize(50 * 1024 * 1024)
 * .diskCacheFileCount(100)
 * .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
 * .imageDownloader(new BaseImageDownloader(context)) // default
 * .imageDecoder(new BaseImageDecoder()) // default
 * .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
 * .writeDebugLogs()
 * .build();
 * <p/>
 * options 示例
 * <p/>
 * DisplayImageOptions options = new DisplayImageOptions.Builder()
 * .showStubImage(R.drawable.ic_stub)          // 设置图片下载期间显示的图片
 * .showImageForEmptyUri(R.drawable.ic_empty)  // 设置图片Uri为空或是错误的时候显示的图片
 * .showImageOnFail(R.drawable.ic_error)       // 设置图片加载或解码过程中发生错误显示的图片
 * .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
 * .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
 * .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
 * .build();                                   // 创建配置过得DisplayImageOption对象
 * <p/>
 * <p/>
 * <p/>
 * //可接受 uri 类型
 * //String imageUri = "http://site.com/image.png"; // from Web
 * //String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
 * //String imageUri = "content://media/external/audio/albumart/13"; // from content provider
 * //String imageUri = "assets://image.png"; // from assets
 * //String imageUri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
 * <p/>
 * <p/>
 * 用法参见：https://github.com/nostra13/Android-Universal-Image-Loader/tree/v1.9.3
 */

package com.legent.utils.graphic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.widget.ImageView;
import android.widget.SimpleCursorTreeAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.legent.utils.api.DisplayUtils;
import com.legent.utils.api.StorageUtils;

import java.io.File;

public class ImageUtils {

    // -------displayImage
    public static void displayImage(Context context, int resId, ImageView view) {
//        loader.displayImage(uri, view);
        Glide.with(context).load(resId).into(view);
    }

    public static void displayImage(Context context, String uri, ImageView view) {
//        loader.displayImage(uri, view);
        Glide.with(context).load(uri).into(view);
    }

    public static void displayImage(Context context, String uri, ImageView view,
                                    RequestOptions options) {
//        loader.displayImage(uri, view, options);
        Glide.with(context).load(uri).apply(options).into(view);
    }


    // -------loadImage

    public static void loadImage(Context context, String uri, CustomTarget customTarget) {
//        loader.loadImage(uri, listener);
        Glide.with(context).asBitmap().load(uri).into(customTarget);
    }

    // -------image uri

    /**
     * String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
     *
     * @param imageFile
     *            完全路径的图片文件名
     * @return
     */
    public static String fromFile(String imageFile) {
        return String.format("file:///%s", imageFile);
    }

    /**
     * String imageUri = "assets://image.png"; // from assets
     *
     * @param imageFile
     *            assets目录下的图片文件
     * @return
     */
    public static String fromAssets(String imageFile) {
        return String.format("assets://%s", imageFile);
    }

    /**
     * String imageUri = "content://media/external/audio/albumart/13"; // from
     * content provider
     *
     * @param imageFile
     * @return
     */
    public static String fromProvider(String imageFile) {
        return String.format("content://%s", imageFile);
    }

    /**
     * String imageUri = "drawable://" + R.drawable.image; // from drawables
     * (only images, non-9patch)
     *
     * @param imageResid
     * @return
     */
    public static String fromDrawable(int imageResid) {
        return String.format("drawable://%s", imageResid);
    }
}
