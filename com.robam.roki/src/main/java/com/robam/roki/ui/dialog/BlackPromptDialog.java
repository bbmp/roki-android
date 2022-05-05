package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.robam.roki.R;

/**
 * Created by Rent on 2016/6/17.
 */
public class BlackPromptDialog extends Dialog {
    protected int layout_res;//布局文件
    protected View contentView;
    protected Context cx;
    public static BlackPromptDialog dlg;
    public BlackPromptDialog(Context context) {
        super(context);
        init();
    }

    public BlackPromptDialog(Context context, int res) {
        super(context, R.style.Dialog_Microwave_professtion);
        this.layout_res = res;
        this.cx = context;
        init();
    }

    protected void init() {
        if (layout_res == 0) return;
        contentView = LayoutInflater.from(cx)
                .inflate(layout_res, null, false);
        setContentView(contentView);
    }

    public static BlackPromptDialog show(Context cx, int res) {
        dlg = new BlackPromptDialog(cx, res);
        WindowManager wm= (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        Window window=dlg.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width=displayMetrics.widthPixels;
        layoutParams.height=(int)(displayMetrics.heightPixels*0.4);
        window.setAttributes(layoutParams);
        return dlg;
    }


    public interface PickListener {
        void onCancel();
        void onConfirm(int what,Object m);
    }

    protected PickListener listener;

    public void setPickListener(PickListener listener) {
        this.listener = listener;
    }
}
