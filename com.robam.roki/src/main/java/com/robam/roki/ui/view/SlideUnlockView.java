package com.robam.roki.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.legent.utils.LogUtils;
import com.robam.roki.R;

public class SlideUnlockView extends View {

    public int currentState;
    public static final int STATE_LOCK = 1;
    public static final int STATE_UNLOCK = 2;
    public static final int STATE_MOVING = 3;

    private static final String TAG = "SlideUnlockView";
    private Bitmap slideUnlockBackground;
    private Bitmap slideUnlockBlock;


    private int blockBackgroundWidth;    //底部背景宽和高
    private int blockBackgroundHeight;

    private int blockWidth;                //滑动控件宽和高
    private int blockHeight;
    /**
     */
    private float x;
    private float y;
    /**
     */
    private boolean downOnBlock;
    /**
     */
    private int xOffset = 4;
    private String describe = getContext().getString(R.string.device_sterilizer_slide_to_unlock);
    private Paint mTextPaint;

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {

                if (y > 0) {
                    y = y - blockBackgroundHeight * 1.0f / 100;
                    postInvalidate();
                    handler.sendEmptyMessageDelayed(0, 10);
                } else {
                    handler.removeCallbacksAndMessages(null);
                    currentState = STATE_LOCK;
                }
            }
        }

    };
    /**
     */
    private OnUnLockListener onUnLockListener;

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SlideUnlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        currentState = STATE_LOCK;
        // 命名空间
        String namespace = "http://schemas.android.com/apk/res/com.robam.roki";

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setTextSize(40);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setFakeBoldText(true);

        int slideUnlockBackgroundResource = attrs.getAttributeResourceValue(
                namespace, "slideUnlockBackgroundResource", -1);

        int slideUnlockBlockResource = attrs.getAttributeResourceValue(
                namespace, "slideUnlockBlockResource", -1);


        setSlideUnlockBackground(slideUnlockBackgroundResource);

        setSlideUnlockBlock(slideUnlockBlockResource);

        postInvalidate();
    }

    public SlideUnlockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideUnlockView(Context context) {
        this(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //
        canvas.drawBitmap(slideUnlockBackground, 0, 0, null);

        for (int i = 0; i < describe.length(); i++) {
            canvas.drawText(describe, i, i + 1, blockBackgroundWidth / 2, blockBackgroundHeight / 2 + i * 60, mTextPaint);
        }

        switch (currentState) {
            //
            case STATE_LOCK:
                canvas.drawBitmap(slideUnlockBlock, xOffset, 0, null);
                break;

            case STATE_UNLOCK:


                int unlockY = blockBackgroundHeight - blockHeight;
                canvas.drawBitmap(slideUnlockBlock, xOffset, unlockY, null);
                break;
            case STATE_MOVING:

                if (y < 0) {
                    y = 0;
                } else if (y > blockBackgroundHeight - blockHeight) {
                    y = blockBackgroundHeight - blockHeight;
                }
                canvas.drawBitmap(slideUnlockBlock, xOffset, y, null);
                break;
            default:
                break;
        }
    }

    public void setSlideUnlockBackground(int slideUnlockBackgroundResource) {
        slideUnlockBackground = BitmapFactory.decodeResource(getResources(),
                slideUnlockBackgroundResource);


        blockBackgroundWidth = slideUnlockBackground.getWidth();
        blockBackgroundHeight = slideUnlockBackground.getHeight();
        LogUtils.i("20171211", "blockBackgroundWidth:" + blockBackgroundWidth + " blockBackgroundHeight:" + blockBackgroundHeight);
    }

    public void setSlideUnlockBlock(int slideUnlockBlockResource) {
        slideUnlockBlock = BitmapFactory.decodeResource(getResources(),
                slideUnlockBlockResource);

        blockWidth = slideUnlockBlock.getWidth();
        blockHeight = slideUnlockBlock.getHeight();
        LogUtils.i("20171211", "blockWidth:" + blockWidth + " blockHeight:" + blockHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.i(TAG, "onMeauser.....");
        setMeasuredDimension(slideUnlockBackground.getWidth(),
                slideUnlockBackground.getHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                if (currentState != STATE_MOVING) {

                    x = event.getX();
                    y = event.getY();

                    float blockCenterX = blockWidth * 1.0f / 2;
                    float blockCenterY = blockHeight * 1.0f / 2;
                    downOnBlock = isDownOnBlock(blockCenterX, x, blockCenterY, y);
                    Log.i(TAG, "down......................");

                    postInvalidate();

                }
                break;
            case MotionEvent.ACTION_MOVE:

                if (downOnBlock) {

                    x = event.getX();
                    y = event.getY();
                    currentState = STATE_MOVING;
                    Log.i(TAG, "move......................");

                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currentState == STATE_MOVING) {

                    if (y < blockBackgroundHeight - blockHeight) {
                        handler.sendEmptyMessageDelayed(0, 10);

                        onUnLockListener.setUnLocked(false);
                    } else {
                        currentState = STATE_UNLOCK;

                        onUnLockListener.setUnLocked(true);
                    }
                    downOnBlock = false;

                    postInvalidate();

                }
                break;

            default:
                break;
        }
        return true;
    }

    /**
     */
    public boolean isDownOnBlock(float x1, float x2, float y1, float y2) {
        double sqrt = Math.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2)
                + Math.abs(y1 - y2) * Math.abs(y1 - y2));

        return sqrt <= blockHeight / 2;
    }

    /**
     * @param onUnLockListener
     */
    public void setOnUnLockListener(OnUnLockListener onUnLockListener) {
        this.onUnLockListener = onUnLockListener;
    }

    public interface OnUnLockListener {
        void setUnLocked(boolean lock);
    }

    /**
     */
    public void reset() {
        currentState = STATE_LOCK;
        postInvalidate();
    }

    public boolean isOnBackground(int x, int y) {
        return x <= slideUnlockBackground.getWidth() && y <= slideUnlockBackground.getHeight();
    }
}
