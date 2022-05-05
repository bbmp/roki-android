package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.common.pojos.device.Oven.Oven016;
import com.robam.common.pojos.device.Oven.Oven039;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by linxiaobin on 2015/12/27.
 */
public class OvenBrokenDialog extends AbsDialog {


    @InjectView(R.id.txt)
    TextView txt;
    @InjectView(R.id.img)
    ImageView img;

    @InjectView(R.id.txtError)
    TextView txtError;

    String guid;
    public OvenBrokenDialog(Context context,String guid) {
        super(context, R.style.Theme_Dialog_HorziFullScreen);
        this.guid=guid;
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
        if (guid.contains("039")){
            switch (type) {
                case Oven039.Event_Oven_Alarm_Senor_Short:
                    txtError.setText("错误：E01");
                    break;
                case Oven039.Event_Oven_Alarm_Senor_Open:
                    txtError.setText("错误：E02");
                    break;
                case Oven039.Event_Oven039_Alarm_Senor_Fault:
                    txtError.setText("错误：E03");
                    break;
            }
        }else if (guid.contains("016")){
            switch (type){
                case Oven016.Event_Oven_Alarm_Senor_Fault:
                    txtError.setText("错误：E05");
                    break;
                case Oven016.Event_Oven_Alarm_Senor_Open:
                    txtError.setText("错误：E01");
                    break;
                case Oven016.Event_Oven_Alarm_Senor_Short:
                    txtError.setText("错误：E00");
                    break;
            }
        }else if (guid.contains("026")){
            switch (type){
                case Oven016.Event_Oven_Alarm_Senor_Fault:
                    txtError.setText("错误：E05");
                    break;
                case Oven016.Event_Oven_Alarm_Senor_Open:
                    txtError.setText("错误：E01");
                    break;
                case Oven016.Event_Oven_Alarm_Senor_Short:
                    txtError.setText("错误：E00");
                    break;
            }
        }

    }

    static public void show(Context cx, String guid, short type) {
        OvenBrokenDialog dlg = new OvenBrokenDialog(cx,guid);
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

