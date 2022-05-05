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
import com.robam.roki.R;
import com.robam.roki.ui.view.DeviceNumWheel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/13.
 */
public class MicrowaveTimeSettingDialog extends AbsDialog {
    @InjectView(R.id.weight_weightwheel)
    DeviceNumWheel weight_weightwheel;//火力滚动条
    @InjectView(R.id.weight_titletext)
    TextView weight_titletext;//文字
    @InjectView(R.id.weight_img)
    ImageView weight_img;//图标
    public static MicrowaveTimeSettingDialog dlg;
    Context cx;
    private View customView;

    public MicrowaveTimeSettingDialog(Context context) {
        super(context, R.style.Dialog_Microwave_professtion);
        this.cx = context;
        init();
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_microwave_weightsetting;
    }

    @Override
    protected void initView(View view) {
        this.customView = view;
    }

    private void init() {
        ButterKnife.inject(this, customView);
        weight_weightwheel.setData(generateModelWheelData());
        weight_weightwheel.setDefault(0);//设置重量默认值
        weight_weightwheel.setUnit("min");
        weight_img.setImageResource(R.mipmap.ic_device_microwave_normal_heatingic_yellow);
        weight_titletext.setText("快速加热");
    }

    private List<Integer> generateModelWheelData() {
        List<Integer> list = Lists.newArrayList();
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
        return list;
    }

    @OnClick(R.id.weight_txtConfirm)//确认
    public void onClickConfirm() {
        if (listener != null)
            listener.onConfirm(Short.valueOf(String.valueOf(weight_weightwheel.getSelectedTag())));
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    static public MicrowaveTimeSettingDialog show(Context context) {
        dlg = new MicrowaveTimeSettingDialog(context);
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
