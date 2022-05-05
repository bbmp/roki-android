package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.common.pojos.device.microwave.MicroWaveModel;
import com.robam.common.pojos.device.microwave.MicrowaveUtils;
import com.robam.roki.R;
import com.robam.roki.ui.view.DeviceMicrowaveTimeWheel;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/15.
 */

public class Micro526PauseSettingDialog extends AbsDialog {
    @InjectView(R.id.dialog_micro526_title)
    TextView title;
    @InjectView(R.id.micro526_resettime)
    DeviceMicrowaveTimeWheel micro526_resettime;
    Context cx;
    short model;
    public static Micro526PauseSettingDialog dlg;
    Micro526PauseSettingDialog.PickListener callback;
    public Micro526PauseSettingDialog(Context context, short model) {
        super(context,R.style.Dialog_Microwave_professtion);
        this.cx=context;
        this.model=model;
        init();
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_micro526_pausetimesetting;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    private void init() {
        switch (model) {
            case MicroWaveModel.MicroWave:
                micro526_resettime.setData(MicrowaveUtils.create30TimeList());
                micro526_resettime.setDefault(11);//设置时间默认值
                title.setText("微波");
                break;
            case MicroWaveModel.Barbecue:
                micro526_resettime.setData(MicrowaveUtils.create90TimeList());
                micro526_resettime.setDefault(23);//烧烤设置时间默认值
                title.setText("烧烤");
                break;
            case MicroWaveModel.ComibineHeating:
                micro526_resettime.setData(MicrowaveUtils.create90TimeList());
                micro526_resettime.setDefault(11);//组合加热设置时间默认值e
                title.setText("组合加热");
                break;
        }
    }

    static public Micro526PauseSettingDialog show(Context cx, short model) {
        dlg = new Micro526PauseSettingDialog(cx,model);
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        Window window = dlg.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = displayMetrics.widthPixels;
        layoutParams.height = (int) (displayMetrics.heightPixels * 0.7);
        window.setAttributes(layoutParams);
        dlg.show();
        return dlg;
    }


    //取消
    @OnClick(R.id.micro526_pausesetting_close)
    public void onClickCannel(){
        if (this != null && this.isShowing()) {
            this.dismiss();
        }
    }

    //确定
    @OnClick(R.id.micro526_pausesetting_confirm)
    public void onClickConfirm(){
        if (listener != null && model >= 0) {
            listener.onConfirm(Short.valueOf(String.valueOf(micro526_resettime.getSelectedTag())));
        }
        if(dlg!=null&&dlg.isShowing())
            dlg.dismiss();
    }



    public interface PickListener {
        void onCancel();

        void onConfirm(short min);
    }

    private  Micro526PauseSettingDialog.PickListener listener;

    public void setPickListener( Micro526PauseSettingDialog.PickListener listener) {
        Log.i("mic", "setPickListener");
        this.listener = listener;
    }
}
