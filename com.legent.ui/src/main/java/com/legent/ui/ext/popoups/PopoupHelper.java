package com.legent.ui.ext.popoups;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.legent.Callback2;
import com.legent.ui.R;

import java.util.Calendar;
import java.util.List;

/**
 * Created by sylar on 15/8/4.
 */
public class PopoupHelper {

    public static void show(View parent, PopupWindow popupWindow, int gravity, Drawable drawable) {
        popupWindow.setBackgroundDrawable(drawable);
        show(parent, popupWindow, gravity);
    }

    public static void show(View parent, PopupWindow popupWindow, int gravity) {
        switch (gravity) {
            case Gravity.TOP:
                showTop(parent, popupWindow);
                break;
            case Gravity.BOTTOM:
                showBottom(parent, popupWindow);
                break;
            case Gravity.RIGHT:
                showRight(parent, popupWindow);
                break;
            default:
                showCenter(parent, popupWindow);
                break;
        }
    }

    public static void showTop(View parent, PopupWindow popupWindow) {
        int dp = 55;
        int px = Dp2PxUtils.dp2px(parent.getContext(), dp);
        popupWindow.showAtLocation(parent, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, px);
    }

    public static void showBottom(View parent, PopupWindow popupWindow) {
        popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public static void showCenter(View parent, PopupWindow popupWindow) {
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public static void showRight(View parent, PopupWindow popupWindow) {
        popupWindow.showAtLocation(parent, Gravity.RIGHT, 0, 0);
    }


    //---------------------------BasePickerPopupWindow---------------------------


}
