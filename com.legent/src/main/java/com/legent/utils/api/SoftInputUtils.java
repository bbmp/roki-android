package com.legent.utils.api;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SoftInputUtils {

    /**
     * 强制显示软键盘
     *
     * @param view
     */
    public static void show(Context cx, View view) {
        InputMethodManager imm = (InputMethodManager) cx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // 接受软键盘输入的编辑文本或其它视图
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 强制隐藏软键盘
     */
    public static void hide(Context cx, View view) {
        InputMethodManager imm = (InputMethodManager) cx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // 接受软键盘输入的编辑文本或其它视图
        imm.hideSoftInputFromInputMethod(view.getApplicationWindowToken(), 0);
    }


    /**
     * 强制隐藏软键盘
     *
     * @param atv
     */
    public static void hide(Activity atv) {

        final View view = atv.getCurrentFocus();
        if (view != null && view.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) atv
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 如果输入法打开则关闭，如果没打开则打开
     */
    public static void toggleInput(Context cx) {
        InputMethodManager imm = (InputMethodManager) cx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获取输入法打开的状态
     *
     * @return
     */
    public static boolean isOpen(Context cx) {
        InputMethodManager imm = (InputMethodManager) cx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        return isOpen;
    }

    public static void showSoftInput(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

}
