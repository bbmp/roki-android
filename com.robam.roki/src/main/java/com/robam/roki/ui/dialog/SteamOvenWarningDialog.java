package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.common.pojos.device.Steamoven.Steam209;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Rosicky on 15/12/12.
 */
public class SteamOvenWarningDialog extends AbsDialog {

    @InjectView(R.id.txt)
    TextView txt;
    @InjectView(R.id.img)
    ImageView img;

    public SteamOvenWarningDialog(Context context) {
        super(context, R.style.Theme_Dialog_HorziFullScreen);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_warning_work;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    public void setText(String s) {
        if (s != null) txt.setText(s);
    }

    public void changeImg(short type) {
        switch (type) {
            case Steam209.Steam_Door_Open:
                img.setImageResource(R.mipmap.img_steamoven_warning_no_door);
                txt.setText("蒸汽炉门未关");
                break;
            case Steam209.Event_Steam_Alarm_water:
                img.setImageResource(R.mipmap.img_steamoven_warning_lack_water);
                txt.setText("请取出水箱加满水");
                break;
        }
    }

    static public void show(Context cx, String one, short type) {
        SteamOvenWarningDialog dlg = new SteamOvenWarningDialog(cx);
        Window win = dlg.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.alpha = 0.6f;
        win.setAttributes(lp);
        dlg.setCanceledOnTouchOutside(false);
//        dlg.setCancelable(false);
        dlg.setText(one);
        dlg.changeImg(type);
        dlg.show();
    }

}
