package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yinwei on 2017/12/26.
 */

public class DeviceSelectStoveHeadDialog extends AbsDialog {

    public interface PickNewDeviceSelectLister{
        void onConfirm(int stoveHead);
    }

    private DeviceSelectStoveHeadDialog.PickNewDeviceSelectLister lister;

    public void setPickNewDeviceSelectLister(DeviceSelectStoveHeadDialog.PickNewDeviceSelectLister lister){
        this.lister = lister;
    }

    public static DeviceSelectStoveHeadDialog dlg;

    Context cx;

    public DeviceSelectStoveHeadDialog(Context context) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.cx = context;
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_stovehead_select;
    }


    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    @OnClick(R.id.left_stove)
    public void onClickLeft(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                this.cancel();
            }
        }, 1500);
        if (lister!=null){
            lister.onConfirm(0);
        }
        dlg.dismiss();
    }

    @OnClick(R.id.right_stove)
    public void onClickRight(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                this.cancel();
            }
        }, 1500);
        if (lister!=null){
            lister.onConfirm(1);
        }
        dlg.dismiss();
    }

    @OnClick(R.id.cannel)
    public void onClickCannel(){
        dlg.dismiss();
    }


    static public DeviceSelectStoveHeadDialog show(Context cx) {
        dlg = new DeviceSelectStoveHeadDialog(cx);
        Window win = dlg.getWindow();
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = wm.getDefaultDisplay().getHeight();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = screenHeight/2;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }

}
