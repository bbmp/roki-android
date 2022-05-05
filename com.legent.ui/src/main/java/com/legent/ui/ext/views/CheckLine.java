package com.legent.ui.ext.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.legent.ui.R;

public class CheckLine extends AbsLineView implements OnCheckedChangeListener {

	public interface OnCheckedCallback {
		void onChecked(View view, boolean checked);
	}

	public CheckLine(Context context) {
		super(context);
	}

	public CheckLine(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CheckLine(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected ToggleButton chkBtn;

	protected boolean checked;
	protected OnCheckedCallback callback;

	@Override
	protected void init(Context cx, AttributeSet attrs) {
		super.init(cx, attrs);
		LayoutInflater.from(cx)
				.inflate(R.layout.view_check_line, pnlMain, true);
		chkBtn = findViewById(R.id.chkButton);

		int btnBgResid;
		if (attrs == null) {
			btnBgResid = R.drawable.common_switch_selector;
			checked = false;
		} else {
			TypedArray ta = cx.obtainStyledAttributes(attrs,
					R.styleable.CheckLine);
			checked = ta.getBoolean(R.styleable.CheckLine_checked, true);
			btnBgResid = ta.getResourceId(R.styleable.CheckLine_btnBackground,
					R.drawable.common_switch_selector);
			ta.recycle();
		}

		chkBtn.setChecked(checked);
		chkBtn.setBackgroundResource(btnBgResid);
		chkBtn.setOnCheckedChangeListener(this);

	}

	public void setOnCheckedCallback(OnCheckedCallback callback) {
		this.callback = callback;
	}

	@Override
	public void onCheckedChanged(CompoundButton btn, boolean checked) {

		if (callback != null && R.id.chkButton == btn.getId()) {
			callback.onChecked(this, getChecked());
		}
	}

	public boolean getChecked() {
		return chkBtn.isChecked();
	}

	public void setChecked(boolean checked) {
		chkBtn.setChecked(checked);
	}

	@Override
	public void setContentDisible(boolean isDisabled) {
		chkBtn.setEnabled(!isDisabled);
	}

}
