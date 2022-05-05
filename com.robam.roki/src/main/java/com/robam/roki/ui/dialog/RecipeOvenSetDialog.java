package com.robam.roki.ui.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;


/**
 * Created by WZTCM on 2015/12/27.
 */
public class RecipeOvenSetDialog extends AbsDialog {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dismiss();
        }
    };

    public RecipeOvenSetDialog(Context context) {
        super(context, R.style.Theme_Dialog_HorziFullScreen);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_recipe_oven_set;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
    }

    static public void show(Context cx) {
        RecipeOvenSetDialog dlg = new RecipeOvenSetDialog(cx);
        Window win = dlg.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.alpha = 0.6f;
        win.setAttributes(lp);
        dlg.start();
        dlg.show();
    }

    public void start() {
        handler.sendEmptyMessageDelayed(0, 5000);
    }

}
