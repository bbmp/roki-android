package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ViewUtils;
import com.robam.common.pojos.device.microwave.MicroWaveModel;
import com.robam.common.pojos.device.microwave.MicrowaveUtils;
import com.robam.roki.R;
import com.robam.roki.ui.view.DeviceModelNumWheel;
import com.robam.roki.ui.view.DeviceModelTimeWheel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/13.
 */
public class MicrowaveLinkArgumentSettingDialog extends AbsDialog {
    Context cx;
    @InjectView(R.id.mic_txtConfirm)
    TextView mic_txtConfirm;
    @InjectView(R.id.mic_modelwheel)
    DeviceModelNumWheel mic_modelwheel;//模式滚动条
    @InjectView(R.id.mic_firewheel)
    DeviceModelNumWheel mic_firewheel;//火力滚动条
    @InjectView(R.id.mic_timewheel)
    DeviceModelTimeWheel mic_timewheel;//时间滚动条
    public static MicrowaveLinkArgumentSettingDialog dlg;
    private short i;

    public MicrowaveLinkArgumentSettingDialog(Context context, short i) {
        super(context, R.style.Dialog_Microwave_professtion);
        ViewUtils.setBottmScreen(cx,this);
        this.cx = context;
        this.i = i;
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_microwave_linkagesetting;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        mic_modelwheel.setOnSelectListener(new DeviceModelNumWheel.OnSelectListener() {
            @Override
            public void endSelect(int index, Object item) {
                Log.i("mic", "endSelect-index:" + index);
                switch (index) {
                    case 0:
                        mic_timewheel.setData(MicrowaveUtils.create90TimeList());
                        mic_timewheel.setDefault(23);
                        mic_firewheel.setData(MicrowaveUtils.createBarbecueFireList());//设置火力范围
                        mic_firewheel.setDefault(0);//设置火力默认值
                        break;
                    case 1:
                        mic_timewheel.setData(MicrowaveUtils.create30TimeList());
                        mic_timewheel.setDefault(11);
                        mic_firewheel.setData(MicrowaveUtils.createMicroFireList());//设置火力范围
                        mic_firewheel.setDefault(5);//设置火力默认值
                        break;
                    case 2:
                        mic_timewheel.setData(MicrowaveUtils.create90TimeList());
                        mic_timewheel.setDefault(11);
                        mic_firewheel.setData(MicrowaveUtils.createCombineFireList());//设置火力范围
                        mic_firewheel.setDefault(0);//设置火力默认值
                        break;
                }
            }

            @Override
            public void selecting(int index, Object item) {
            }
        });
        List<String> list1 = Lists.newArrayList();
        list1.add("烧烤");
        list1.add("微波");
        list1.add("组合加热");
        mic_modelwheel.setData(list1);
        mic_modelwheel.setDefault(0);

        mic_firewheel.setData(MicrowaveUtils.createBarbecueFireList());//设置火力范围
        mic_firewheel.setDefault(0);//设置火力默认值
        mic_firewheel.setUnit("火力");
        mic_firewheel.setOnSelectListener(new DeviceModelNumWheel.OnSelectListener() {
            @Override
            public void endSelect(int index, Object item) {
                try {
                    if ("微波".equals(mic_modelwheel.getSelectedText()) &&
                            "6".equals(mic_firewheel.getSelectedText())) {
                        mic_timewheel.setData(MicrowaveUtils.create30TimeList());
                        mic_timewheel.setDefault(23);//设置时间默认值
                        is6 = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if ("微波".equals(mic_modelwheel.getSelectedText()) &&
                            !"6".equals(mic_firewheel.getSelectedText())) {
                        if (!is6) return;
                        mic_timewheel.setData(MicrowaveUtils.create90TimeList());
                        mic_timewheel.setDefault(23);
                        is6 = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void selecting(int index, Object item) {

            }
        });

        mic_timewheel.setData(MicrowaveUtils.create90TimeList());
        mic_timewheel.setDefault(23);//设置时间默认值
    }

    private boolean is6;

    @OnClick(R.id.mic_txtConfirm)//确认
    public void onClickConfirm() {
        int model;
        switch (mic_modelwheel.getSelected()) {
            case 0:
                model = MicroWaveModel.Barbecue;
                break;
            case 1:
                model = MicroWaveModel.MicroWave;
                break;
            case 2:
                model = MicroWaveModel.ComibineHeating;
                break;
            default:
                model = -100;
        }
        if (listener != null && model >= 0) {
            listener.onConfirm((short) model, Short.valueOf(String.valueOf(mic_firewheel.getSelectedTag())),
                    Short.valueOf(String.valueOf(mic_timewheel.getSelectedTag())));
            this.dismiss();
        }
    }

    static public MicrowaveLinkArgumentSettingDialog show(Context context, short i) {
        dlg = new MicrowaveLinkArgumentSettingDialog(context, i);
        Window win = dlg.getWindow();
        win.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }

    @OnClick(R.id.tv_microw_close)
    public void onViewClicked() {
        if(dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    public interface PickListener {

        void onCancel();

        void onConfirm(short model, short fire, short min);
    }

    private PickListener listener;

    public void setPickListener(PickListener listener) {
        Log.i("mic", "setPickListener");
        this.listener = listener;
    }

}
