package com.robam.roki.ui.activity3.temporary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.legent.plat.events.PageBackEvent;
import com.legent.utils.EventUtils;
import com.robam.common.pojos.Recipe;
import com.robam.roki.ui.FormKey;
import com.robam.roki.ui.form.BaseRokiActivity;

/**
 * 临时activity 兼容之前的写法使用
 */

public class TemporaryActivity extends BaseRokiActivity {

    private static String key ;

    static public void start(Activity atv , String pageKey) {
        atv.startActivity(new Intent(atv, TemporaryActivity.class)
        .putExtra(WillShowPageKey , pageKey));

    }

    @Override
    protected String createFormKey() {
        return FormKey.Temporary;
    }



}
