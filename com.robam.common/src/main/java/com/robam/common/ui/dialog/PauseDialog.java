package com.robam.common.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ViewUtils;
import com.robam.common.R;

public class PauseDialog extends AbsDialog {
	Context cx;

	public interface DialogCallback {
		void onRestore();
	}

	static public void show(Context cx, DialogCallback callback) {
		PauseDialog dlg = new PauseDialog(cx, callback);
		Window win = dlg.getWindow();
		win.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
		WindowManager.LayoutParams wl = win.getAttributes();
		wl.width = WindowManager.LayoutParams.MATCH_PARENT;
		wl.height = WindowManager.LayoutParams.MATCH_PARENT;
		win.setAttributes(wl);
		dlg.show();
	}

	DialogCallback callback;

	private PauseDialog(Context cx, DialogCallback callback) {
		super(cx, R.style.Theme_Dialog_FullScreen);

		ViewUtils.setFullScreen(cx, this);
		this.callback = callback;
	}

	@Override
	protected int getViewResId() {
		return R.layout.dialog_pause;
	}

	@Override
	protected void initView(View view) {

		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (callback != null) {
					callback.onRestore();
				}
			}
		});
	}

}
