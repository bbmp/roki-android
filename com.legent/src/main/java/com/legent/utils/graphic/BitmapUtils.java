package com.legent.utils.graphic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.common.io.BaseEncoding;
import com.legent.utils.FileUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StreamUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 尽量不要用 BitmapFactory.decodeResource 当使用像
 * <p/>
 * 当使用像imageView.setBackgroundResource，imageView.setImageResource, 或者
 * BitmapFactory.decodeResource 这样的方法来设置一张大图片的时候，
 * 这些函数在完成decode后，最终都是通过java层的createBitmap来完成的，需要消耗更多内存。
 * 因此，改用先通过BitmapFactory.decodeStream方法，创建出一个bitmap，再将其设为ImageView的
 * source，decodeStream最大的秘密在于其直接调用JNI
 * >>nativeDecodeAsset()来完成decode，无需再使用java层的createBitmap
 * ，从而节省了java层的空间。如果在读取时加上图片的Config参数，可以跟有效减少加载的内存，从而跟有效阻止抛out of Memory异常。
 *
 * @author sylar
 */
public class BitmapUtils {

    static BitmapFactory.Options defaultOptions;

    static {
        defaultOptions = new BitmapFactory.Options();
        defaultOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        defaultOptions.inPurgeable = true;
        defaultOptions.inInputShareable = true;
    }

    /**
     * Bitmap缩放
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBySize(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }


    // -----------------------------------------------------------------------------

    /**
     * 图片拼接
     *
     * @param first
     * @param second
     * @return
     */
    public static Bitmap add2Bitmap(Bitmap first, Bitmap second) {
        int width = Math.max(first.getWidth(), second.getWidth());
        int height = first.getHeight() + second.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0, first.getHeight(), null);
        return result;
    }
    //
    // -----------------------------------------------------------------------------

    /**
     * Bitmap转换成Drawable
     *
     * @param cx
     * @param bmp
     * @return
     */
    public static Drawable toDrawable(Context cx, Bitmap bmp) {
        BitmapDrawable bd = new BitmapDrawable(cx.getResources(), bmp);
        return bd;
    }


    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    public static Bitmap cropBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int coreHeight = (int) (h / 1.1);
        int startHeight = h - coreHeight;
        int endHeight = coreHeight -startHeight;
        return Bitmap.createBitmap(bitmap, 0, startHeight, w, endHeight, null, false);
    }


    /**
     * 将Drawable转化为Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap fromDrawable(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    // -----------------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------------

    public static String toBase64(Bitmap bmp) {
        return toBase64(bmp, Bitmap.CompressFormat.JPEG, 80);
    }

    public static String toBase64(Bitmap bmp, int quality) {
        return toBase64(bmp, Bitmap.CompressFormat.JPEG, quality);
    }

    /**
     * 图片转字符串表示
     *
     * @param bmp
     * @param format
     * @param quality 图片质量 0-100
     * @return
     */
    public static String toBase64(Bitmap bmp, Bitmap.CompressFormat format,
                                  int quality) {

        String res = null;
        ByteArrayOutputStream stream = null;

        try {
            stream = new ByteArrayOutputStream();
            bmp.compress(format, quality, stream);
            res = BaseEncoding.base64().encode(stream.toByteArray());
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stream = null;
        }

        return res;
    }

    public static Bitmap fromBase64(String base64String) {
        byte[] bytes = BaseEncoding.base64().decode(base64String);
        return fromBytes(bytes);
    }

    // -----------------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------------

    /**
     * Bitmap 转 byte[]
     *
     * @param bmp
     * @return
     */
    public static byte[] toBytes(Bitmap bmp) {
        byte[] data = toBytes(bmp, Bitmap.CompressFormat.PNG, 100);
        return data;
    }


    /**
     * Bitmap 转 byte[]
     *
     * @param bmp
     * @param format  eg:Bitmap.CompressFormat.PNG
     * @param quality eg:1-100
     * @return
     */
    public static byte[] toBytes(Bitmap bmp, Bitmap.CompressFormat format,
                                 int quality) {
        if (quality < 0 || quality > 100)
            quality = 100;

        InputStream in = StreamUtils.bitmap2stream(bmp, format, quality);
        byte[] data = StreamUtils.stream2Bytes(in);

        return data;
    }

    /**
     * byte[] 转 Bitmap
     */
    public static Bitmap fromBytes(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory
                .decodeByteArray(b, 0, b.length);
    }

    // -----------------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------------

    /**
     * uri to bitmap
     *
     * @param uri
     * @return
     */
    public static Bitmap fromUri(Context cx, Uri uri) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = cx.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            in = null;
        }
        return bitmap;
    }

    // -----------------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------------

    public static Bitmap fromResource(Context cx, int resid) {
        return fromResource(cx, resid, defaultOptions);
    }

    public static Bitmap fromResource(Context cx, int resid,
                                      BitmapFactory.Options option) {
        InputStream is = cx.getResources().openRawResource(resid);
        Bitmap bmp = BitmapFactory.decodeStream(is, null, option);
        return bmp;
    }

    // -----------------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------------

    public static Bitmap fromFile(String fileName) {

        Bitmap bmp = BitmapFactory.decodeFile(fileName, defaultOptions);
        return bmp;
    }

    public static Bitmap fromFile(String fileName, BitmapFactory.Options option) {

        Bitmap bmp = BitmapFactory.decodeFile(fileName, option);
        return bmp;
    }

    public static File toFile(Bitmap bmp, String fullFileName) {

        File file = FileUtils.createFile(fullFileName);

        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file));
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out = null;
        }

        return file;
    }

    /**
     * 按指定尺寸转换资源图片
     *
     * @param reqWidth  请求的宽度（控件宽度）
     * @param reqHeight 请求的高度（控件高度）
     */
    public static Bitmap decodeSampledBitmapFromResource(Context cx, int resId,
                                                         int reqWidth, int reqHeight) {
        Resources res = cx.getResources();

        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeResource(res, resId, options);
        return bmp;
    }

    /**
     * 计算最匹配的采样数
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        options.inJustDecodeBounds = true;
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
