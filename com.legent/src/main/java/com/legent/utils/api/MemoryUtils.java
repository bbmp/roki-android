package com.legent.utils.api;

import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MemoryUtils {

	/**
	 * 清理播放背景
	 * 
	 * @param view
	 */
	public static void disposeBackground(View view) {
		if (view == null)
			return;
		Drawable dw = view.getBackground();
		if (dw != null) {
			dw.setCallback(null);
			dw = null;
		}
		view.setBackgroundResource(0);
	}

	/**
	 * 清理图片背景，包含嵌套子控件
	 * @param view
	 */
	public static void disposeView(View view) {
		if (view == null)
			return;

		Drawable dw = view.getBackground();
		if (dw != null) {
			dw.setCallback(null);
			dw = null;
		}
		view.setBackgroundResource(0);

		if (view instanceof ImageView) {
			ImageView img = (ImageView) view;
			dw = img.getDrawable();
			if (dw != null) {
				dw.setCallback(null);
				dw = null;
			}
			img.destroyDrawingCache();
			img.setImageDrawable(null);
		}

		if (view instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) view;
			int count = group.getChildCount();
			for (int i = 0; i < count; i++) {
				View childView = group.getChildAt(i);
				disposeView(childView);
			}
		}

		view = null;
	}

	public static void logMemory() {

		Log.d("MEM", String.format("总内存: %s KB", getAppNavtiveSize() / 1024F));
		Log.d("MEM",
				String.format("已用内存: %s KB", getAppAllocatedSize() / 1024F));
		Log.d("MEM", String.format("空闲内存: %s KB", getAppFreeSize() / 1024F));
	}

	public static void printMemory() {
		Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
		final String tag = "MEM";

		long totalMemory = Runtime.getRuntime().totalMemory();
		long freeMemory = Runtime.getRuntime().freeMemory();
		long usedMemory = (totalMemory - freeMemory) >> 10;
		totalMemory = totalMemory >> 10;
		freeMemory = freeMemory >> 10;

		Log.i(tag, String.format("总内存: %s KB", totalMemory));
		Log.i(tag, String.format("已用内存: %s KB", usedMemory));
		Log.i(tag, String.format("空闲内存: %s KB", freeMemory));

		Debug.getMemoryInfo(memoryInfo);
		Log.i(tag,
				String.format("Native PSS: %s KB", memoryInfo.nativePss >> 10));
		Log.i(tag,
				String.format("Dalvik PSS: %s KB", memoryInfo.dalvikPss >> 10));
	}

	/**
	 * 当前进程navtive堆本身总的内存大小
	 * 
	 * @return
	 */
	public static long getAppNavtiveSize() {
		return Debug.getNativeHeapSize();
	}

	/**
	 * 当前进程navtive堆中已使用的内存大小
	 * 
	 * @return
	 */
	public static long getAppAllocatedSize() {
		return Debug.getNativeHeapAllocatedSize();
	}

	/**
	 * 当前进程navtive堆中已经剩余的内存大小
	 * 
	 * @return
	 */
	public static long getAppFreeSize() {
		return Debug.getNativeHeapFreeSize();
	}

}
