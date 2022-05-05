package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/3/16.
 */

public class ScrollviewMegListView extends ListView {
    public ScrollviewMegListView(Context context) {
        super(context);
    }

    public ScrollviewMegListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollviewMegListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
