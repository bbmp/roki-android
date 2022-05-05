package com.legent.ui.ext.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 可被嵌套在ScrollView中的ListView
 * 
 * @author sylar
 * 
 */
public class NestedListView extends ListView {

	public NestedListView(Context context) {
		super(context);
	}

	public NestedListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NestedListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
