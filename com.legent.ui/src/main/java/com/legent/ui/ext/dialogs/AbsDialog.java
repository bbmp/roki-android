package com.legent.ui.ext.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.legent.ui.R;

abstract public class AbsDialog extends ParentDialog {

	abstract protected int getViewResId();

    protected Context cx;

	public AbsDialog(Context context) {
		this(context, R.style.Theme_Dialog);
		loadView(context);
	}

	public AbsDialog(Context context, int theme) {
		super(context, theme);
		loadView(context);
	}

	protected void loadView(Context cx) {
        this.cx = cx;
		View view = LayoutInflater.from(cx)
				.inflate(getViewResId(), null, false);
		setContentView(view);
		initView(view);
	}

	protected void initView(View view) {

	}

}
