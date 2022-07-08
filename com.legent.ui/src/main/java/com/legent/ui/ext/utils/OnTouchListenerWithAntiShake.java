package com.legent.ui.ext.utils;

import android.view.MotionEvent;
import android.view.View;

import com.google.common.base.Preconditions;

/**
 * Created by sylar on 15/7/7.
 */

/**
 * 1、防止抖动
 * 2、防止重复点击
 */
public class OnTouchListenerWithAntiShake implements View.OnTouchListener {

    private static final int CLICK_DURATION = 40;
    private static final int CLICK_DISTANCE = 10;
    private static final long REPEAT_PERIOD = 500;

    private long mLastTouchTime;
    private long mCurrentTouchTime;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mCurrentTouchX;
    private float mCurrentTouchY;

    //标识当前是否处于刚点击过，默认未点击过
    private boolean isInRepeat = false;

    //激活防重复点击功能
    private boolean enable_anti_repeat = true;

    private View view;
    private View.OnClickListener clickListener;


    public OnTouchListenerWithAntiShake(View view, View.OnClickListener clickListener) {

        Preconditions.checkNotNull(view);

        this.view = view;
        this.clickListener = clickListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = mCurrentTouchX = event.getX();
                mLastTouchY = mCurrentTouchY = event.getY();
                mLastTouchTime = mCurrentTouchTime = System.currentTimeMillis();

                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentTouchX = event.getX();
                mCurrentTouchY = event.getY();
                mCurrentTouchTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentTouchTime - mLastTouchTime > CLICK_DURATION
                        && Math.abs(mCurrentTouchX - mLastTouchX) < CLICK_DISTANCE
                        && Math.abs(mCurrentTouchY - mLastTouchY) < CLICK_DISTANCE) {
                    onClickView();
                }
                break;
        }
        return true;
    }

    private void onClickView() {

        if (enable_anti_repeat && !isInRepeat) {
            isInRepeat = true;
            if (clickListener != null) {
                clickListener.onClick(view);
            }

            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //延迟后，将状态复位
                    isInRepeat = false;
                }
            }, REPEAT_PERIOD);

        } else {
            if (clickListener != null) {
                clickListener.onClick(view);
            }
        }
    }


}
