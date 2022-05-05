package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/2/26.
 */

public class ShowRemind extends AbsDialog {
    @InjectView(R.id.txt)
    TextView txt;
    @InjectView(R.id.img)
    ImageView img;

    public ShowRemind(Context context) {
        super(context, R.style.Theme_Dialog_HorziFullScreen);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dailog_show_remind;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    public void setText(String s) {
        if (s != null) txt.setText(s);
    }


    static public void show(Context cx, String guid) {
        OvenBrokenDialog dlg = new OvenBrokenDialog(cx,guid);
        Window win = dlg.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.alpha = 0.6f;
        win.setAttributes(lp);
        dlg.show();
    }

}
