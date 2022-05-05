package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rosicky on 15/12/12.
 */
public class SteamOvenSensorBrokeDialog extends AbsDialog {

    @InjectView(R.id.txt)
    TextView txt;
    @InjectView(R.id.img)
    ImageView img;

    public SteamOvenSensorBrokeDialog(Context context) {
        super(context, R.style.Theme_Dialog_HorziFullScreen);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_sensor_broke_work;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    static public void show(Context cx, String one, short type) {
        SteamOvenSensorBrokeDialog dlg = new SteamOvenSensorBrokeDialog(cx);
        Window win = dlg.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.alpha = 0.6f;
        win.setAttributes(lp);
        dlg.show();
    }

    @OnClick(R.id.afterBuy)
    public void onClickAfterBuy() {
        UIService.getInstance().postPage(PageKey.SaleService);
        dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        return false;
    }
}
