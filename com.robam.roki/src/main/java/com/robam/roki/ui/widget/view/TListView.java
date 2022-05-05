package com.robam.roki.ui.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

public class TListView  extends ListView {
    private Context mContext;
    private  int mMaxOverDistance = 100;
    public TListView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public TListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public TListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }
    /**
     * @Title: initView
     * @Description: 使得不同分辨率的弹性距离基本一致
     * @param
     * @return void    返回类型
     * @throws
     */
    private void initView() {
        // TODO Auto-generated method stub
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        float dendity = metrics.density;
        mMaxOverDistance = (int)(dendity *mMaxOverDistance);
    }
    /* (非 Javadoc)
     * <p>Title: overScrollBy</p>
     * <p>Description: 重写系统的overScrollBy</p>
     * @param deltaX
     * @param deltaY
     * @param scrollX
     * @param scrollY
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX
     * @param maxOverScrollY
     * @param isTouchEvent
     * @return
     * @see android.view.View#overScrollBy(int, int, int, int, int, int, int, int, boolean)
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY,
                                   int scrollX, int scrollY,
                                   int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY,
                                   boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY,
                scrollX, scrollY,
                scrollRangeX, scrollRangeY,
                maxOverScrollX, mMaxOverDistance,
                isTouchEvent);
    }

}
