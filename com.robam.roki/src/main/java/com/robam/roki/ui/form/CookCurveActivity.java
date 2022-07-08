package com.robam.roki.ui.form;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.legent.plat.events.PageBackEvent;
import com.legent.ui.ext.BaseActivity;
import com.legent.utils.EventUtils;
import com.robam.roki.ui.PageKey;


public class CookCurveActivity extends BaseActivity {


	static public void start(Activity atv) {
		atv.startActivity(new Intent(atv, CookCurveActivity.class));
		atv.finish();
	}
    static public void start(Activity atv, Bundle bd) {
        atv.startActivity(new Intent(atv, CookCurveActivity.class)
                .putExtras(bd)
        );

    }

    @Override
    protected void onKeyDown_Back() {
        EventUtils.postEvent(new PageBackEvent("CookCurveActivityBack"));
    }

	@Override
	protected String createFormKey() {
		return PageKey.DeviceStoveCurve;
	}

}
