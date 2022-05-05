package com.legent.utils.graphic;

import com.legent.utils.api.DisplayUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.GradientDrawable;
import android.media.ThumbnailUtils;

public class BitmapCreator {

	/**
	 * 创建缩略图
	 *
	 * @param src
	 * @param width
	 * @param height
	 * @param opts
	 * @return
	 */
	public static Bitmap createThumbnail(Bitmap src, int width, int height) {
		return ThumbnailUtils.extractThumbnail(src, width, height);
	}

	/**
	 * 根据原图和直径绘制圆形图片
	 *
	 * @param source
	 * @param min
	 * @return
	 */
	public static Bitmap createCircleImage(Bitmap source, int min) {
		try {
			final Paint paint = new Paint();
			paint.setAntiAlias(true);
			Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
			/**
			 * 产生一个同样大小的画布
			 */
			Canvas canvas = new Canvas(target);
			/**
			 * 首先绘制圆形
			 */
			canvas.drawCircle(min / 2, min / 2, min / 2, paint);
			/**
			 * 使用SRC_IN
			 */
			paint.setXfermode(new PorterDuffXfermode(
					android.graphics.PorterDuff.Mode.SRC_IN));
			/**
			 * 绘制图片
			 */
			canvas.drawBitmap(source, 0, 0, paint);
			return target;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 * @param cx
	 * @param source
	 * @param radiusDip
	 * @return
	 */
	public static Bitmap createMaskImage(Context cx, Bitmap source,
			float radiusDip) {
		int width = source.getWidth();
		int height = source.getHeight();

		float radius = DisplayUtils.dip2px(cx, radiusDip);
		GradientDrawable bg = new GradientDrawable();
		bg.setCornerRadius(radius);
		bg.setSize(width, height);

		Bitmap target = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(target);

		bg.draw(canvas);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setXfermode(new PorterDuffXfermode(
				android.graphics.PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);

		return target;
	}

	/**
	 * 获得圆角图片
	 *
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap createCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 获得带倒影的图片
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImage(Bitmap bitmap) {
		final int reflectionGap = 4;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
				h / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
				Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

}
