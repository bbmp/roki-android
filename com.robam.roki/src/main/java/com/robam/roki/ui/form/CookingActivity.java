package com.robam.roki.ui.form;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.PowerManager;

import com.legent.ui.ext.BaseActivity;
import com.legent.ui.ext.dialogs.DialogHelper;
import com.robam.roki.service.MobileStoveCookTaskService;
import com.robam.roki.ui.FormKey;

public class CookingActivity extends BaseActivity{

	static public void start(Activity atv) {
		atv.startActivity(new Intent(atv, CookingActivity.class));
	}

	PowerManager pm;
	PowerManager.WakeLock wl;

	@Override
	protected String createFormKey() {
		return FormKey.CookingForm;
	}

	@Override
	protected void onKeyDown_Back() {
		onClose();
	}

	private void onClose() {
		String message = "正在烧菜中,是否确定退出？";
		DialogHelper.newDialog_OkCancel(this, null, message,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dlg, int witch) {
						if (witch == DialogInterface.BUTTON_POSITIVE) {
							MobileStoveCookTaskService.getInstance().stop();
						}
					}
				}).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,"CookingActivity");
		// 在释放之前，屏幕一直亮着（有可能会变暗,但是还可以看到屏幕内容,换成PowerManager.SCREEN_BRIGHT_WAKE_LOCK不会变暗，
		// SCREEN_DIM_WAKE_LOCK会变暗）
		wl.acquire();
	}

	@Override
	protected void onPause() {
		super.onPause();
		wl.release();
	}
}
