package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveM526;
import com.robam.roki.R;
import com.robam.roki.ui.view.DeviceNumWheel526;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/11.
 */

public class Microwave526TimeSettingDialog extends AbsDialog {
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.weight_weightwheel)
    DeviceNumWheel526 weight_weightwheel;
    @InjectView(R.id.bottom_desc)
    TextView bottom_desc;
    public static Microwave526TimeSettingDialog dlg;
    Context cx;
    private View customView;
    MicroWaveM526 microWave;
    public Microwave526TimeSettingDialog(Context context, AbsMicroWave microWave) {
        super(context,R.style.Dialog_Micro_FullScreen);
        this.cx = context;
        this.microWave= (MicroWaveM526) microWave;
        init();
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_microwave526_weightsetting;
    }

    @Override
    protected void initView(View view) {
        this.customView = view;
    }

    private void init() {
        ButterKnife.inject(this, customView);
        weight_weightwheel.setData(generateModelWheelData());
        weight_weightwheel.setDefault(0);//设置重量默认值
        weight_weightwheel.setUnit("分");
        title.setText("快速加热");
        bottom_desc.setText("*适宜加热冷饭冷菜");

    }



    private List<Integer> generateModelWheelData() {
        List<Integer> list = Lists.newArrayList();
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
        return list;
    }

    @OnClick(R.id.imgreturn)
    public void onClickBack(){
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    @OnClick(R.id.start_cook)
    public void onClickStartCook(){
        if (listener != null){
            LogUtils.i("20170413","timeSetting:"+weight_weightwheel.getSelectedTag());
            listener.onConfirm(Short.valueOf(String.valueOf(weight_weightwheel.getSelectedTag())));
        }
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    static public Microwave526TimeSettingDialog show(Context context,AbsMicroWave microWave) {
        dlg = new Microwave526TimeSettingDialog(context,microWave);
        Window win = dlg.getWindow();
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }


    public interface PickListener {

        void onCancel();

        void onConfirm(short time);
    }

    private Microwave526TimeSettingDialog.PickListener listener;

    public void setPickListener(Microwave526TimeSettingDialog.PickListener listener) {
        Log.i("mic", "setPickListener");
        this.listener = listener;
    }
}
