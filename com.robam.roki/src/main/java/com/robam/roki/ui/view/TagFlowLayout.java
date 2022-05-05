package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robam.roki.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 由于用到了Android3.0的API，所以只能在3.0及以上版本编译通过
 *
 * @author chenjing
 */
public class TagFlowLayout extends RelativeLayout implements OnClickListener {
    public interface ITag {
        String getTag();
    }

    public interface OnTagClickCallback {
        void onTagClick(ITag tag);
    }


    private float minTextSize = 12;
    private float maxTextSize = 22;

    private float minAlpha = 0.2f;
    private float maxAlpha = 1f;

    private int mWidth, mHeight;
    private int textMargin = (int) (6.5 * minTextSize);
    /**
     * list的head数据的Y值
     */
    private int firstTextY;
    private boolean isInit = true;
    /**
     * 滚动速度
     */
    private int moveSpeed = 1;
    /**
     * 抛物线顶点到零点高度占View总长度的比值
     */
    private float scaleArcTopPoint = 3;
    /**
     * 是否自动滚动
     */
    private boolean isAutoMove = true;
    private float lastX, lastY;
    private boolean isClick = false;
    // 左中右三列数据
    List<TextView> mTextViews, leftTextViews, rightTextViews;
    private Context mContext;
    OnTagClickCallback callback;

    public TagFlowLayout(Context context) {
        super(context);
        init(context);
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mTextViews = new ArrayList<TextView>();
        leftTextViews = new ArrayList<TextView>();
        rightTextViews = new ArrayList<TextView>();
        mContext = context;
    }

    public void setOnTagClickCallback(OnTagClickCallback callback) {
        this.callback = callback;
    }

    public void addTags(List<ITag> tags) {
        this.removeAllViews();
        if (tags == null || tags.size() == 0) return;

        stop();

        int n = tags.size() / 3;
        for (int i = 0; i < n * 3; i += 3) {
            addLeft(tags.get(i));
            addMiddle(tags.get(i + 1));
            addRight(tags.get(i + 2));
        }

        int m = tags.size() % 3;
        if (m > 0) {
            if (m == 1) {
                addMiddle(tags.get(n * 3));
            } else if (m == 2) {
                addLeft(tags.get(n * 3));
                addRight(tags.get(n * 3 + 1));
            }
        }

        start();

    }

     void addMiddle(ITag tag) {
        TextView tv = createTextView(tag);
        mTextViews.add(tv);
        addView(tv);
    }

     void addLeft(ITag tag) {
        TextView tv = createTextView(tag);
        leftTextViews.add(tv);
        addView(tv);
    }

     void addRight(ITag tag) {
        TextView tv = createTextView(tag);
        rightTextViews.add(tv);
        addView(tv);
    }

    private TextView createTextView(ITag tag) {
        TextView tv = new TextView(mContext);
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        tv.setTag(tag);
        tv.setText(tag.getTag());
        tv.setTextColor(getResources().getColor(R.color.White));
        tv.setTextSize(minTextSize);
        tv.setGravity(Gravity.CENTER);
        tv.setOnClickListener(this);
        // 给TextView设置OnTouchListener为了防止手指落点在TextView上时父控件无法响应事件，onTouch的内容和onTouchEvent的内容差不多
        tv.setOnTouchListener(tvTouchListener);
        return tv;
    }

    private OnTouchListener tvTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isClick = true;
                    lastX = event.getX();
                    lastY = event.getY();
                    stop();
                    isAutoMove = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float length = (float) Math.sqrt(Math.pow(x - lastX, 2)
                            + Math.pow(y - lastY, 2));
                    if (length > 10)
                        isClick = false;
                    float y_length = event.getY() - lastY;
                    if (y_length < 0) {
                        isMoveUp = true;
                        moveUp((int) -y_length);
                    } else if (canDown && y_length > 0) {
                        isMoveUp = false;
                        moveDown((int) y_length);
                    }
                    lastX = event.getX();
                    lastY = event.getY();
                    break;

