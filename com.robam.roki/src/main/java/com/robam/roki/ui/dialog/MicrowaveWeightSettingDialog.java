package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.common.pojos.device.microwave.MicroWaveModel;
import com.robam.common.pojos.device.microwave.MicrowaveUtils;
import com.robam.roki.R;
import com.robam.roki.ui.view.DeviceNumWheel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/13.
 */
public class MicrowaveWeightSettingDialog extends AbsDialog {
    @InjectView(R.id.weight_weightwheel)
    DeviceNumWheel weight_weightwheel;//火力滚动条
    @InjectView(R.id.weight_titletext)
    TextView weight_titletext;//文字
    @InjectView(R.id.weight_img)
    ImageView weight_img;//图标
    @InjectView(R.id.dialog_microwave_weightsetting_bottomtext)
    TextView dialog_microwave_weightsetting_bottomtext;
    public static MicrowaveWeightSettingDialog dlg;
    Context cx;
    short model;//模式
    private View customView;

    public MicrowaveWeightSettingDialog(Context context, short m) {
        super(context, R.style.Dialog_Microwave_professtion);
        this.cx = context;
        this.model = m;
        init();
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_microwave_weightsetting;
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
                weight_img.setImageResource(R.mipmap.ic_device_microwave_normal_meat);weight_titletext.setText("牛肉");
                weight_weightwheel.setUnit("g");
                break;
            case MicroWaveModel.Checken:
                list = MicrowaveUtils.createWeightList(Short.valueOf("600"), Short.valueOf("1200"),
                        Short.valueOf("100"));
                weight_img.setImageResource(R.mipmap.ic_device_microwave_normal_checken);weight_titletext.setText("烤全鸡");
                weight_weightwheel.setUnit("g");
                break;
            case MicroWaveModel.Kebab:
                list = MicrowaveUtils.createWeightList(Short.valueOf("100"), Short.valueOf("400"),
                        Short.valueOf("100"));
                weight_img.setImageResource(R.mipmap.ic_device_microwave_normal_kebab);weight_titletext.setText("肉串");
                weight_weightwheel.setUnit("g");
                break;
            case MicroWaveModel.Rice:
                list = MicrowaveUtils.createWeightList(Short.valueOf("200"), Short.valueOf("600"),
                        Short.valueOf("100"));
                weight_img.setImageResource(R.mipmap.ic_device_microwave_normal_rice);weight_titletext.setText("煮饭");
                weight_weightwheel.setUnit("g");
                break;
            case MicroWaveModel.Porridge:
                list = MicrowaveUtils.createWeightList(Short.valueOf("50"), Short.valueOf("200"),
                        Short.valueOf("50"));
                weight_img.setImageResource(R.mipmap.ic_device_microwave_normal_porridge);weight_titletext.setText("煮粥");
                weight_weightwheel.setUnit("g");
                break;
            case MicroWaveModel.Vegetables:
                list = MicrowaveUtils.createWeightList(Short.valueOf("100"), Short.valueOf("500"),
                        Short.valueOf("100"));
                weight_img.setImageResource(R.mipmap.ic_device_microwave_normal_vegetables);weight_titletext.setText("蔬菜");
                weight_weightwheel.setUnit("g");
                break;
            case MicroWaveModel.Fish:
                list = MicrowaveUtils.createWeightList(Short.valueOf("100"), Short.valueOf("400"),
                        Short.valueOf("100"));
                weight_img.setImageResource(R.mipmap.ic_device_microwave_normal_fish);weight_titletext.setText("鱼");
                weight_weightwheel.setUnit("g");
                break;
            case MicroWaveModel.Milk:
                list = MicrowaveUtils.createWeightList(Short.valueOf("1"), Short.valueOf("2"),
                        Short.valueOf("1"));
                weight_img.setImageResource(R.mipmap.ic_device_microwave_normal_baotang);weight_titletext.setText("煲汤");
                weight_weightwheel.setUnit("份");
                break;
            case MicroWaveModel.Bread:
                list = MicrowaveUtils.createWeightList(Short.valueOf("1"), Short.valueOf("2"),
                        Short.valueOf("1"));
                weight_img.setImageResource(R.mipmap.ic_device_microwave_normal_bread);weight_titletext.setText("面包");
                weight_weightwheel.setUnit("份");
                break;
            case MicroWaveModel.Unfreeze://解冻
                list = MicrowaveUtils.createWeightList(Short.valueOf("100"), Short.valueOf("3000"),
                        Short.valueOf("100"));
                weight_img.setImageResource(R.mipmap.ic_device_microwave_normal_unfreeze);weight_titletext.setText("解冻");
                weight_weightwheel.setUnit("g");
                dialog_microwave_weightsetting_bottomtext.setText("* 此功能比较适合冰箱冷藏室中的冷饭或冷菜再加热。");
                break;
            case MicroWaveModel.HeatingAgain://再加热
                list = MicrowaveUtils.createWeightList(Short.valueOf("200"), Short.valueOf("600"),
                        Short.valueOf("100"));
                weight_img.setImageResource(R.mipmap.ic_device_microwave_normal_heatingagain);weight_titletext.setText("再加热");
                weight_weightwheel.setUnit("g");
                dialog_microwave_weightsetting_bottomtext.setText("* 适宜加热各种冷冻食材，微波中途翻转食材，效果会更好。");
                break;
            default:
                break;
        }
        return list;
    }

    @OnClick(R.id.weight_txtConfirm)//确认
    public void onClickConfirm() {
        if (listener != null && model >= 0)
            listener.onConfirm(Short.valueOf(String.valueOf(weight_weightwheel.getSelectedTag())));
        if(dlg!=null&&dlg.isShowing())
            dlg.dismiss();
    }

    static public MicrowaveWeightSettingDialog show(Context context, short model) {
        dlg = new MicrowaveWeightSettingDialog(context, model);
        Window win = dlg.getWindow();
        win.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
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

    private PickListener listener;

    public void setPickListener(PickListener listener) {
        Log.i("mic", "setPickListener");
        this.listener = listener;
    }

}
