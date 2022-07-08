package com.robam.roki.ui.form;

import android.app.Activity;
import android.content.Intent;

import com.legent.ui.ext.BaseActivity;
import com.robam.roki.ui.FormKey;

/**
 * @author hxw
 */
public class UserActivity extends BaseActivity {

	static public void start(Activity atv) {
		atv.startActivity(new Intent(atv, UserActivity.class));
		atv.finish();
	}

	@Override
	protected String createFormKey() {
		return FormKey.UserForm;
	}

}
