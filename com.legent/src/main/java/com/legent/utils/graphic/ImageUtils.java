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

import com.legent.utils.api.DisplayUtils;
import com.legent.utils.api.StorageUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

public class ImageUtils {

    public static DisplayImageOptions defaultOptions = getDefaultBuilder()
            .build();

    static ImageLoader loader = ImageLoader.getInstance();

    public static void init(Context cx, String app) {
        if (loader.isInited())
            return;

        File cacheDir = StorageUtils.getCachDir(cx);
        int maxWidth = DisplayUtils.getScreenWidthPixels(cx) / 2;
        int maxHeight = DisplayUtils.getScreenHeightPixels(cx) / 2;

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(cx);
        builder.memoryCache(new WeakMemoryCache());
        builder.defaultDisplayImageOptions(defaultOptions);
        builder.threadPriority(Thread.NORM_PRIORITY - 2);
        builder.diskCache(new UnlimitedDiskCache(cacheDir));//任涛 ImageLoader改成1.9.5 UnlimitedDiscCache 改为 UnlimitedDiskCache
        builder.denyCacheImageMultipleSizesInMemory();// 拒绝缓存同一图片，有不同的大小
        builder.threadPoolSize(5);
        builder.diskCacheExtraOptions(maxWidth, maxHeight, null);
        if (app != null && app.equals("RKPAD"))
            builder.threadPoolSize(3) // default
                    .threadPriority(Thread.NORM_PRIORITY - 1) // default
                    .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                    .memoryCache(new LruMemoryCache(15 * 1024 * 1024))
                    .memoryCacheSize(15 * 1024 * 1024)
                    .memoryCacheSizePercentage(13) // default
                    .diskCacheSize(25 * 1024 * 1024)
                    .diskCacheFileCount(50)
                    .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                    .imageDownloader(new BaseImageDownloader(cx)) // default
                    .imageDecoder(new BaseImageDecoder(true)) // default
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                    .writeDebugLogs();
        ImageLoaderConfiguration config = builder.build();
        loader.init(config);
    }

    public static DisplayImageOptions.Builder getDefaultBuilder() {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.bitmapConfig(Config.RGB_565);
        builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
       // builder.displayer(new RoundedBitmapDisplayer(20));
        builder.cacheInMemory(false);
        builder.cacheOnDisk(true);
        builder.showImageForEmptyUri(null);
        builder.showImageOnFail(null);
        builder.showImageOnLoading(null);
        return builder;
    }

    // -------displayImage

    public static void displayImage(String uri, ImageView view) {
        loader.displayImage(uri, view);
    }

    public static void displayImage(String uri, ImageView view,
                                    DisplayImageOptions options) {
        loader.displayImage(uri, view, options);
    }

    public static void displayImage(String uri, ImageView view,
                                    ImageLoadingListener listener) {
        loader.displayImage(uri, view, listener);
    }

    public static void displayImage(String uri, ImageView view,
                                    DisplayImageOptions options, ImageLoadingListener listener) {
        loader.displayImage(uri, view, options, listener);
    }

    // -------loadImage

    public static void loadImage(String uri, ImageLoadingListener listener) {
        loader.loadImage(uri, listener);
    }

    public static void loadImage(String uri, DisplayImageOptions options,
                                 ImageLoadingListener listener) {
        loader.loadImage(uri, options, listener);
    }

    public static void loadImage(String uri, ImageSize targetImageSize,
                                 ImageLoadingListener listener) {
        loader.loadImage(uri, targetImageSize, listener);
    }

    public static void loadImage(String uri, ImageSize targetImageSize,
                                 DisplayImageOptions options, ImageLoadingListener listener) {
        loader.loadImage(uri, targetImageSize, options, listener);
    }

    // -------loadImageSync

    public static Bitmap loadImageSync(String uri) {
        return loader.loadImageSync(uri);
    }

    public static Bitmap loadImageSync(String uri, DisplayImageOptions options) {
        return loader.loadImageSync(uri, options);
    }

    public static Bitmap loadImageSync(String uri, ImageSize targetImageSize) {
        return loader.loadImageSync(uri, targetImageSize, defaultOptions);
    }

    public static Bitmap loadImageSync(String uri, ImageSize targetImageSize,
                                       DisplayImageOptions options) {
        return loader.loadImageSync(uri, targetImageSize, options);
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
