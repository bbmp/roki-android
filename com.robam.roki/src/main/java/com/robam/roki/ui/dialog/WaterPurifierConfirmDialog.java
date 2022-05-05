package com.robam.roki.ui.dialog;

import android.content.Context;
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
public class WaterPurifierConfirmDialog extends AbsDialog {
    @InjectView(R.id.dialog_water_confirm_pos)
    Button dialog_water_confirm_pos;
    @InjectView(R.id.dialog_water_confirm_neg)
    Button dialog_water_confirm_neg;
    DialogConfirmBtn posClick;
    DialogConfirmBtn negClick;

    public WaterPurifierConfirmDialog(Context context) {
        super(context, R.style.Dialog_FullScreen);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_water_confirm;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }
    public void setPosClick(DialogConfirmBtn listern){
        this.posClick=listern;
    }
    public void setNegClick(DialogConfirmBtn listern){
        this.negClick=listern;
    }
    @OnClick(R.id.dialog_water_confirm_pos)
    public void onClickPos(){
        if(posClick!=null){
            posClick.onClickListeren();
        }
    }
    @OnClick(R.id.dialog_water_confirm_neg)
    public void onClickNeg(){
        if(negClick!=null){
            negClick.onClickListeren();
        }
    }

    static public WaterPurifierConfirmDialog show(Context cx) {
        WaterPurifierConfirmDialog dlg = new WaterPurifierConfirmDialog(cx);
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);

        Window win = dlg.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (dm.widthPixels * 0.86);
        lp.height = (int) (dm.heightPixels * 0.7);
        lp.alpha = 0.75f;
        win.setAttributes(lp);
        dlg.show();return dlg;
    }

    public interface DialogConfirmBtn {
        void onClickListeren();
    }

}

