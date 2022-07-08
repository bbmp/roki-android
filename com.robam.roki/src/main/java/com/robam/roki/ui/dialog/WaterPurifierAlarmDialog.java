package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/05/31.
 */
public class WaterPurifierAlarmDialog extends AbsDialog {
    @InjectView(R.id.water_alarm_btn)
    Button water_alarm_btn;
    protected  static int res;
    static  WaterPurifierAlarmDialog wad;
    public WaterPurifierAlarmDialog(Context context,int id) {
        super(context, R.style.WatreDialog_FullScreen);

    }
    @Override
    protected int getViewResId() {

        return res;
    }
    public static void setRes(int resId){
        res=resId;
    }
    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }
    public static  Dialog show(Context cx,int id) {
        wad = new WaterPurifierAlarmDialog(cx,id);
        WindowManager wm = (WindowManager)cx.getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        DisplayMetrics dm=new DisplayMetrics();
        display.getMetrics(dm);

        Window win = wad.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        //lp.gravity= Gravity.BOTTOM;
        lp.width = (int)(dm.widthPixels*0.86);
        lp.height=(int) (dm.heightPixels*0.8);
        win.setAttributes(lp);
        wad.show();
        return wad;
    }



    @OnClick(R.id.water_alarm_btn)
    public void onClickServer(){
        Uri uri = Uri.parse(String.format("tel:%s","95105855"));
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        cx.startActivity(it);
        wad.dismiss();
    }
}

