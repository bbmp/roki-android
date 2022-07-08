package com.robam.roki.ui.view.wheelview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.fan.IFan;
import com.robam.roki.R;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.listener.OnItemSelectedListenerCenterPosition;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerPosition;
import com.robam.roki.listener.OnItemSelectedListenerRear;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * Created by  2017/8/12
 */
public class LoopView extends View {
    //放大选中字体比例
    private float scaleX = 1.03F;
    //默认字体的大小
    private static final int DEFAULT_TEXT_SIZE = (int) (Resources.getSystem().getDisplayMetrics().density *6);
    //item 默认隔开的间距
    private static final float DEFAULT_LINE_SPACE = 2f;
    //默认显示的条目个数
    private static final int DEFAULT_VISIBIE_ITEMS = 7;
    //枚举动作 单击 抛 拖拽
    public enum ACTION {
        CLICK, FLING, DAGGLE
    }

    private Context context;

    Handler mHandler;
    private GestureDetector mFlingGestureDetector;
    OnItemSelectedListenerFrone mOnItemSelectedListenerFrone;
    OnItemSelectedListenerCenter mOnItemSelectedListenerCenter;
    OnItemSelectedListenerRear mOnItemSelectedListenerRear;
    OnItemSelectedListenerPosition mOnItemSelectedListenerPosition;
    OnItemSelectedListenerCenterPosition mOnItemSelectedListenerCenterPosition;

    // Timer mTimer;
    ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    private Paint mPaintOuterText;
    private Paint mPaintCenterText;
    private Paint mPaintIndicator;

    List<String> mItems;
    int mTextSize;
    int mMaxTextHeight;
    int mOuterTextColor;//外部字体颜色
    int mCenterTextColor;//选中的字体颜色
    int mDividerColor;
    float mLineSpacingMultiplier;
    boolean mIsLoop;
    int mFirstLineY;
    int mSecondLineY;
    int mTotalScrollY;
    int mInitPosition;
    private int mSelectedItem;
    int mPreCurrentIndex;
    int mChange;
    int mItemsVisibleCount;
    String[] mDrawingStrings;
    int mMeasuredHeight;
    int measuredWidth;
    int mHalfCircumference;//两条线之间的高度
    int mRadius;
    private int mOffset = 0;
    private float mPreviousY;
    long mStartTime = 0;
     Rect mTempRect = new Rect();
    private int mPaddingLeft, mPaddingRight;

