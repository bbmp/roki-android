///**
// * 1、可启用/禁止滑动
// * 2、在禁止滑动时，若执行滑动操作，提供事件通知
// *
// *
// */
//package com.legent.ui.ext.views;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import androidx.viewpager.widget.ViewPager;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//
//public class ExtViewPager extends ViewPager {
//	public interface OnScrollDisabledCallback {
//		void onScrollDisabled();
//	}
//
//	float x_temp01 = 0.0f;
//	float y_temp01 = 0.0f;
//	float x_temp02 = 0.0f;
//	float y_temp02 = 0.0f;
//	float diffX;
//
//	boolean scrollable;
//	OnScrollDisabledCallback listener;
//
//	public ExtViewPager(Context context) {
//		super(context);
//	}
//
//	public ExtViewPager(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//
//	public void setOnScrollDisabledListener(OnScrollDisabledCallback listener) {
//		this.listener = listener;
//	}
//
//	protected void onScrollDisabled() {
//		if (listener != null)
//			listener.onScrollDisabled();
//	}
//
//	@SuppressLint("ClickableViewAccessibility")
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//
//		if (scrollable) {
//			return super.onTouchEvent(event);
//		} else {
//			return false;
//		}
//	}
//
//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent event) {
//		// 获得当前坐标
//		float x = event.getX();
//		float y = event.getY();
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			x_temp01 = x;
//			y_temp01 = y;
//			diffX = 0;
//			break;
//		case MotionEvent.ACTION_MOVE:
//			x_temp02 = x;
//			y_temp02 = y;
//			if (x_temp01 != 0 && y_temp01 != 0) {
//				diffX = Math.abs(x_temp01 - x_temp02);
//			}
//
//			if (!scrollable && diffX > 8) {
//				onScrollDisabled();
//			}
//			break;
//		}
//
//		if (scrollable) {
//			return super.onInterceptTouchEvent(event);
//		} else {
//
//			return false;
//		}
//	}
//
//	public void setScrollable(boolean enable) {
//		scrollable = enable;
//	}
//
//	public boolean getScrollable() {
//		return scrollable;
//	}
//
//}
