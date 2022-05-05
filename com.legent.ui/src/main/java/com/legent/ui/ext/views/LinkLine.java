package com.legent.ui.ext.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.legent.ui.UI;
import com.legent.ui.R;

public class LinkLine extends AbsLineView implements OnClickListener {

	protected TextView txtValue, txtLink;
	protected OnClickCallback callback;
	protected String value, linkText;

	public LinkLine(Context context) {
		super(context);
	}

	public LinkLine(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public LinkLine(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	protected void init(Context cx, AttributeSet attrs) {
		super.init(cx, attrs);
		LayoutInflater.from(cx).inflate(R.layout.view_link_line, pnlMain, true);
		txtValue = findViewById(R.id.txtValue);
		txtLink = findViewById(R.id.txtLink);

		TypedArray ta = cx.obtainStyledAttributes(attrs, R.styleable.LinkLine);
		value = ta.getString(R.styleable.LinkLine_text);
		linkText = ta.getString(R.styleable.LinkLine_linkText);
		ta.recycle();

		txtValue.setText(value);
		txtLink.setText(linkText);
		txtLink.setOnClickListener(this);
	}

	public void setOnClickCallback(OnClickCallback callback) {
		this.callback = callback;
	}

	@Override
	public void onClick(View view) {

		int id = view.getId();
		if (id == R.id.txtLink) {
			if (callback != null) {
				callback.onClick(LinkLine.this, getText());
			}
		}

	}

	public String getText() {
		return txtValue.getText().toString();
	}

	public void setText(int resId) {
		this.value = UI.getString(getContext(), resId);
		txtValue.setText(resId);
	}

	public void setText(String value) {
		this.value = value;
		txtValue.setText(value);
	}

	public String getLinkText() {
		return txtLink.getText().toString();
	}

	public void setLinkText(String lnkText) {
		this.linkText = lnkText;
		txtLink.setText(lnkText);
	}

	public void setLinkText(int resId) {
		this.linkText = UI.getString(getContext(), resId);
		txtLink.setText(resId);
	}

	@Override
	public void setContentDisible(boolean isDisabled) {
		txtLink.setEnabled(!isDisabled);
	}

	public interface OnClickCallback {
		void onClick(View view, String value);
	}

}
