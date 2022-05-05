package com.robam.roki.ui.form;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.legent.ui.AbsActivity;
//import com.legent.ui.ext.BaseActivity;
import com.legent.utils.EventUtils;
import com.robam.common.events.Detailevnet;
import com.robam.roki.ui.FormKey;


/**
 * @author r210190
 * des 启动页
 */
public class WelcomeActivity extends AbsActivity {

	 public static void start(Activity atv) {
		atv.startActivity(new Intent(atv, WelcomeActivity.class));
		atv.finish();
	}

	@Override
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);
	}

	private static final String TAG = "WelcomeActivity";
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.e(TAG,"onNewIntent");


	}

	@Override
	protected void setContentView() {
		super.setContentView();
//	   EventUtils.postEvent(new Detailevnet(Long.parseLong("6")));

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected String createFormKey() {
		return FormKey.WelcomeForm;
	}


	@Override
	protected void onKeyDown_Back() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");
	}

	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
}
