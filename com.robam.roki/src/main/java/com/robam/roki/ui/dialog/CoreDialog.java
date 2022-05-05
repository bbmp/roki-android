package com.robam.roki.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.robam.roki.utils.DialogUtil;

/**
 * 对话框核心类
 */
public class CoreDialog extends Dialog {
	private Window mWindow = null;
	private View mView = null;
	private boolean isTouchToDismiss = true;
	private Context mContext = null;
    private boolean mNonActivityLevelDialog = false;
	private int mDialogHeight;

	public CoreDialog(Context context, int theme, View view) {
		super(context, theme);
		mView = view;
		mWindow = getWindow();
		mContext = context;
	}



	public CoreDialog(Context context, int theme, View view, boolean isDismiss) {
		super(context, theme);
		mContext = context;
		mView = view;
		mWindow = getWindow();
		isTouchToDismiss = isDismiss;
	}




	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(mView);
		mView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				autoAdjustHeight();
			}
		});
		try {
			int contentWidth = DialogUtil.getContentWidth(getContext());
			setDialogSize(contentWidth, 0);
		} catch (Exception e) {

		}
		if (!isTouchToDismiss) {
			setCanceledOnTouchOutside(false);
		}
	}

	/**
	 * 设置窗体的大小
	 * @param width 宽
	 * @param height 高
     */
	public void setDialogSize(int width, int height) {
		if (mWindow != null) {
			WindowManager.LayoutParams lp = mWindow.getAttributes();
			if (width > 0) {
				//lp.width = (int) (dm.widthPixels*0.5);
				lp.width = width;
			}
			if (height > 0) {
				lp.height = height;
			}
			mWindow.setAttributes(lp);
		}
	}

	/**
	* 设置显示位置
	*/
	public void setPosition(int gravity, int x, int y) {
		WindowManager.LayoutParams lp = mWindow.getAttributes();
		lp.x = x;
		lp.y = y;
		lp.gravity = gravity;
		mWindow.setAttributes(lp);
	}

	/**
	 * 设置动画效果
 	 */
	public void setAnimation(int resId) {
		mWindow.setWindowAnimations(resId);
	}

	@Override
	public void show() {
		if (!mNonActivityLevelDialog && !isActivityValid()) {
			return;
		}

		try {
			super.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自动调整高度
	 */
	private void autoAdjustHeight() {
		// 高度不超过屏幕高度的80%
		int maxHeight = (int) (DialogUtil.getScreenHeight(mContext) * 0.5);
		int height = mView.getHeight();
		if (height > maxHeight) {
			height = maxHeight;
			if (mDialogHeight != height) {
				mDialogHeight = height;
				setDialogSize(0, mDialogHeight);
			}
		}
	}

	@Override
	public void dismiss() {
		try {
			super.dismiss();
		} catch (Exception e) {
		}
	}

	/**
	 * 判断activity是否有效
	 * @return
     */
	private boolean isActivityValid() {
		if (null != mContext && mContext instanceof Activity) {
			Activity at = (Activity) mContext;
            // /< 是 activity，但已finish
            // /< 是activity，还在运行中...
            return !at.isFinishing();
		} else {
			// /< context无效 或 context不是有效的activity
			return false;
		}
	}
}
