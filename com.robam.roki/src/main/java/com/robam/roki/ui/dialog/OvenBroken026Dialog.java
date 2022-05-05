package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by linxiaobin on 2015/12/27.
 */
public class OvenBroken026Dialog extends AbsDialog {

    @InjectView(R.id.txt)
    TextView txt;
    @InjectView(R.id.img)
    ImageView img;

    @InjectView(R.id.txtError)
    TextView txtError;

    public OvenBroken026Dialog(Context context) {
        super(context, R.style.Theme_Dialog_HorziFullScreen);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_oven_broken;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    public void setText(String s) {
        if (s != null) txt.setText(s);
    }

    public void changeErrorText(short type) {
        switch (type) {
            case AbsOven.Event_Oven_Heat_Fault:
                txtError.setText("错误：E03");
                break;
            case AbsOven.Event_Oven_Alarm_Senor_Fault:
                txtError.setText("错误：E05");
                break;
            case AbsOven.Event_Oven_Communication_Fault:
                txtError.setText("错误：E06");
                break;
            default:
                break;
        }
    }

    static public void show(Context cx, String one, short type) {
        OvenBroken026Dialog dlg = new OvenBroken026Dialog(cx);
        Window win = dlg.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.alpha = 0.6f;
        win.setAttributes(lp);
        dlg.changeErrorText(type);
        dlg.show();
    }

    @OnClick(R.id.afterBuy)
    public void onClickAfterBuy() {
        dismiss();
        UIService.getInstance().postPage(PageKey.SaleService);
    }

}

