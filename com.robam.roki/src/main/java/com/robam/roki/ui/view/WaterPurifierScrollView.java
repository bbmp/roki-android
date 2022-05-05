package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Rent on 2016/5/31.
 */
public class WaterPurifierScrollView extends ScrollView {
    private ScrollViewListener scrollViewListener;
    public WaterPurifierScrollView(Context context) {
        super(context);
    }
    public WaterPurifierScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public WaterPurifierScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public interface ScrollViewListener {
        void onScrollChanged(WaterPurifierScrollView scrollView, int x, int y, int oldx, int oldy);
    }
}