    /**
     * 设置文本行空间必须大于1
     * @param lineSpacingMultiplier
     */
    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        if (lineSpacingMultiplier > 1.0f) {
            this.mLineSpacingMultiplier = lineSpacingMultiplier;
        }
    }

    /**
     * 设置文本外颜色
     * @param centerTextColor
     */
    public void setCenterTextColor(int centerTextColor) {
        this.mCenterTextColor = centerTextColor;
        mPaintCenterText.setColor(centerTextColor);
    }

    /**
     * 设置中间文本颜色
     * @param outerTextColor
     */
    public void setOuterTextColor(int outerTextColor) {
        mOuterTextColor = outerTextColor;
        mPaintOuterText.setColor(outerTextColor);
    }

    /**
     * 设置线颜色
     * @param dividerColor
     */
    public void setDividerColor(int dividerColor) {
        mDividerColor = dividerColor;
        mPaintIndicator.setColor(dividerColor);
    }


    public LoopView(Context context) {
        super(context);
        initLoopView(context, null);
    }

    public LoopView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        initLoopView(context, attributeset);
    }

    public LoopView(Context context, AttributeSet attributeset, int defStyleAttr) {
        super(context, attributeset, defStyleAttr);
        initLoopView(context, attributeset);
    }

    /**
     * 初始化控件
     * @param context
     * @param attributeset
     */
    private void initLoopView(Context context, AttributeSet attributeset) {
        this.context = context;
        mHandler = new MessageHandler(this);
        mHandler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
        mFlingGestureDetector = new GestureDetector(context, new LoopViewGestureListener(this));
        mFlingGestureDetector.setIsLongpressEnabled(false);
        TypedArray typedArray = context.obtainStyledAttributes(attributeset, R.styleable.rokiWheelView);
        mTextSize = typedArray.getInteger(R.styleable.rokiWheelView_rwv_textsize, DEFAULT_TEXT_SIZE);
        mTextSize = (int) (Resources.getSystem().getDisplayMetrics().density * mTextSize);
        mLineSpacingMultiplier = typedArray.getFloat(R.styleable.rokiWheelView_rwv_lineSpace, DEFAULT_LINE_SPACE);
        mCenterTextColor = typedArray.getInteger(R.styleable.rokiWheelView_rwv_centerTextColor, 0xff000000);
        mOuterTextColor = typedArray.getInteger(R.styleable.rokiWheelView_rwv_outerTextColor, 0xff999999);
        mDividerColor = typedArray.getInteger(R.styleable.rokiWheelView_rwv_dividerTextColor, 0xffDAA520);
        mItemsVisibleCount = typedArray.getInteger(R.styleable.rokiWheelView_rwv_itemsVisibleCount, DEFAULT_VISIBIE_ITEMS);
        if (mItemsVisibleCount % 2 == 0) {
            mItemsVisibleCount = DEFAULT_VISIBIE_ITEMS;
        }
        mIsLoop = typedArray.getBoolean(R.styleable.rokiWheelView_rwv_isLoop, false);
        typedArray.recycle();
        mDrawingStrings = new String[mItemsVisibleCount];
        mTotalScrollY = 0;
        mInitPosition = -1;
        initPaints();

    }

    /**
     * 可见项数，必须是奇数
     *
     * @param visibleNumber
     */
    public void setItemsVisibleCount(int visibleNumber) {
        if (visibleNumber % 2 == 0) {
            return;
        }
        if (visibleNumber != mItemsVisibleCount) {
            mItemsVisibleCount = visibleNumber;
            mDrawingStrings = new String[mItemsVisibleCount];
        }
    }

    /**
     * 初始化所有的画笔
     */
    private void initPaints() {
        mPaintOuterText = new Paint();
        mPaintOuterText.setColor(mOuterTextColor);
        mPaintOuterText.setAntiAlias(true);
        mPaintOuterText.setTypeface(Typeface.MONOSPACE);
        mPaintOuterText.setTextSize(mTextSize);

        mPaintCenterText = new Paint();
        mPaintCenterText.setColor(mCenterTextColor);
        mPaintCenterText.setAntiAlias(true);
        mPaintCenterText.setTextScaleX(scaleX);
        mPaintCenterText.setTypeface(Typeface.MONOSPACE);
        mPaintCenterText.setTextSize(mTextSize);

        mPaintIndicator = new Paint();
        mPaintIndicator.setColor(mDividerColor);
        mPaintIndicator.setAntiAlias(true);

    }

    /**
     * 重新测量
     */
    private void remeasure() {
        if (mItems == null) {
            return;
        }

        measuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();
        if (measuredWidth == 0 || mMeasuredHeight == 0) {
            return;
        }

        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();
        measuredWidth = measuredWidth - mPaddingRight;
        mPaintCenterText.getTextBounds("\u661F\u671F", 0, 2, mTempRect); // 星期
        mMaxTextHeight = mTempRect.height();
        mHalfCircumference = (int) (mMeasuredHeight * Math.PI / 2);
        mMaxTextHeight = (int) (mHalfCircumference / (mLineSpacingMultiplier * (mItemsVisibleCount - 1)));
        mRadius = mMeasuredHeight / 2;
        mFirstLineY = (int) ((mMeasuredHeight - mLineSpacingMultiplier * mMaxTextHeight) / 2.0F);
        mSecondLineY = (int) ((mMeasuredHeight + mLineSpacingMultiplier * mMaxTextHeight) / 2.0F);
        if (mInitPosition == -1) {
            if (mIsLoop) {
                mInitPosition = (mItems.size() + 1) / 2;
            } else {
                mInitPosition = 0;
            }
        }

        mPreCurrentIndex = mInitPosition;
    }

    /**
     * 上下滚动
     * @param action
     */
    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DAGGLE) {
            float itemHeight = mLineSpacingMultiplier * mMaxTextHeight;
            mOffset = (int) ((mTotalScrollY % itemHeight + itemHeight) % itemHeight);
            if ((float) mOffset > itemHeight / 2.0F) {
                mOffset = (int) (itemHeight - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }
        mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, mOffset), 0, 4, TimeUnit.MILLISECONDS);
    }
    /**
     * 滚动到指定位置
     * @param velocityY
     */
    protected final void scrollBy(float velocityY) {
        cancelFuture();
        int velocityFling = 5;
        mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, velocityY), 0, velocityFling,
            TimeUnit.MILLISECONDS);
    }

    public void cancelFuture() {
        if (null != mFuture&& !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    /**
     * 设置不循环
     */
    public void setNotLoop() {
        mIsLoop = false;
    }

    /**
     * 设置文字大小
     * @param size
     */
    public final void setTextSize(float size) {
        if (size > 0.0F) {
            mTextSize = (int) (context.getResources().getDisplayMetrics().density * size);
            mPaintOuterText.setTextSize(mTextSize);
            mPaintCenterText.setTextSize(mTextSize);
        }
    }

    /**
     * 设置初始化时的位置
     * @param initPosition
     */
    public final void setInitPosition(int initPosition) {
        if (initPosition < 0) {
            this.mInitPosition = 0;
        } else {
            if (mItems != null && mItems.size() > initPosition) {
                mInitPosition = initPosition;
            }
        }
    }

    public void setListenerFrone(OnItemSelectedListenerFrone OnItemSelectedListenerFrone) {
        mOnItemSelectedListenerFrone = OnItemSelectedListenerFrone;
    }

    public void setListenerCenter(OnItemSelectedListenerCenter OnItemSelectedListenerCenter) {
        mOnItemSelectedListenerCenter = OnItemSelectedListenerCenter;
    }
    public void setListenerRear(OnItemSelectedListenerRear OnItemSelectedListenerRear) {
        mOnItemSelectedListenerRear = OnItemSelectedListenerRear;
    }
    public void setListenerPosition(OnItemSelectedListenerPosition OnItemSelectedListenerPosition) {
        mOnItemSelectedListenerPosition = OnItemSelectedListenerPosition;
    }

    public void setListenerCenterPosition(OnItemSelectedListenerCenterPosition mOnItemSelectedListenerCenterPosition) {
        this.mOnItemSelectedListenerCenterPosition = mOnItemSelectedListenerCenterPosition;
    }

    public void setItems(List<String> items) {
        mItems = items;
        remeasure();
        invalidate();
    }

    /**
     * 获取选中的item
     * @return
     */
    public int getSelectedItem() {
        return mSelectedItem;
    }

    /*
     protected final void scrollBy(float velocityY) {
     Timer timer = new Timer();
     mTimer = timer;
     timer.schedule(new InertiaTimerTask(this, velocityY, timer), 0L, 20L);
     }*/

    protected void onItemSelected() {

        if (mOnItemSelectedListenerFrone != null) {
            postDelayed(new OnItemSelectedFroneRunnable(this), 200L);
        }
        if (mOnItemSelectedListenerCenter != null) {
            postDelayed(new OnItemSelectedCenterRunnable(this), 200L);
        }
        if (mOnItemSelectedListenerRear != null) {
            postDelayed(new OnItemSelectedRearRunnable(this), 200L);
        }
        if (mOnItemSelectedListenerPosition != null){
            postDelayed(new OnItemSelectedPositionRunnable(this), 200L);
        }
//        if (mOnItemSelectedListenerCenterPosition != null){
//            postDelayed(new OnItemSelectedPositionRunnable(this), 200L);
//        }
    }


    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    /**
     * 设置当前位置
     * @param position
     */
    public void setCurrentPosition(int position) {
        if (null == mItems || mItems.isEmpty()) {
            return;
        }
        int size = mItems.size();
        if (position >= 0 && position < size && position != mSelectedItem) {
            mInitPosition = position;
            mSelectedItem = position;
            mTotalScrollY = 0;
            mOffset = 0;
            invalidate();
        }
    }

    /**
     * 获取item的内容
     * @param position 获取内容的标签
     * @return
     */
    public String getItemsContent(int position){

        if (null != mItems && mItems.size() != 0){
            for(int i = 0; i < mItems.size(); i++){
                String itemContent = mItems.get(position);
                return itemContent;
            }
        }
        return null;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (null == mItems) {
            return;
        }
        mChange = (int) (mTotalScrollY / (mLineSpacingMultiplier * mMaxTextHeight));
        if (mItems.size() != 0){
            mPreCurrentIndex = mInitPosition + mChange % mItems.size();
        }
        if (!mIsLoop) {
            if (mPreCurrentIndex < 0) {
                mPreCurrentIndex = 0;
            }
            if (mPreCurrentIndex > mItems.size() - 1) {
                mPreCurrentIndex = mItems.size() - 1;
            }
        } else {
            if (mPreCurrentIndex < 0) {
                mPreCurrentIndex = mItems.size() + mPreCurrentIndex;
            }
            if (mPreCurrentIndex > mItems.size() - 1) {
                mPreCurrentIndex = mPreCurrentIndex - mItems.size();
            }
        }

        int j2 = (int) (mTotalScrollY % (mLineSpacingMultiplier * mMaxTextHeight));
        // put value to drawingString
        int k1 = 0;
        while (k1 < mItemsVisibleCount) {
            int l1 = mPreCurrentIndex - (mItemsVisibleCount / 2 - k1);
            if (mIsLoop) {
                while (l1 < 0) {
                    l1 = l1 + mItems.size();
                }
                while (l1 > mItems.size() - 1) {
                    l1 = l1 - mItems.size();
                }
                mDrawingStrings[k1] = mItems.get(l1);
            } else if (l1 < 0) {
                mDrawingStrings[k1] = "";
            } else if (l1 > mItems.size() - 1) {
                mDrawingStrings[k1] = "";
            } else {
                mDrawingStrings[k1] = mItems.get(l1);
            }
            k1++;
        }
        canvas.drawLine(mPaddingLeft, mFirstLineY, measuredWidth, mFirstLineY, mPaintIndicator);
        canvas.drawLine(mPaddingLeft, mSecondLineY, measuredWidth, mSecondLineY, mPaintIndicator);

        int i = 0;
        while (i < mItemsVisibleCount) {
            canvas.save();
            float itemHeight = mMaxTextHeight * mLineSpacingMultiplier;
            double radian = ((itemHeight * i - j2) * Math.PI) / mHalfCircumference;
            if (radian >= Math.PI || radian <= 0) {
                canvas.restore();
            } else {
                int translateY = (int) (mRadius - Math.cos(radian) * mRadius - (Math.sin(radian) * mMaxTextHeight) / 2D);
                canvas.translate(0.0F, translateY);
                canvas.scale(1.0F, (float) Math.sin(radian));
                if (translateY <= mFirstLineY && mMaxTextHeight + translateY >= mFirstLineY) {
                    // first divider
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, mFirstLineY - translateY);
                    canvas.drawText(mDrawingStrings[i], getTextX(mDrawingStrings[i], mPaintOuterText, mTempRect),
                            mMaxTextHeight, mPaintOuterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, mFirstLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.drawText(mDrawingStrings[i], getTextX(mDrawingStrings[i], mPaintCenterText, mTempRect),
                            mMaxTextHeight, mPaintCenterText);
                    canvas.restore();
                } else if (translateY <= mSecondLineY && mMaxTextHeight + translateY >= mSecondLineY) {
                    // second divider
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, mSecondLineY - translateY);
                    canvas.drawText(mDrawingStrings[i], getTextX(mDrawingStrings[i], mPaintCenterText, mTempRect),
                            mMaxTextHeight, mPaintCenterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, mSecondLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.drawText(mDrawingStrings[i], getTextX(mDrawingStrings[i], mPaintOuterText, mTempRect),
                            mMaxTextHeight, mPaintOuterText);
                    canvas.restore();
                } else if (translateY >= mFirstLineY && mMaxTextHeight + translateY <= mSecondLineY) {
                    // center item
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.drawText(mDrawingStrings[i], getTextX(mDrawingStrings[i], mPaintCenterText, mTempRect),
                            mMaxTextHeight, mPaintCenterText);
                    mSelectedItem = mItems.indexOf(mDrawingStrings[i]);
                } else {
                    // other item
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.drawText(mDrawingStrings[i], getTextX(mDrawingStrings[i], mPaintOuterText, mTempRect),
                            mMaxTextHeight, mPaintOuterText);
                }
                canvas.restore();
            }
            i++;
        }
    }
    //文本开始画的位置
    private int getTextX(String a, Paint paint, Rect rect) {
        paint.getTextBounds(a, 0, a.length(), rect);
        int textWidth = rect.width();
        textWidth *= scaleX;
        return (measuredWidth - mPaddingLeft - textWidth) / 2 + mPaddingLeft;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        remeasure();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean eventConsumed = mFlingGestureDetector.onTouchEvent(event);
        float itemHeight = mLineSpacingMultiplier * mMaxTextHeight;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartTime = System.currentTimeMillis();
                cancelFuture();
                mPreviousY = event.getRawY();
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = mPreviousY - event.getRawY();
                mPreviousY = event.getRawY();

                mTotalScrollY = (int) (mTotalScrollY + dy);

                if (!mIsLoop) {
                    float top = -mInitPosition * itemHeight;
                    float bottom=0;
                    if (!mItems.isEmpty())
                        bottom = (mItems.size() - 1 - mInitPosition) * itemHeight;

                    if (mTotalScrollY < top) {
                        mTotalScrollY = (int) top;
                    } else if (mTotalScrollY > bottom) {
                        mTotalScrollY = (int) bottom;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                if (!eventConsumed) {
                    float y = event.getY();
                    double l = Math.acos((mRadius - y) / mRadius) * mRadius;
                    int circlePosition = (int) ((l + itemHeight / 2) / itemHeight);

                    float extraOffset = (mTotalScrollY % itemHeight + itemHeight) % itemHeight;
                    mOffset = (int) ((circlePosition - mItemsVisibleCount / 2) * itemHeight - extraOffset);

                    if ((System.currentTimeMillis() - mStartTime) > 120) {
                        smoothScroll(ACTION.DAGGLE);
                    } else {
                        smoothScroll(ACTION.CLICK);
                    }
                }
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }

        invalidate();
        return true;
    }
}
