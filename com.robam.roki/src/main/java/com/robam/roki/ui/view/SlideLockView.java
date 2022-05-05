package com.robam.roki.ui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.legent.utils.graphic.ImageUtils;
import com.robam.roki.R;

/**
 * Created by 14807 on 2018/5/31.
 */

public class SlideLockView extends TextView {

    private static final String TAG = "SlideLockView";

    private Bitmap mLockBitmap;
    private int mLockDrawableId;
    private Paint mPaint;
    private int mLockRadius;

    private int height, with;
    private float mLocationY;
    private boolean mIsDragable = false;
    private OnLockListener mLockListener;
    private OnTouchListener mOnTouchListener;


    public SlideLockView(Context context) {
        this(context, null);
    }

    public SlideLockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.SlideLockView, defStyleAttr, 0);
        mLockDrawableId = tp.getResourceId(R.styleable.SlideLockView_lock_drawable, -1);
        mLockRadius = tp.getDimensionPixelOffset(R.styleable.SlideLockView_lock_radius, 1);
        tp.recycle();
        if (mLockDrawableId == -1) {
            throw new RuntimeException("未设置滑动解锁图片");
        }
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight();
        with = getMeasuredWidth();
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        this.setEms(1);
        mLockBitmap = BitmapFactory.decodeResource(context.getResources(), mLockDrawableId);
        // 将图片进行缩小或者放大
        int oldSize = mLockBitmap.getWidth();
        int newSize = mLockRadius * 2;
        float scale = newSize * 1.0f / oldSize;// 缩放值
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        mLockBitmap = Bitmap.createBitmap(mLockBitmap, 0, 0, oldSize, oldSize, matrix, true);
    }

    /**
     * 重绘控件
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLockBitmap == null) return;
        int rightMax = getHeight() - mLockRadius * 2;
        // 保证滑动图片绘制居中 (height / 2 - mLockRadius)
        if (mLocationY < 0) {
            canvas.drawBitmap(mLockBitmap, with / 2 - mLockRadius,0, mPaint);
        } else if (mLocationY > rightMax) {
            canvas.drawBitmap(mLockBitmap, with / 2 - mLockRadius, rightMax, mPaint);
        } else {
            canvas.drawBitmap(mLockBitmap, with / 2 - mLockRadius,mLocationY, mPaint);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int rightMax = getHeight() - mLockRadius * 2;// 右滑成功距离

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://开始触摸
                callTouch(true);
                float yPos = event.getY();
                float xPos = event.getX();
                if (isTouchLock(yPos, xPos)) {
                    mLocationY = yPos - mLockRadius;
                    mIsDragable = true;
                    invalidate();
                } else {
                    mIsDragable = false;
                }
                return true;
            case MotionEvent.ACTION_CANCEL://手势被取消了
                callTouch(false);
                if (!mIsDragable)
                    return true;
                resetLock();
                break;
            case MotionEvent.ACTION_MOVE://移动

                if (!mIsDragable)
                    return true;

                resetLocationY(event.getY(), rightMax);
                invalidate();

                return true;
            //                break;
            case MotionEvent.ACTION_UP://抬起了手指
                callTouch(false);
                if (!mIsDragable)
                    return true;
                if (mLocationY >= rightMax) {
                    mIsDragable = false;
                    mLocationY = 0;
                    invalidate();
                    if (mLockListener != null) {
                        mLockListener.onOpenLockSuccess();
                    }
                }
                resetLock();
                break;
            case MotionEvent.ACTION_OUTSIDE://超出了正常的UI边界
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    private void callTouch(boolean isTouch) {
        if (mOnTouchListener != null)
            mOnTouchListener.onTouch(isTouch);
    }

    /**
     * 回到初始位置
     */
    private void resetLock() {
        ValueAnimator anim = ValueAnimator.ofFloat(mLocationY, 0);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mLocationY = (Float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        anim.start();
    }

    private void resetLocationY(float eventYPos, float rightMax) {

        float yPos = eventYPos;
        mLocationY = yPos - mLockRadius;
        if (mLocationY < 0) {
            mLocationY = 0;
        } else if (mLocationY >= rightMax) {
            mLocationY = rightMax;
        }
    }

    /**
     * 判断是不是在目标点上
     * @param xPos
     * @param yPox
     * @return
     */
    private boolean isTouchLock(float yPox, float xPos) {
        float centerY = mLocationY + mLockRadius;
        float diffY = yPox - centerY;
        float diffX = xPos - mLockRadius;

        return diffY * diffY + diffX * diffX < mLockRadius * mLockRadius;
    }


    public void setLockListener(OnLockListener lockListener) {
        this.mLockListener = lockListener;
    }



    public interface OnLockListener {
        void onOpenLockSuccess();
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    /**
     *  是否在滑动
      */
    public interface OnTouchListener {
        void onTouch(boolean isTouch);
    }
}
