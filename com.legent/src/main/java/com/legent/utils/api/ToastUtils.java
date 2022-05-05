package com.legent.utils.api;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.google.common.base.Strings;

public class ToastUtils {

	static Context cx;

	public static void init(Context cx) {
		ToastUtils.cx = cx;
	}

	public static void show(String msg) {
		showShort(msg);
	}
	public static void show(int resId) {
		showShort(resId);
	}
	public static void showThrowable(Throwable t) {
		if (t != null)
			showShort(t.getMessage());
	}

	public static void showException(Exception e) {
		if (e != null)
			showShort(e.getMessage());
	}

	public static void showShort(int resId) {
		show(resId, Toast.LENGTH_SHORT);
	}

	public static void showShort(String msg) {
		show(msg, Toast.LENGTH_SHORT);
	}

	public static void showLong(int resId) {
		show(resId, Toast.LENGTH_LONG);
	}

	public static void showLong(String msg) {
		show(msg, Toast.LENGTH_LONG);
	}

	public static void show(int resId, int duration) {
		String msg = cx.getString(resId);
		show(msg, duration);
	}

	public static void show(final String msg, final int duration) {

		if (Strings.isNullOrEmpty(msg))
			return;

		Handler h = new Handler(Looper.getMainLooper());
		h.post(new Runnable() {

			@Override
			public void run() {
				Toast toast = Toast.makeText(cx, msg, duration);
				toast.setGravity(Gravity.CENTER,0,0);
				toast.show();
			}
		});

		h = null;
	}

}
