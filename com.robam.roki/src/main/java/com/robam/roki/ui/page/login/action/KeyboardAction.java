package com.robam.roki.ui.page.login.action;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

public interface KeyboardAction {
    default void showKeyboard(View view) {
        if (view == null)
            return;
        InputMethodManager manager = (InputMethodManager)view.getContext().getSystemService("input_method");
        if (manager != null)
            manager.showSoftInput(view, 0);
    }

    default void hideKeyboard(View view) {
        if (view == null)
            return;
        InputMethodManager manager = (InputMethodManager)view.getContext().getSystemService("input_method");
        if (manager != null)
            manager.hideSoftInputFromWindow(view.getWindowToken(), 2);
    }

    default void toggleSoftInput(View view) {
        if (view == null)
            return;
        InputMethodManager manager = (InputMethodManager)view.getContext().getSystemService("input_method");
        if (manager != null)
            manager.toggleSoftInput(0, 0);
    }
}
