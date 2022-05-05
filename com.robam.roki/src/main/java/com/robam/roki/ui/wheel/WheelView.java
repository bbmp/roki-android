package com.robam.roki.ui.wheel;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.legent.utils.LogUtils;
import com.robam.roki.R;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING;


/**
 * Created by you on 2017/10/11.
 */

public class WheelView extends ViewGroup {
    /**
     * 无效的位置
     */
    public static final int IDLE_POSITION = -1;
    /**
     * 垂直与水平布局两种状态
     */
    public static final int WHEEL_VERTICAL = LinearLayoutManager.VERTICAL;
    public static final int WHEEL_HORIZONTAL = LinearLayoutManager.HORIZONTAL;

    private WheelRecyclerView mRecyclerView;
    /**
     * 分割线颜色
     */
    private int dividerColor = getContext().getResources().getColor(R.color.c31);
    /**
     * item数量
     */
    private int itemCount = 3;
    /**
     * item大小
     */
    private int itemSize = 90;
    /**
     * 分割线之间距离
     */
    private int dividerSize = 90;
    /**
     * 布局方向
     */
    private int orientation = WHEEL_VERTICAL;

    private int selectTextColor = getContext().getResources().getColor(R.color.c30);

    /**
     * 对齐方式
     */
    private int gravity = WheelRecyclerView.GRAVITY_CENTER;

    private int lastSelectedPosition = IDLE_POSITION;
    private int selectedPosition = IDLE_POSITION;
    private int defaultPosition = 0;

    public WheelView(Context context) {
        super(context);
        init(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setDefaultPosition(int defaultPosition) {
        this.defaultPosition = defaultPosition;
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelView);
            itemCount = a.getInt(R.styleable.WheelView_wheelItemCount, itemCount);
            dividerColor = a.getColor(R.styleable.WheelView_dividerColor2, dividerColor);
            itemSize = a.getDimensionPixelOffset(R.styleable.WheelView_wheelItemSize, itemSize);
            dividerSize = a.getDimensionPixelOffset(R.styleable.WheelView_wheelDividerSize, dividerSize);
            orientation = a.getInt(R.styleable.WheelView_wheelOrientation, orientation);
            selectTextColor = a.getInt(R.styleable.WheelView_selectColor, selectTextColor);
            gravity = a.getInt(R.styleable.WheelView_wheelGravity, gravity);
            a.recycle();
        }
        initRecyclerView(context);
    }

    private void initRecyclerView(Context context) {
        mRecyclerView = new WheelRecyclerView(context, gravity, itemCount, itemSize, orientation, dividerColor, dividerSize);
        //RecyclerView66996774Workaround.assistRcyclerView(mRecyclerView);
        mRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        int totolItemSize = (itemCount * 2 + 1) * itemSize;
        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);//让滑动结束时都能定到中心位置
        this.addView(mRecyclerView, WheelUtils.createLayoutParams(orientation, totolItemSize));
        /*mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (listener == null || newState != RecyclerView.SCROLL_STATE_IDLE){
                    return;
                }
                int centerItemPosition = mRecyclerView.findCenterItemPosition();
                if (centerItemPosition == IDLE_POSITION) return;
                selectedPosition = centerItemPosition;
                if (selectedPosition != lastSelectedPosition) {
                    listener.onItemSelected(centerItemPosition);
                    lastSelectedPosition = selectedPosition;
                }
            }
        });*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        if (orientation == WHEEL_HORIZONTAL) {//水平布局时,最好固定高度,垂直布局时最好固定宽度
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        int width, height;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            View child = getChildAt(0);
            width = child.getMeasuredWidth() + getPaddingLeft() + getPaddingRight();
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = itemSize + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }

    private void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        int width, height;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            View child = getChildAt(0);
            height = child.getMeasuredHeight() + getPaddingTop() + getPaddingBottom();
        }
        if (widthMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = itemSize + getPaddingLeft() + getPaddingRight();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() <= 0) {
            return;
        }
        View child = getChildAt(0);
        int childWidth = child.getMeasuredWidth();
        int childHeight = child.getMeasuredHeight();
        int left, top;
        if (orientation == WHEEL_HORIZONTAL) {//水平布局时,最好固定高度,垂直布局时最好固定宽度
            int centerWidth = (getWidth() - getPaddingLeft() - getPaddingRight() - childWidth) >> 1;
            left = getPaddingLeft() + centerWidth;
            top = getPaddingTop();
        } else {
            int centerHeight = (getHeight() - getPaddingTop() - getPaddingBottom() - childHeight) >> 1;
            left = getPaddingLeft();
            top = getPaddingTop() + centerHeight;
        }
        child.layout(left, top, left + childWidth, top + childHeight);
    }

    private OnItemSelectedListener listener;

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * item selected
     */
    public interface OnItemSelectedListener {
        void onItemSelected(int index);
    }

    public void setAdapter(WheelAdapter adapter) {
        if (adapter == null) {
            mRecyclerView.setAdapter(null);
            return;
        }
        final SimpleWheelAdapter wheelAdapter = new SimpleWheelAdapter(adapter, orientation, itemSize, itemCount);
        adapter.adapter = wheelAdapter;
        //RecyclerView66996774Workaround.assistRcyclerView(mRecyclerView);
        mRecyclerView.setAdapter(wheelAdapter);
        wheelAdapter.setSelectPos(defaultPosition);

        if (mRecyclerView == null) {
            return;
        }
//        RecyclerView66996774Workaround.assistRcyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //if (listener == null || newState != RecyclerView.SCROLL_STATE_IDLE) {
                //    return;
                //}

                if (listener != null && newState == RecyclerView.SCROLL_STATE_IDLE) {

                    int centerItemPosition = mRecyclerView.findCenterItemPosition();
                    LogUtils.i("20200411","centerItemPosition:::"+centerItemPosition);
                    if (centerItemPosition == IDLE_POSITION) return;
                    selectedPosition = centerItemPosition;

                    LogUtils.i("20200411","lastSelectedPosition:::"+lastSelectedPosition);
                    //注释掉后 就没有出现偶先的滑动停止后失去焦点
//                    if (selectedPosition != lastSelectedPosition) {
                        LogUtils.i("20200411","-----");
                        wheelAdapter.setSelectPos(centerItemPosition);
                        listener.onItemSelected(centerItemPosition);
                        lastSelectedPosition = selectedPosition;
//                    }
                }



            }
        });
        mRecyclerView.scrollToPosition(defaultPosition);
    }


    /**
     * 适配器
     */
    public abstract static class WheelAdapter<VH extends ViewHolder> {

        SimpleWheelAdapter adapter;

        public abstract int getItemCount();

        public int getItemViewType(int position) {
            return 0;
        }

        public abstract VH onCreateViewHolder(LayoutInflater inflater, int viewType);

        public abstract void onBindViewHolder(VH holder, int position);

        public void onViewRecycled(VH holder) {
        }

        public void onViewAttachedToWindow(VH holder) {
        }

        public abstract void getSelect(int pos);


        public void onViewDetachedFromWindow(VH holder) {
        }

        public final void notifyDataSetChanged() {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

}
