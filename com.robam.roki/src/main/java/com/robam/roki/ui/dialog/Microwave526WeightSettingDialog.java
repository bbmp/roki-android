package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveM526;
import com.robam.common.pojos.device.microwave.MicroWaveModel;
import com.robam.common.pojos.device.microwave.MicrowaveUtils;
import com.robam.roki.R;
import com.robam.roki.ui.view.DeviceNumWheel526;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/10.
 */

public class Microwave526WeightSettingDialog extends AbsDialog {
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.weight_weightwheel)
    DeviceNumWheel526 weight_weightwheel;
    @InjectView(R.id.bottom_desc)
    TextView bottom_desc;
    @InjectView(R.id.start_cook)
    Button start_cook;
    public static Microwave526WeightSettingDialog dlg;
    Context cx;
    short model;//模式
    private View customView;
    MicroWaveM526 microWave;
    public Microwave526WeightSettingDialog(Context context, short model, AbsMicroWave microWave) {
        super(context,R.style.Dialog_Micro_FullScreen);
        this.cx = context;
        this.model = model;
        this.microWave= (MicroWaveM526) microWave;
        init();
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_microwave526_weightsetting;
    }

    @Override
    protected void initView(View view) {
        this.customView=view;
    }

    private void init() {
        ButterKnife.inject(this, customView);
        weight_weightwheel.setData(generateModelWheel());
        weight_weightwheel.setDefault(0);//设置重量默认值
    }

    private List<Integer> generateModelWheel() {
        List<Integer> list = Lists.newArrayList();
        switch (model) {
            case MicroWaveModel.Meat:
                list = MicrowaveUtils.createWeightList(Short.valueOf("200"), Short.valueOf("600"),
                        Short.valueOf("100"));
                title.setText("黑椒牛排");
                weight_weightwheel.setUnit("克");
                break;
            case MicroWaveModel.Checken:
                list = MicrowaveUtils.createWeightList(Short.valueOf("600"), Short.valueOf("1200"),
                        Short.valueOf("100"));
                title.setText("香烤全鸡");
                weight_weightwheel.setUnit("克");
                break;
            case MicroWaveModel.Kebab:
                list = MicrowaveUtils.createWeightList(Short.valueOf("100"), Short.valueOf("400"),
                        Short.valueOf("100"));
                title.setText("风味肉串");
                weight_weightwheel.setUnit("克");
                break;
            case MicroWaveModel.Rice:
                list = MicrowaveUtils.createWeightList(Short.valueOf("200"), Short.valueOf("600"),
                        Short.valueOf("100"));
                title.setText("煮饭");
                weight_weightwheel.setUnit("克");
                break;
            case MicroWaveModel.Porridge:
                list = MicrowaveUtils.createWeightList(Short.valueOf("50"), Short.valueOf("200"),
                        Short.valueOf("50"));
                title.setText("煮粥");
                weight_weightwheel.setUnit("克");
                break;
            case MicroWaveModel.Vegetables:
                list = MicrowaveUtils.createWeightList(Short.valueOf("100"), Short.valueOf("500"),
                        Short.valueOf("100"));
                title.setText("炒时蔬");
                weight_weightwheel.setUnit("克");
                break;
            case MicroWaveModel.Fish:
                list = MicrowaveUtils.createWeightList(Short.valueOf("100"), Short.valueOf("400"),
                        Short.valueOf("100"));
                title.setText("蒸鱼");
                weight_weightwheel.setUnit("克");
                break;
            case MicroWaveModel.Milk:
                list = MicrowaveUtils.createWeightList(Short.valueOf("1"), Short.valueOf("2"),
                        Short.valueOf("1"));
                title.setText("煲汤");
                weight_weightwheel.setUnit("份");
                break;
            case MicroWaveModel.Bread:
                list = MicrowaveUtils.createWeightList(Short.valueOf("1"), Short.valueOf("2"),
                        Short.valueOf("1"));
                title.setText("早餐面包");
                weight_weightwheel.setUnit("份");
                break;
            case MicroWaveModel.Unfreeze://解冻
                list = MicrowaveUtils.createWeightList(Short.valueOf("100"), Short.valueOf("3000"),
                        Short.valueOf("100"));
                title.setText("解冻");
                weight_weightwheel.setUnit("克");
                bottom_desc.setText("* 适宜加热各种冷冻食材，微波中途翻转食材，效果会更好。");
                break;
            case MicroWaveModel.HeatingAgain://再加热
                list = MicrowaveUtils.createWeightList(Short.valueOf("200"), Short.valueOf("600"),
                        Short.valueOf("100"));
                title.setText("再加热");
                weight_weightwheel.setUnit("克");
                bottom_desc.setText("* 此功能比较适合冰箱冷藏室中的冷饭或冷菜再加热。");
                break;
            default:
                break;
        }
        return list;
    }

    @OnClick(R.id.imgreturn)
    public void onClickBack(){
        if (dlg!=null&&dlg.isShowing()){
            dlg.dismiss();
        }
    }
    //开始烹饪
    @OnClick(R.id.start_cook)
    public void onClickCook(){
        if (listener != null && model >= 0){
            listener.onConfirm(Short.valueOf(String.valueOf(weight_weightwheel.getSelectedTag())));
        }

        if(dlg!=null&&dlg.isShowing())
            dlg.dismiss();

    }

    static public Microwave526WeightSettingDialog show(Context context, short model,AbsMicroWave microWave) {
        dlg = new Microwave526WeightSettingDialog(context, model,microWave);
        Window win = dlg.getWindow();
       // win.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }

    public interface PickListener {

        void onCancel();

        void onConfirm(short weight);
    }

    private Microwave526WeightSettingDialog.PickListener listener;

    public void setPickListener(Microwave526WeightSettingDialog.PickListener listener) {
        Log.i("mic", "setPickListener");
        this.listener = listener;
    }

}
