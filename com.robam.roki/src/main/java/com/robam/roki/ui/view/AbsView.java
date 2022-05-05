package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.robam.roki.R;

import butterknife.ButterKnife;

public class AbsView extends FrameLayout {

	public AbsView(Context context) {
		super(context);
		init(context, null);
	}

	public AbsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public AbsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	void init(Context cx, AttributeSet attrs) {

		View view = LayoutInflater.from(cx).inflate(R.layout.abs_view,
				this, true);
		if (!view.isInEditMode()) {
			ButterKnife.inject(this, view);
		}

	}

}
