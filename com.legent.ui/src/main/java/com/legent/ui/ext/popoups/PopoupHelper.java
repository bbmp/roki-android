package com.legent.ui.ext.popoups;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.legent.Callback2;
import com.legent.ui.R;
import com.legent.ui.ext.views.DateWheelView;
import com.legent.ui.ext.views.SimpleAreaWheelView;
import com.legent.ui.ext.views.TimeWheelView;
import com.legent.ui.ext.views.WheelView;

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

    public static PopupWindow newDatePicker(Context cx, final Callback2<Calendar> callback) {
        return newDatePicker(cx, Calendar.getInstance(), callback);
    }

    public static PopupWindow newDatePicker(Context cx, Calendar c, final Callback2<Calendar> callback) {
        final DateWheelView view = new DateWheelView(cx);
        view.setDefault(c);

        BasePickerPopupWindow.PickListener listener = new BasePickerPopupWindow.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm() {
                if (callback != null) {
                    callback.onCompleted(view.getSelected());
                }
            }
        };

        PopupWindow pop = newBasePickerPopoup(cx, view, listener);
        return pop;
    }

    public static PopupWindow newTimePicker(Context cx, final Callback2<Calendar> callback) {
        return newTimePicker(cx, Calendar.getInstance(), callback);
    }

    public static PopupWindow newTimePicker(Context cx, Calendar c, final Callback2<Calendar> callback) {
        final TimeWheelView view = new TimeWheelView(cx);
        view.setDefault(c);

        BasePickerPopupWindow.PickListener listener = new BasePickerPopupWindow.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm() {
                if (callback != null) {
                    callback.onCompleted(view.getSelected());
                }
            }
        };

        PopupWindow pop = newBasePickerPopoup(cx, view, listener);
        return pop;
    }


    public static PopupWindow newSimpleAreaPicker(Context cx, final Callback2<String> callback) {
        final SimpleAreaWheelView view = new SimpleAreaWheelView(cx);
        BasePickerPopupWindow.PickListener listener = new BasePickerPopupWindow.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm() {
                if (callback != null) {
                    String str = String.format("%s%s%s",
                            view.getSelectedItem1(),
                            view.getSelectedItem2(),
                            view.getSelectedItem3());

                    callback.onCompleted(str);
                }
            }
        };

        PopupWindow pop = newBasePickerPopoup(cx, view, listener);
        return pop;
    }


    public static <T> PopupWindow newCustomPicker(Context cx, List<T> list, final Callback2<T> callback) {
        return newCustomPicker(cx, list, -1, callback);
    }

    public static <T> PopupWindow newCustomPicker(Context cx, List<T> list, int defaultIndex, final Callback2<T> callback) {
        final WheelView view = (WheelView) LayoutInflater.from(cx).inflate(R.layout.view_wheel, null);
        view.setData(list);

        if (defaultIndex >= 0 && defaultIndex < list.size()) {
            view.setDefault(defaultIndex);
        } else {
            view.setDefault(list.size() / 2);
        }

        BasePickerPopupWindow.PickListener listener = new BasePickerPopupWindow.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm() {
                if (callback != null) {
                    Object obj = null;
                    try {
                        obj = view.getSelectedTag();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    callback.onCompleted((T) obj);
                }
            }
        };

        PopupWindow pop = newBasePickerPopoup(cx, view, listener);
        return pop;
    }

    public static PopupWindow newBasePickerPopoup(Context cx, View customView, BasePickerPopupWindow.PickListener listener) {
        BasePickerPopupWindow pop = new BasePickerPopupWindow(cx, customView);
        pop.setBackgroundDrawable(cx.getResources().getDrawable(R.color.main_background));
        pop.setPickListener(listener);
        return pop;
    }


}
