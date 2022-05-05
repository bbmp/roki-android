package com.robam.roki.ui.page.device.oven;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by Dell on 2018/7/19.
 */

public class MyGridView extends GridView {
    public boolean isMove;
    private float downX;
    private float downY;
    private float moveX;
    private float moveY;

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            moveX = downX - ev.getX();
            moveY = downY - ev.getY();
            if (Math.abs(moveX) > 5 || Math.abs(moveY) > 5) {
                isMove = true;
            }
            return true; // 禁止GridView滑动
        } else if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            isMove = false;
            downX = ev.getX();
            downY = ev.getY();
        }
        return super.dispatchTouchEvent(ev);

    }

}
