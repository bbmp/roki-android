package com.legent.ui.ext.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.ui.UI;
import com.legent.ui.R;
import com.legent.utils.api.DisplayUtils;

public class AbsLineView extends FrameLayout {

	protected int TitleMaxLength = getTitleMaxLength();
	protected FrameLayout pnlMain;
	protected LinearLayout pnlLine;
	protected View split;
	protected TextView txtTitle;

	protected boolean showSplit, isDisabled;
	protected String title;
	protected int lblMaxWidth;

	public AbsLineView(Context context) {
		super(context);
		init(context, null);
	}

	public AbsLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public AbsLineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	protected void init(Context cx, AttributeSet attrs) {

		if (attrs != null) {
			TypedArray ta = cx.obtainStyledAttributes(attrs,
					R.styleable.LineView);
			showSplit = ta.getBoolean(R.styleable.LineView_showSplit, true);
			isDisabled = ta.getBoolean(R.styleable.LineView_isDisible, false);
			title = ta.getString(R.styleable.LineView_lineTitle);
			lblMaxWidth = ta.getInt(R.styleable.LineView_titleMaxWidth,
					TitleMaxLength);
			ta.recycle();
		} else {
			showSplit = true;
			isDisabled = false;
			lblMaxWidth = TitleMaxLength;
		}

		LayoutInflater.from(cx).inflate(R.layout.view_abs_line, this, true);
		pnlLine = findViewById(R.id.pnlLine);
		pnlMain = findViewById(R.id.pnlMain);
		split = findViewById(R.id.split);
		txtTitle = findViewById(R.id.txtTitle);

		txtTitle.setMaxWidth(lblMaxWidth);
		setShowSplit(showSplit);

		setTitle(title);

		pnlMain.setEnabled(!isDisabled);
	}

	public void setShowSplit(boolean isShowSplit) {
		this.showSplit = isShowSplit;
		split.setVisibility(isShowSplit ? View.VISIBLE : View.GONE);
	}

	public void setContentDisible(boolean isDisabled) {
		this.isDisabled = isDisabled;
		pnlMain.setEnabled(!isDisabled);
	}

	public boolean isShowSplit() {
		return showSplit;
	}

	public void setTitle(String title) {
		this.title = title;
		txtTitle.setText(title);
	}

	public void setTitle(int resId) {
		this.title = UI.getString(getContext(), resId);
		txtTitle.setText(resId);
	}

	public String getTitle() {
		return title;
	}

	protected int getTitleMaxLength() {
		if (isInEditMode())
			return 300;
		else
			return DisplayUtils.getScreenWidthPixels(getContext()) / 2;
	}
}
