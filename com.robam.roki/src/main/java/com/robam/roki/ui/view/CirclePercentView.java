package com.robam.roki.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.legent.utils.LogUtils;
import com.robam.roki.R;
import com.robam.roki.utils.PxUtils;

/**
 * Created by 14807 on 2018/4/27.
 */

public class CirclePercentView extends View {

    //圆的半径
    private float mRadius;

    //色带的宽度
    private float mStripeWidth;
    //总体大小
    private int mHeight;
    private int mWidth;

    //动画位置百分比进度
    private int mCurPercent;

    //实际百分比进度
    private int mPercent;
    //圆心坐标
    private float x;
    private float y;

    //要画的弧度
    private int mEndAngle;

    //小圆的颜色
    private int mOrangeColor;
    //大圆颜色
    private int mGrayColor;

    //中心百分比文字大小
    private float mCenterTextSize;

    public CirclePercentView(Context context) {
        this(context, null);
    }

    public CirclePercentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView, defStyleAttr, 0);
        mStripeWidth = a.getDimension(R.styleable.CirclePercentView_stripeWidth, PxUtils.dpToPx(14, context));
        mCurPercent = a.getInteger(R.styleable.CirclePercentView_percent, 0);
        mOrangeColor = a.getColor(R.styleable.CirclePercentView_smallColor, 0xfffed268);
        mGrayColor = a.getColor(R.styleable.CirclePercentView_bigColor, 0xffe8e8e8);
        mCenterTextSize = a.getDimensionPixelSize(R.styleable.CirclePercentView_centerTextSize, PxUtils.spToPx(50, context));
        mRadius = a.getDimensionPixelSize(R.styleable.CirclePercentView_radius, PxUtils.dpToPx(100, context));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取测量大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            mRadius = widthSize / 2;
            x = widthSize / 2;
            y = heightSize / 2;
            mWidth = widthSize;
            mHeight = heightSize;
        }

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            mWidth = (int) (mRadius * 2);
            mHeight = (int) (mRadius * 2);
            x = mRadius;
            y = mRadius;

        }

        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        mEndAngle = (int) (mCurPercent * 3.65);
        //灰色圆
        Paint bigCirclePaint = new Paint();
        bigCirclePaint.setAntiAlias(true);
        bigCirclePaint.setColor(mGrayColor);
        bigCirclePaint.setStyle(Paint.Style.FILL);
        bigCirclePaint.setStrokeWidth(mStripeWidth);
        canvas.drawCircle(x, y, mRadius, bigCirclePaint);


        //橙色
        Paint sectorPaint = new Paint();
        sectorPaint.setColor(mOrangeColor);
        sectorPaint.setAntiAlias(true);
        sectorPaint.setStyle(Paint.Style.FILL);
        RectF rect = new RectF(0, 0, mWidth, mHeight);
        canvas.drawArc(rect, 300, mEndAngle, true, sectorPaint);


        //绘制小圆,颜色透明
        Paint smallCirclePaint = new Paint();
        smallCirclePaint.setAntiAlias(true);
        smallCirclePaint.setColor(0xfff6f4f1);
        canvas.drawCircle(x, y, mRadius - mStripeWidth, smallCirclePaint);
    }

    //外部设置百分比数
    public void setPercent(int percent) {
        if (percent > 100) {
            throw new IllegalArgumentException("percent must less than 60!");
        }
        setCurPercent(percent);

    }

    //内部设置百分比 用于动画效果
    private void setCurPercent(int percent) {


        mPercent = percent;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 1;
                for (int i = 0; i < mPercent; i++) {
                    if (i % 20 == 0) {
                        sleepTime += 2;
                    }
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mCurPercent = i;
                    CirclePercentView.this.postInvalidate();
                }
            }

        }).start();

    }

}
