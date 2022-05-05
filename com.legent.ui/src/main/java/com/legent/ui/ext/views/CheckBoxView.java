package com.legent.ui.ext.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

/**
 * Created by sylar on 15/6/20.
 */
public class CheckBoxView extends ToggleButton {
    public CheckBoxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public CheckBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CheckBoxView(Context context) {
        super(context);
        init(context, null);
    }

    void init(Context cx, AttributeSet attrs) {
        setTextOn("");
        setTextOff("");
    }
}
