package com.robam.roki.ui.video;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.robam.roki.R;
import com.robam.roki.utils.DensityUtil;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * @author r210190
 * 圆角播放+列表静音
 */
public class JzvdStdRoundVolume extends JzvdStd {

    private int radius;
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;

    public JzvdStdRoundVolume(Context context) {
        super(context);
    }

    public JzvdStdRoundVolume(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JzvdStdRound);
        radius = a.getDimensionPixelSize(R.styleable.JzvdStdRound_radius_jz, 0);
        leftTopRadius = a.getDimensionPixelSize(R.styleable.JzvdStdRound_left_top_radius, 0);
        rightTopRadius = a.getDimensionPixelSize(R.styleable.JzvdStdRound_right_top_radius, 0);
        rightBottomRadius = a.getDimensionPixelSize(R.styleable.JzvdStdRound_right_bottom_radius, 0);
        leftBottomRadius = a.getDimensionPixelSize(R.styleable.JzvdStdRound_left_bottom_radius, 0);
    }


    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    public void setLeftTopRadius(int leftTopRadius) {
        this.leftTopRadius = leftTopRadius;
        invalidate();
    }


    public void setRightTopRadius(int rightTopRadius) {
        this.rightTopRadius = rightTopRadius;
        invalidate();
    }


    public void setRightBottomRadius(int rightBottomRadius) {
        this.rightBottomRadius = rightBottomRadius;
        invalidate();
    }

    public void setLeftBottomRadius(int leftBottomRadius) {
        this.leftBottomRadius = leftBottomRadius;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Path path = new Path();
        if (radius > 0) {
            path.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()),
                    radius, radius, Path.Direction.CW);
        } else {
            path.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()),
                    new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius,
                            rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius},
                    Path.Direction.CW);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
                    Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        }
        canvas.clipPath(path);
        super.dispatchDraw(canvas);
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        if (screen == SCREEN_FULLSCREEN) {
            mediaInterface.setVolume(1f, 1f);
        } else {
            mediaInterface.setVolume(0f, 0f);
        }
    }

    /**
     * 进入全屏模式的时候关闭静音模式
     */
    @Override
    public void gotoFullscreen() {
        super.gotoFullscreen();
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        if (mediaInterface != null) {
            mediaInterface.setVolume(1f, 1f);
        }
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        try {
            if (mediaInterface != null) {
                mediaInterface.setVolume(0f, 0f);
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }

    }

}

