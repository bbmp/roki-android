package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.MicroWaveStatusChangedEvent;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
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

/**
 * Created by Administrator on 2017/4/11.
 */

public class Microwave526WeightTimeSettingDialog extends AbsDialog {
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.weighttime_firewheel)
    DeviceNumWheel fireNum;
    @InjectView(R.id.weighttime_timewheel)
    DeviceMicrowaveTimeWheel minites;
  /*  @InjectView(R.id.weighttime_timesecond)
    DeviceMicrowaveTimeWheel second;*/
    @InjectView(R.id.bottom_desc)
    TextView bottom_desc;
    public static Microwave526WeightTimeSettingDialog dlg;
    Context cx;
    short model;//模式
    private View customView;
    MicroWaveM526 microWave;
    public Microwave526WeightTimeSettingDialog(Context context, short model, AbsMicroWave microWave) {
        super(context,R.style.Dialog_Micro_FullScreen);
        this.cx = context;
        this.model=model;
        this.microWave= (MicroWaveM526) microWave;
        init();
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_microwave526_weighttimesetting1;
    }

    private boolean preItemIs6=false;

    private void init() {
        switch (model){
            case MicroWaveModel.MicroWave:
                fireNum.setData(MicrowaveUtils.createMicroFireList());
                fireNum.setDefault(5);//设置火力默认值

                minites.setData(MicrowaveUtils.create30TimeList());
                minites.setDefault(11);//设置时间默认值
                title.setText("微波");
                bottom_desc.setText("* 使食物均匀加热，越大越厚的食物选用越高的火力越适合。");
                break;
            case MicroWaveModel.Barbecue://烧烤
                fireNum.setData(MicrowaveUtils.createBarbecueFireList());
                fireNum.setDefault(0);//设置火力默认值
                minites.setData(MicrowaveUtils.create90TimeList());
                minites.setDefault(23);//烧烤设置时间默认值
                title.setText("烧烤");
                bottom_desc.setText("* 高火力(6)适合烤薄肉片；\n中火力(4)适合薄的海鲜类和吐司；\n低火力(2)适合较厚的肉片。\n烹饪中途翻转食材，效果会更好。");
                break;
            case MicroWaveModel.ComibineHeating://组合加热
                fireNum.setData(MicrowaveUtils.createCombineFireList());
                fireNum.setDefault(0);//设置火力默认值
                minites.setData(MicrowaveUtils.create90TimeList());
                minites.setDefault(11);//组合加热设置时间默认值e
                title.setText("组合加热");
                bottom_desc.setText("* 适合烧烤较厚实的家禽类、肉类及海鲜等。既可确保食物熟透，又达到烘烤类食物特有的脆性。烹饪中途翻转食材，效果会更好。");
                break;
        }
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        String str1 = "火力";

        fireNum.setUnit(str1);
        fireNum.setOnSelectListener(new DeviceNumWheel.OnSelectListener() {
            @Override
            public void endSelect(int index, Object item) {
                Log.i("mic", "endSelect-index:" + index);
                if(model!=MicroWaveModel.MicroWave)return;
                if((Integer)item==6){
                    minites.setData(MicrowaveUtils.create30TimeList());
                    minites.setDefault(11);//设置时间默认值
                    preItemIs6=true;
                }else{
                    if(preItemIs6){
                        minites.setData(MicrowaveUtils.create90TimeList());
                        minites.setDefault(11);//设置时间默认值
                        preItemIs6=false;
                    }
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
                if (dlg!=null&&dlg.isShowing()){
                    dlg.dismiss();
                }
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.imgreturn)
    public  void onClickBack(){
        if(dlg!=null&&dlg.isShowing())
            dlg.dismiss();
    }

    @OnClick(R.id.start_cook)//确认
   public void onClickStartCook() {
        ToastUtils.show("点我啦", Toast.LENGTH_SHORT);
        if (listener != null && model >= 0) {
            listener.onConfirm(model, Short.valueOf(String.valueOf(fireNum.getSelectedTag())), Short.valueOf(String.valueOf(minites.getSelectedTag())));
        }
        if(dlg!=null&&dlg.isShowing())
            dlg.dismiss();
    }

    static public Microwave526WeightTimeSettingDialog show(Context context, short model,AbsMicroWave microWave) {
        dlg = new Microwave526WeightTimeSettingDialog(context, model,microWave);
        Window win = dlg.getWindow();
       // win.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }

    public interface PickListener {
        void onCancel();
        void onConfirm(short model, short fire, short min);
    }

    private Microwave526WeightTimeSettingDialog.PickListener listener;

    public void setPickListener(Microwave526WeightTimeSettingDialog.PickListener listener) {
        Log.i("mic", "setPickListener");
        this.listener = listener;
    }
}
