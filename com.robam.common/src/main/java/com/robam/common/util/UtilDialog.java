package com.robam.common.util;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/7/18.
 */
public class UtilDialog {
    public static Dialog setDialogWidthAndHeight(Context cx,Dialog dialog, float scale) {
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = displayMetrics.widthPixels;
        layoutParams.height = (int) (displayMetrics.heightPixels * scale);
        window.setAttributes(layoutParams);
        return dialog;
    }
}