                case MotionEvent.ACTION_UP:
                    if (isClick)
                        v.performClick();
                    else {
                        start();
                    }
                    isAutoMove = true;
                    break;

            }
            return true;
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTimer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimer();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (isInit) {
            this.mWidth = this.measure(widthMeasureSpec);
            this.mHeight = this.measure(heightMeasureSpec);

            // 从底部开始往上滚动
            firstTextY = mHeight;
            isInit = false;
        }

    }

    private int measure(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result;
        if (specMode == 0) {
            result = 200;
        } else {
            result = specSize;
        }

        return result;
    }


    boolean isRunning = true;

    void start() {
        isRunning = true;
    }

    void startAfterDelay(long delay) {
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }, delay);
    }

    void stop() {
        isRunning = false;
    }

    void startTimer() {
        this.postDelayed(timerRunnable, 30);
    }


    void stopTimer() {
        stop();
        this.removeCallbacks(timerRunnable);
    }

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                if (isAutoMove) {
                    if (isMoveUp) {
                        moveUp(moveSpeed);
                    } else {
                        moveDown(moveSpeed);
                    }
                }
            }
            startTimer();
        }
    };


    private void moveUp(int speed) {
        firstTextY -= speed;
        if (firstTextY < -textMargin) {
            // list的head数据被隐藏了，将head放置到尾部，可以循环滚动了
            canDown = true;
            firstTextY = mTextViews.get(1).getTop();
            TextView tv = mTextViews.get(0);
            mTextViews.remove(0);
            mTextViews.add(tv);
            tv = leftTextViews.get(0);
            leftTextViews.remove(0);
            leftTextViews.add(tv);
            tv = rightTextViews.get(0);
            rightTextViews.remove(0);
            rightTextViews.add(tv);
        }
        TagFlowLayout.this.requestLayout();
    }

    private void moveDown(int speed) {
        if(mTextViews.size() == 0) return;

        firstTextY += speed;
        if (firstTextY > textMargin) {
            firstTextY = -textMargin;
            TextView tv = mTextViews.get(mTextViews.size() - 1);
            mTextViews.remove(mTextViews.size() - 1);
            mTextViews.add(0, tv);
            tv = leftTextViews.get(leftTextViews.size() - 1);
            leftTextViews.remove(leftTextViews.size() - 1);
            leftTextViews.add(0, tv);
            tv = rightTextViews.get(rightTextViews.size() - 1);
            rightTextViews.remove(rightTextViews.size() - 1);
            rightTextViews.add(0, tv);
        }
        TagFlowLayout.this.requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isInit) {
            layoutAll(mTextViews, 0);
            int temp = firstTextY;
            firstTextY += textMargin;
            layoutAll(leftTextViews, -1);
            layoutAll(rightTextViews, 1);
            firstTextY = temp;
        }
    }

    private boolean isMoveUp = false;
    private boolean canDown = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isClick = true;
                lastX = event.getX();
                lastY = event.getY();
                // 手按下后停止自由滚动，随手的滑动而滚动
                stop();
                isAutoMove = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float length = (float) Math.sqrt(Math.pow(x - lastX, 2)
                        + Math.pow(y - lastY, 2));
                if (length > 10)
                    isClick = false;
                float y_length = event.getY() - lastY;
                // 手动move
                if (y_length < 0) {
                    isMoveUp = true;
                    moveUp((int) -y_length);
                } else if (canDown && y_length > 0) {
                    isMoveUp = false;
                    moveDown((int) y_length);
                }
                lastX = event.getX();
                lastY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                if (isClick) {
                    // 点击View后停2秒再开始
                    stop();
                    startAfterDelay(2000);
                } else {
                    start();
                }
                isAutoMove = true;
                break;

        }
        return true;
    }

    /**
     * @param textViews
     * @param type      -1，0,1分别代表左中右list
     */
    private void layoutAll(List<TextView> textViews, int type) {
        int temp_y = firstTextY;
        for (int i = 0; i < textViews.size(); i++) {
            TextView temp = textViews.get(i);
            // 根据y值计算x坐标上的偏移量，type为-1时往左偏，抛物线开口向右，0的时候走直线，1的时候和-1对称
            int detaX = type
                    * (int) (-mWidth * 4 / scaleArcTopPoint
                    / Math.pow(mHeight, 2)
                    * Math.pow(mHeight / 2.0 - temp_y, 2) + mWidth
                    / scaleArcTopPoint);
            float scale = (float) (1 - 4 * Math.pow(mHeight / 2.0 - temp_y, 2)
                    / Math.pow(mHeight, 2));
            if (scale < 0)
                scale = 0;
            float textScale = (float) ((minTextSize + scale
                    * (maxTextSize - minTextSize)) * 1.0 / minTextSize);
            temp.setScaleX(textScale);
            temp.setScaleY(textScale);
            temp.setAlpha(minAlpha + scale * (maxAlpha - minAlpha));
            temp.layout((mWidth - temp.getMeasuredWidth()) / 2 + detaX, temp_y,
                    (mWidth + temp.getMeasuredWidth()) / 2 + detaX, temp_y
                            + temp.getMeasuredHeight());
            temp_y += 2 * textMargin;
        }
    }


    @Override
    public void onClick(View v) {

        stop();
        startAfterDelay(2000);

        ITag tag = (ITag) v.getTag();
        if (tag != null && callback != null) {
            callback.onTagClick(tag);
        }
    }
}
