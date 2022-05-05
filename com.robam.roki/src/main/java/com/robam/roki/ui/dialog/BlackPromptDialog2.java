package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.robam.roki.R;

/**
 * Created by Rent on 2016/6/17.
 */
public class BlackPromptDialog2 extends Dialog {
    protected int layout_res;//布局文件
    View resview;
    protected View contentView;
    protected Context cx;
    public static BlackPromptDialog2 dlg;

    public BlackPromptDialog2(Context context) {
        super(context);
        init();
    }

    public BlackPromptDialog2(Context context, View view) {
        super(context, R.style.Dialog_Microwave_professtion);
        this.resview = view;
        this.cx = context;
        init();
    }

    public BlackPromptDialog2(Context context, View view, int style) {
        super(context, style);
        this.resview = view;
        this.cx = context;
        init();
    }

    protected void init() {
        if (resview != null)
            setContentView(resview);
    }

    public static BlackPromptDialog2 show(Context cx, View view) {
        dlg = new BlackPromptDialog2(cx, view);
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        Window window = dlg.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = displayMetrics.widthPixels;
        layoutParams.height = (int) (displayMetrics.heightPixels * 0.3);
        window.setAttributes(layoutParams);
        return dlg;
    }

    public static BlackPromptDialog2 show(Context cx, View view, int style) {
        if (style == 0)
            dlg = new BlackPromptDialog2(cx, view);
        else
            dlg = new BlackPromptDialog2(cx, view, style);
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        Window window = dlg.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = displayMetrics.widthPixels;
        layoutParams.height = (int) (displayMetrics.heightPixels * 0.3);
        window.setAttributes(layoutParams);
        return dlg;
    }


    public interface PickListener {
        void onCancel();

        void onConfirm(int what, Object m);
    }

    protected PickListener listener;

    public void setPickListener(PickListener listener) {
        this.listener = listener;
    }
}
