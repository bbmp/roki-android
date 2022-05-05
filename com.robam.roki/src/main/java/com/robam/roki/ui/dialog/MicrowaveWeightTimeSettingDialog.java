package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.common.events.MicroWaveConnectStatus;
import com.robam.common.events.MicroWaveStatusChangedEvent;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveM509;
import com.robam.common.pojos.device.microwave.MicroWaveM526;
import com.robam.common.pojos.device.microwave.MicroWaveModel;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.pojos.device.microwave.MicrowaveUtils;
import com.robam.roki.R;
import com.robam.roki.ui.view.DeviceMicrowaveTimeWheel;
import com.robam.roki.ui.view.DeviceNumWheel;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.common.pojos.device.microwave.MicrowaveUtils.create526R30TimeList;

/**
 * Created by Rent on 2016/6/13.
 */
public class MicrowaveWeightTimeSettingDialog extends AbsDialog {
    @InjectView(R.id.weighttime_firewheel)
    DeviceMicrowaveTimeWheel fireNum;
    @InjectView(R.id.weighttime_timewheel)
    DeviceMicrowaveTimeWheel minites;
    @InjectView(R.id.weighttime_timesecond)
    DeviceMicrowaveTimeWheel second;
    public static MicrowaveWeightTimeSettingDialog dlg;
    Context cx;
    short model;//模式
    MicroWaveM509 microWave;

    public MicrowaveWeightTimeSettingDialog(Context context,short model, AbsMicroWave microWave) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.cx = context;
        this.model = model;
        this.microWave = (MicroWaveM509) microWave;
        init();
    }

    @Subscribe
    public void onEvent(MicroWaveConnectStatus event) {
        if (!event.flag) {
            if (dlg != null && dlg.isShowing())
                dlg.dismiss();
        }
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_microwave_weighttimesetting;
    }

    private boolean preItemIs6 = false;

    private void init() {
        switch (model) {
            case MicroWaveModel.MicroWave:
                fireNum.setData(MicrowaveUtils.createMicroFireList());
                fireNum.setDefault(5);//设置火力默认值

                minites.setData(MicrowaveUtils.create30TimeMinList());//MicrowaveUtils.create526R90TimeList()
                minites.setDefault(1);//设置时间默认值

                second.setData(create526R30TimeList());
                second.setDefault(0);
                break;
            case MicroWaveModel.Barbecue://烧烤
                fireNum.setData(MicrowaveUtils.createBarbecueFireList());
                fireNum.setDefault(2);//设置火力默认值
                minites.setData(MicrowaveUtils.create526R90TimeList());
                minites.setDefault(2);//烧烤设置时间默认值
                second.setData(create526R30TimeList());
                second.setDefault(0);
                break;
            case MicroWaveModel.ComibineHeating://组合加热
                fireNum.setData(MicrowaveUtils.createCombineFireList());
                fireNum.setDefault(2);//设置火力默认值
                minites.setData(MicrowaveUtils.create526R90TimeList());
                minites.setDefault(1);//组合加热设置时间默认值e
                second.setData(create526R30TimeList());
                second.setDefault(0);
                break;
        }
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        String str1 = "火力";
        fireNum.setUnit(str1);
        minites.setUnit("分");
        minites.setMinimumWidth(30);
        second.setUnit("秒");
        fireNum.setOnSelectListener(new DeviceMicrowaveTimeWheel.OnSelectListener() {
            @Override
            public void endSelect(int index, Object item) {
                Log.i("mic", "endSelect-index:" + index);
                if (model != MicroWaveModel.MicroWave) return;
                if ((Integer) item == 6) {
                    minites.setData(MicrowaveUtils.create30TimeMinList());
                    minites.setDefault(1);//设置时间默认值
                    second.setData(MicrowaveUtils.create526R30TimeList());
                    second.setDefault(0);
                    preItemIs6 = true;
                } else {
                    if (preItemIs6) {
                        minites.setData(MicrowaveUtils.create526R90TimeList());
                        minites.setDefault(1);//设置时间默认值
                        second.setData(MicrowaveUtils.create526R30TimeList());
                        second.setDefault(0);
                        preItemIs6 = false;
                    }
                }

            }
            @Override
            public void selecting(int index, Object item) {

            }
        });
        minites.setOnSelectListener(new DeviceMicrowaveTimeWheel.OnSelectListener() {
            @Override
            public void endSelect(int index, Object item) {
                int min = (int) minites.getSelectedTag();
                if (min == 0) {
                    second.setData(MicrowaveUtils.create526R5SecTimeList());
                    second.setDefault(0);
                } else if (min > 0 && min < 3) {
                    second.setData(MicrowaveUtils.create526R30TimeList());
                    second.setDefault(0);
                } else if (min >= 3 && min < 10) {
                    second.setData(MicrowaveUtils.create526R30SecTimeList());
                    second.setDefault(0);
                } else {
                    second.setData(MicrowaveUtils.create526R0SecTimeList());
                    second.setDefault(0);
                }
            }

            @Override
            public void selecting(int index, Object item) {

            }
        });
    }

    @Subscribe
    public void OnEvent(MicroWaveStatusChangedEvent event) {

        switch (microWave.state) {
            case MicroWaveStatus.Run:
            case MicroWaveStatus.Pause:
                if (dlg != null && dlg.isShowing()) {
                    dlg.dismiss();
                }
                break;
            default:
                break;
        }
    }


    @OnClick(R.id.start_cook)//确认
    public void onClickStartCook() {
        if (listener != null && model >= 0) {
            listener.onConfirm(model, Short.valueOf(String.valueOf(fireNum.getSelectedTag())),
                    Short.valueOf(String.valueOf(minites.getSelectedTag())), Short.valueOf(String.valueOf(second.getSelectedTag())));
        }
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    static public MicrowaveWeightTimeSettingDialog show(Context context, short model, AbsMicroWave microWave) {
        dlg = new MicrowaveWeightTimeSettingDialog(context, model, microWave);
        Window win = dlg.getWindow();
        // win.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }

    @OnClick(R.id.tvreturn)
    public void onViewClicked() {
        if(dlg!=null&&dlg.isShowing())
            dlg.dismiss();
    }

    public interface PickListener {
        void onCancel();

        void onConfirm(short model, short fire, short min, short sec);
    }

    private MicrowaveWeightTimeSettingDialog.PickListener listener;

    public void setPickListener(MicrowaveWeightTimeSettingDialog.PickListener listener) {
        Log.i("mic", "setPickListener");
        this.listener = listener;
    }
}
