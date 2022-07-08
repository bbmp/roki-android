package com.robam.roki.utils;


import android.os.Bundle;

import com.legent.utils.LogUtils;
import com.robam.roki.MobApp;



public class ToolUtils {


    public static void logEvent(String value1, String value2, String eventName) {
        if (value1 != null) {
            Bundle bundle = new Bundle();
            bundle.putString("type", value1);
            bundle.putString("action", value1 + ":" + value2);
            MobApp.getmFirebaseAnalytics().logEvent(eventName, bundle);
        } else {
            LogUtils.i("logEvent20190731", "value is null");
        }

    }
}
