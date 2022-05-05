package com.robam.roki.utils;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.legent.utils.LogUtils;
import com.robam.roki.MobApp;



public class ToolUtils {


    public static void logEvent(String value1, String value2, String eventName) {
//        if (value1 != null) {
//            Bundle bundle = new Bundle();
//            bundle.putString("type", value1);
//            bundle.putString("action", value1 + ":" + value2);
//            MobApp.getmFirebaseAnalytics().logEvent(eventName, bundle);
//        } else {
//            LogUtils.i("logEvent20190731", "value is null");
//        }

    }

    public static void hideSoftInput(@NonNull Activity activity) {
        if (activity == null)
            return;
        Window window = activity.getWindow();
        if (window == null) {
           return;
        } else {
            View view = window.getCurrentFocus();
            if (view == null) {
                View decorView = window.getDecorView();
                View focusView = decorView.findViewWithTag("keyboardTagView");
                if (focusView == null) {
                    view = new EditText(window.getContext());
                    ((View)view).setTag("keyboardTagView");
                    ((ViewGroup)decorView).addView((View)view, 0, 0);
                } else {
                    view = focusView;
                }

                ((View)view).requestFocus();
            }

            hideSoftInput((View)view, activity);
        }
    }

    public static void hideSoftInput(@NonNull View view, Activity activity) {
        if (view == null) {
            return;
        } else {
            InputMethodManager imm = (InputMethodManager) activity.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
