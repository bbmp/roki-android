package com.legent.ui.ext.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.legent.ui.R;

import java.util.List;

public class RadioLine extends AbsLineView implements OnCheckedChangeListener {

	public interface OnCheckedCallback {
		void onChecked(View view, int checkedIndex, Object checkedItem);
	}

	public RadioLine(Context context) {
		super(context);
	}

	public RadioLine(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RadioLine(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected RadioGroup group;
	protected OnCheckedCallback callback;

	@Override
	protected void init(Context cx, AttributeSet attrs) {
		super.init(cx, attrs);
		LayoutInflater.from(cx)
				.inflate(R.layout.view_radio_line, pnlMain, true);
		group = findViewById(R.id.pnlGroup);
		group.setOnCheckedChangeListener(this);
	}

	public void setOnCheckedCallback(OnCheckedCallback callback) {
		this.callback = callback;
	}

	public <T> void loadItmes(List<T> items) {
		group.removeAllViews();
		if (items == null || items.size() == 0)
			return;
		RadioButton btn;
		Context cx = getContext();
		ViewGroup.MarginLayoutParams params;

		for (T item : items) {
			btn = new RadioButton(cx);
			btn.setText(item.toString());
			btn.setTag(item);
			group.addView(btn);

			params = (ViewGroup.MarginLayoutParams) btn.getLayoutParams();
			params.leftMargin = 10;
		}
	}

	public void setChecked(int index) {
		if (group.getChildCount() <= index)
			return;
		RadioButton btn = (RadioButton) group.getChildAt(index);
		if (btn == null)
			return;
		btn.setChecked(true);
	}

	public <T> void setChecked(T item) {
		RadioButton btn;
		for (int i = 0; i < group.getChildCount(); i++) {
			btn = (RadioButton) group.getChildAt(i);
			if (btn.getTag() == item) {
				btn.setChecked(true);
				break;
			}
		}
	}

	public int getCheckedIndex() {
		int index = -1;
		for (int i = 0; i < group.getChildCount(); i++) {
			if (((RadioButton) group.getChildAt(i)).isChecked()) {
				index = i;
				break;
			}
		}
		return index;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		View view = group.findViewById(checkedId);
		Object obj = view.getTag();
		int index = group.indexOfChild(view);

		if (callback != null) {
			callback.onChecked(this, index, obj);
		}
	}

	@Override
	public void setContentDisible(boolean isDisabled) {
		group.setEnabled(!isDisabled);
	}

}
