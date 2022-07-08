package com.robam.roki.ui.form;

import static com.robam.roki.ui.page.SelectThemeDetailPage.TYPE_THEME_BANNER;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.legent.ui.ext.BaseActivity;
import com.legent.utils.EventUtils;
import com.robam.common.events.Detailevnet;
import com.robam.roki.ui.FormKey;
import com.robam.roki.ui.page.SelectThemeDetailPage;


/**
 * @author r210190
 * des 启动页
 */
public class WelcomeActivity extends BaseActivity {

	 public static void start(Activity atv) {
		atv.startActivity(new Intent(atv, WelcomeActivity.class));
		atv.finish();
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
//		EventUtils.postEvent(new Detailevnet(Long.parseLong("6")));

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
	//重写，不进行换肤
	@Override
	public void applySkin() {

	}
}
