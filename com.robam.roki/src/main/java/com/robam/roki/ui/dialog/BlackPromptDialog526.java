package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/20.
 */

public class BlackPromptDialog526 extends AbsDialog {

    protected View contentView;
    protected Context cx;
    public static BlackPromptDialog526 dlg;
    short state;

    public BlackPromptDialog526(Context context, short state) {
        super(context, R.style.Dialog_Microwave_professtion);
        this.cx = context;
        this.state=state;
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_microwave526_door_notclosed;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    public static BlackPromptDialog526 show(Context cx, short state) {
        dlg = new BlackPromptDialog526(cx, state);
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

    @OnClick(R.id.micro526_known)
    public void onClickKnow(){
        if(dlg!=null && dlg.isShowing()){
            dlg.dismiss();
        }
    }
    public interface PickListener {
        void onCancel();
        void onConfirm(int what,Object m);
    }

    protected BlackPromptDialog526.PickListener listener;

    public void setPickListener(BlackPromptDialog526.PickListener listener) {
        this.listener = listener;
    }
}


