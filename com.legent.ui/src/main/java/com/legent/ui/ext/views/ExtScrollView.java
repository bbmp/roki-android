//package com.legent.ui.ext.views;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.GestureDetector;
//import android.view.GestureDetector.SimpleOnGestureListener;
//import android.view.MotionEvent;
//import android.widget.ScrollView;
//
///**
// * 垂直ScrollView,以解决ScrollView与水平listView滑动时冲突
// */
//public class ExtScrollView extends ScrollView {
//
//    private GestureDetector mGestureDetector;
//
//    public ExtScrollView(Context context, AttributeSet attrs) {
//        super(context, attrs);
////        mGestureDetector = new GestureDetector(new YScrollDetector());
////        setFadingEdgeLength(0);
//    }
//
////    @Override
////    public boolean onInterceptTouchEvent(MotionEvent ev) {
////        Log.i("httrhtrhtrh", "onInterceptTouchEvent" + (super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev)));
////        Log.i("httrhtrhtrh", " mGestureDetector.onTouchEvent(ev)" + ( mGestureDetector.onTouchEvent(ev)));
////        return super.onInterceptTouchEvent(ev)
////                && mGestureDetector.onTouchEvent(ev);
////         return true;
////    }
//
//    class YScrollDetector extends SimpleOnGestureListener {
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2,
//                                float distanceX, float distanceY) {
//            return Math.abs(distanceY) > Math.abs(distanceX);
//        }
//    }
//
//}
