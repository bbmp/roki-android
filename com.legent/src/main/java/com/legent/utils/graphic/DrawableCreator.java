package com.legent.utils.graphic;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

public class DrawableCreator {
	
	public static Drawable fromResource(Context cx, int resid) {
		return cx.getResources().getDrawable(resid);
	}

	/**
	 * 创建圆角图形
	 * 
	 * @param radius
	 * @param colorValue
	 * @return
	 */
	public static ShapeDrawable createRoundRectShape(int radius, int colorValue) {
		float[] outerR = new float[] { radius, radius, radius, radius, radius,
				radius, radius, radius };
		// 构造一个圆角矩形,可以使用其他形状，这样ShapeDrawable 就会根据形状来绘制。
		RoundRectShape roundRectShape = new RoundRectShape(outerR, null, null);
		// 组合圆角矩形和ShapeDrawable
		ShapeDrawable drawable = new ShapeDrawable(roundRectShape);
		Paint paint = drawable.getPaint();
		paint.setColor(colorValue);
		paint.setStyle(Paint.Style.FILL);

		return drawable;
	}
}
