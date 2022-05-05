package com.legent.utils.api;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class DisplayUtils {

	/**
	 * 获取屏幕高度（不包含通知栏与状态栏）
	 *
	 * @return
	 */
	public static int getHeight_Without_ActionBar_and_NoticationBar(Context cx) {
		int screenHeight = getScreenHeightPixels(cx);
		int acitonbarHeight = (int) getActionBarHeight(cx);
		int noticationbarHeight = (int) getNoticationBarHeight(cx);
		return screenHeight - acitonbarHeight - noticationbarHeight;
	}

	public static int getScreenWidthPixels(Context cx) {
		return cx.getResources().getDisplayMetrics().widthPixels;
	}

	public static int getScreenHeightPixels(Context cx) {
		return cx.getResources().getDisplayMetrics().heightPixels;
	}

	public static float getActionBarHeight(Context cx) {

		TypedArray ta = cx
				.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });

		float h = ta.getDimension(0, 0);
		ta.recycle();

		return h;
	}

	public static float getNoticationBarHeight(Context cx) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;

		int x = 0, statusBarHeight = 0;

		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = cx.getResources().getDimensionPixelSize(x);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return statusBarHeight;
	}

	public static int dip2px(Context cx, View view, float dpValue) {
		if (view.isInEditMode()) {
			return (int) dpValue;
		} else {
			return dip2px(cx, dpValue);
		}
	}

	public static int sp2px(Context cx, View view, float spValue) {
		if (view.isInEditMode()) {
			return (int) spValue;
		} else {
			return sp2px(cx, spValue);
		}
	}

	public static int dip2px(Context cx, float dpValue) {

		final float scale = cx.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context cx, float pxValue) {
		final float scale = cx.getResources().getDisplayMetrics().density;//8230屏幕   1.3312501
		return (int) (pxValue / scale + 0.5f);
	}

	public static int sp2px(Context cx, float spValue) {
		final float scale = cx.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * scale + 0.5f);
	}

	public static int px2sp(Context cx, float pxValue) {
		final float scale = cx.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / scale + 0.5f);
	}

	public static float pt2px(float ptValue) {
		return ptValue * 1.3F;
	}

	public static float px2pt(float pxValue) {
		return pxValue / 1.3F;
	}

	public static float pt2sp(float ptValue) {
		return ptValue * 2.22F;
	}

	public static float sp2pt(float spValue) {
		return spValue / 2.22F;
	}

    /**
     * @return 获取diplay information
     */
    public static DisplayMetrics getDisplayMetrics(Context cx) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

}
