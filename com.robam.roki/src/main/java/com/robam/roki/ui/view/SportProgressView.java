package com.robam.roki.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.roki.R;


/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/5/7.
 * PS: Not easy to write code, please indicate.
 */
public class SportProgressView extends View {

    private static final String TAG = "SportProgressView";
    private Paint mPaint;
    private Paint mTextPaint;
    private int colorStart = Color.parseColor("#ffd800");
    private int colorCenter = Color.parseColor("#ff7e00");
    private int colorEnd = Color.parseColor("#ff0000");
    private int colorEmpty = Color.parseColor("#000000");
    private int mWidth;
    // 控件的宽高
    private int mHeight;
    private int marginBottom;
    private int mProgressWidth;
    private int mProgressR;
    private int mStepTextSize;
    private int mCurTemp;
    private Context mContext;
    private Path mPathHealOil;
    private Path mPathOverheat;
    private Path mPathDryWarning;

    public SportProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SportProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttr(attrs, defStyleAttr);
        init();
    }

    private void initAttr(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.SportProgressView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SportProgressView_startColor:
                    colorStart = a.getColor(attr, colorStart);
                    break;
                case R.styleable.SportProgressView_centerColor:
                    colorCenter = a.getColor(attr, colorCenter);
                    break;
                case R.styleable.SportProgressView_endColor:
                    colorEnd = a.getColor(attr, colorEnd);
                    break;
                case R.styleable.SportProgressView_emptyColor:
                    colorEmpty = a.getColor(attr, colorEmpty);
                    break;
                case R.styleable.SportProgressView_progressWidth:
                    mProgressWidth = a.getDimensionPixelSize(attr, 12);
                    break;
                case R.styleable.SportProgressView_stepTextSize:
                    mStepTextSize = a.getDimensionPixelSize(attr, 10);
                    break;
                default:
                    break;
            }
        }
        a.recycle();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mProgressWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(colorEmpty);
        mTextPaint.setTextSize(mStepTextSize);
        marginBottom = getTextHeight(mTextPaint) + Utils.dip2px(mContext, 8);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY) {
            mWidth = widthSpecSize;
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = 400;
        }
        if (heightSpecMode == MeasureSpec.EXACTLY) {
            mHeight = heightSpecSize;
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            mHeight = 400;
        } /**
         * 以给定的高度为限制条件，计算半径
         */
        mProgressR = mHeight - marginBottom - mProgressWidth / 2;
        mWidth = mProgressR * 2 + mProgressWidth +
                getPaddingLeft()
                +
                getPaddingRight();

        LogUtils.i(TAG, "onMeasure--->mWidth = " + mWidth + ",mHeight = " + mHeight);

        setMeasuredDimension(mWidth, mHeight);

    }

    private float mProgress;

    @Override
    protected void onDraw(Canvas canvas) {

        int left = mProgressWidth / 2 + getPaddingLeft();
        int right = left + 2 * mProgressR;
        int top = mHeight - marginBottom - mProgressR;
        int bottom = mHeight - marginBottom + mProgressR;
        RectF rect = new RectF(left, top, right, bottom);
        RectF rectHealOil = new RectF(left - 25, top - 40, right + 5, bottom);
        mPathHealOil = new Path();
        mPathHealOil.addArc(rectHealOil, 200, 50);
        RectF rectOverheat = new RectF(left - 15, top - 40, right, bottom);
        mPathOverheat = new Path();
        mPathOverheat.addArc(rectOverheat, 300, 20);
        RectF rectWarning = new RectF(left, top, right + 40, bottom);
        mPathDryWarning = new Path();
        mPathDryWarning.addArc(rectWarning, 325, 20);

        //  --------------默认阶段-------------
        //1dt
            mPaint.setStrokeCap(Paint.Cap.BUTT);
            mPaint.setColor(colorEmpty);
            mPaint.setShader(null);
            canvas.drawArc(rect, 180, 110, false, mPaint);

            mPaint.setColor(colorEmpty);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawArc(rect, 180, 5, false, mPaint);
            //2
            mPaint.setColor(colorEmpty);
            mPaint.setStrokeCap(Paint.Cap.BUTT);
            canvas.drawArc(rect, 292, 35, false, mPaint);

            //3
            mPaint.setStrokeCap(Paint.Cap.BUTT);
            mPaint.setColor(colorEmpty);
            canvas.drawArc(rect, 329, 31, false, mPaint);

            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setColor(colorEmpty);
            canvas.drawArc(rect, 355, 5, false, mPaint);


        if (mCurTemp >= 50 && mCurTemp <= 240) {
            mPaint.setStrokeCap(Paint.Cap.BUTT);
            mPaint.setColor(colorStart);
            canvas.drawArc(rect, 180, mProgress, false, mPaint);

            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setColor(colorStart);
            canvas.drawArc(rect, 180, 5, false, mPaint);
        } else if (mCurTemp >= 240 && mCurTemp <= 250) {

            mPaint.setStrokeCap(Paint.Cap.BUTT);
            mPaint.setColor(colorStart);
            canvas.drawArc(rect, 180, 110, false, mPaint);

            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setColor(colorStart);
            canvas.drawArc(rect, 180, 5, false, mPaint);

            mPaint.setStrokeCap(Paint.Cap.BUTT);
            mPaint.setColor(colorCenter);
            canvas.drawArc(rect, 292, mProgress, false, mPaint);

        } else if (mCurTemp >= 250 && mCurTemp <= 400) {

            mPaint.setStrokeCap(Paint.Cap.BUTT);
            mPaint.setColor(colorStart);
            canvas.drawArc(rect, 180, 110, false, mPaint);

            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setColor(colorStart);
            canvas.drawArc(rect, 180, 5, false, mPaint);

            mPaint.setStrokeCap(Paint.Cap.BUTT);
            mPaint.setColor(colorCenter);
            canvas.drawArc(rect, 292, 35, false, mPaint);

            mPaint.setStrokeCap(Paint.Cap.BUTT);
            mPaint.setColor(colorEnd);
            canvas.drawArc(rect, 329, mProgress, false, mPaint);
            if (mProgress > 30) {
                mPaint.setStrokeCap(Paint.Cap.ROUND);
                mPaint.setColor(colorEnd);
                canvas.drawArc(rect, 355, 5, false, mPaint);
            }
        }
        drawProgressText(canvas, mCurTemp);
        drawText(canvas);

    }

    /**
     * 画底部初始的文字和结束的文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        mTextPaint.setColor(colorEmpty);
        mTextPaint.setTextSize(28);
        canvas.drawTextOnPath("健康烹饪", mPathHealOil, 10, 10, mTextPaint);
        canvas.drawTextOnPath("过热", mPathOverheat, 10, 0, mTextPaint);
        canvas.drawTextOnPath("干烧预警", mPathDryWarning, 10, 10, mTextPaint);

    }

    /**
     * 绘制中间进度的文字
     *
     * @param canvas
     * @param progress
     */
    private void drawProgressText(Canvas canvas, float progress) {
        LogUtils.i("20190509", "temp = " + progress);
        mTextPaint.setColor(getResources().getColor(R.color.c11));
        mTextPaint.setTextSize(100);
        String text = (int) progress + "";
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float baseline = mHeight / 2 + marginBottom - fm.descent;
        float stringWidth = mTextPaint.measureText(text);
        canvas.drawText(text, mWidth / 2 - stringWidth / 2, baseline, mTextPaint);

        mTextPaint.setColor(colorEmpty);
        mTextPaint.setTextSize(80);
        Paint.FontMetrics fmTemp = mTextPaint.getFontMetrics();
        float baselineTemp = mHeight / 2 + marginBottom - fmTemp.descent;
        float stringWidthTemp = mTextPaint.measureText("℃");
        canvas.drawText("℃", mWidth / 2 - stringWidthTemp / 2 + stringWidth / 2 + 30, baselineTemp - 30, mTextPaint);
    }

    private int getTextHeight(Paint mTextPaint) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        int textHeight = (int) (Math.ceil(fm.descent - fm.ascent) + 2);
        return textHeight;
    }

    public void setProgress(int temp) {
        mCurTemp = temp;
        LogUtils.i("20190520", "mCurTemp:" + mCurTemp);
        startAnimation();
    }

    //    mProgress = 0f;


    /**
     * 动画效果的取值
     */
    public void startAnimation() {

        if (mCurTemp <= 50) {
            mProgress = 0;
        } else if (mCurTemp >= 50 && mCurTemp <= 240) {
            mProgress = (mCurTemp - 50f) / (240f - 50f) * (360f - (360f - 110f));
        } else if (mCurTemp >= 240 && mCurTemp <= 250) {
            mProgress = (mCurTemp - 240f) / (250f - 240f) * (35);
        } else if (mCurTemp >= 250 && mCurTemp <= 400) {
            mProgress = (mCurTemp - 250f) / (400f - 250f) * (360f - 329f);
        }
        invalidate();
        /*ValueAnimator animator = ValueAnimator.ofFloat(0, mCurTemp / 360f * 180);
        animator.setDuration(1600).start();
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = (float) animation.getAnimatedValue();
                LogUtils.i("20190509", "mProgress:" + mProgress);
            }
        });*/
    }
}
