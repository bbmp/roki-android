package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;
import com.robam.roki.ui.view.CircleProgressView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by yinwei on 2017/8/5.
 */

public class ZjShowDialog extends AbsDialog{
     static  ZjShowDialog zj;
    @InjectView(R.id.zj_pro)
    ProgressBar zj_pro;

    public static ZjShowDialog dlg;
    public ZjShowDialog(Context context) {
        super(context, R.style.Theme_Dialog_HorziFullScreen);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_zj_show_progress;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }


    public static ZjShowDialog show(Context cx) {
        dlg = new ZjShowDialog(cx);
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
}
