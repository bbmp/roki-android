package com.legent.ui.ext.dialogs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.widget.TimePicker;

import com.legent.events.PageChangedEvent;
import com.legent.ui.UI;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ViewUtils;

import java.util.Calendar;
import java.util.Date;

public class TimeDialog {

	public static void show(Context cx, String title, Date time,
			final TimeSeletedCallback callback) {

		final TimePicker tpView = new TimePicker(cx);
		tpView.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
		tpView.setIs24HourView(true);

		if (time != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(time);
			tpView.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
			tpView.setCurrentMinute(c.get(Calendar.MINUTE));
		}

		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				tpView.clearFocus();
				ViewUtils.setDialogShowField(dialog, true);

				if (which == DialogInterface.BUTTON_POSITIVE) {

					if (callback != null) {
						Calendar c = Calendar.getInstance();
						c.set(Calendar.HOUR_OF_DAY, tpView.getCurrentHour());
						c.set(Calendar.MINUTE, tpView.getCurrentMinute());
						callback.onTimeSeleted(c.getTime());
					}
				}
			}
		};

		Builder builder = new Builder(cx);
		builder.setTitle(title);
		builder.setView(tpView);
//		builder.setNegativeButton(UI.getStr_Cancel(cx), clickListener);
		builder.setNegativeButton("取消", clickListener);
//		builder.setPositiveButton(UI.getStr_No(cx), clickListener);
		builder.setPositiveButton("否", clickListener);

		AlertDialog dlg = builder.create();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			dlg.setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {
					EventUtils.postEvent(new PageChangedEvent("dialogshow"));
				}
			});
		}
		dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				EventUtils.postEvent(new PageChangedEvent("dialogdismiss"));
			}
		});
		dlg.show();
	}

	public interface TimeSeletedCallback {
		void onTimeSeleted(Date time);
	}
}
