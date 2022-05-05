package com.robam.roki.ui.view.networkoptimization;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 解决gridview只显示一行的问题
 * @author zdj
 *
 */
public class GridChildView extends GridView {

    public GridChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
