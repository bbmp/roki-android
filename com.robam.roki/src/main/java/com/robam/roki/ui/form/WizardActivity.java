package com.robam.roki.ui.form;


import android.app.Activity;
import android.content.Intent;

import com.legent.ui.ext.BaseActivity;
import com.robam.roki.ui.FormKey;


public class WizardActivity extends BaseActivity {

	/*@Override
	protected void setContentView() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(com.legent.ui.R.layout.abs_activity);
	}*/

	static public void start(Activity atv) {
		atv.startActivity(new Intent(atv, WizardActivity.class));
		atv.finish();
	}

	@Override
	protected String createFormKey() {
		return FormKey.WizardForm;
	}

}
