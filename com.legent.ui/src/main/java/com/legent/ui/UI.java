package com.legent.ui;

import android.content.Context;

import com.legent.R;

public class UI {
	public static final String TAG = "platui";

//	public static String getStr_Yes(Context cx) {
//		return getString(cx, R.string.common_yes);
//	}
//
//	public static String getStr_No(Context cx) {
//		return getString(cx, R.string.common_no);
//	}

//	public static String getStr_Ok(Context cx) {
//		return getString(cx, R.string.common_ok);
//	}

//	public static String getStr_Cancel(Context cx) {
//		return getString(cx, R.string.common_cancel);
//	}

	public static String getString(Context cx, int resId) {
		return cx.getString(resId);
	}
}
