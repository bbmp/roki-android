package com.robam.roki.ui.view.recipeclassify;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 解决gridview只显示一行的问题
 * @author zdj
 *
 */
public class GridFilterView extends GridView {
    public GridFilterView(Context context, AttributeSet attrs) {
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
