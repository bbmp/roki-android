package com.robam.roki.ui.view.networkoptimization;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.robam.roki.R;

import butterknife.ButterKnife;

public class WifiAuthDialog extends Dialog {

    WifiListView wifiListView;
    WifiConnectPage wifiConnectPage;
    Context cx;

    public WifiAuthDialog(Context context, WifiConnectPage wifiConnectPage) {
        super(context, R.style.Theme_Dialog_FullScreen);
        this.wifiConnectPage=wifiConnectPage;
        loadView(context);
    }

    public WifiAuthDialog(Context context, int theme, WifiConnectPage wifiConnectPage) {
        super(context, theme);
        this.wifiConnectPage=wifiConnectPage;
        loadView(context);
    }
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    protected void loadView(Context cx) {
        this.cx = cx;
        wifiListView = new WifiListView(cx,this,wifiConnectPage);
        setContentView(wifiListView);
        initView(wifiListView);
    }

    public static void show(Context cx, String title, WifiConnectPage wifiConnectPage) {
        WifiAuthDialog dlg = new WifiAuthDialog(cx,wifiConnectPage);
        Window win = dlg.getWindow();
        win.setGravity(Gravity.TOP);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = 500;
        lp.y=500;
        dlg.getWindow().setAttributes(lp);
        dlg.setTitle(title);
        dlg.show();
    }

}
