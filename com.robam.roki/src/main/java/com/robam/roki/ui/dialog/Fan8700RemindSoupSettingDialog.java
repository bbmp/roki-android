package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.FanDeviceRemindSoup;
import com.robam.roki.ui.view.DeviceFanNumWheel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/13.
 */
public class Fan8700RemindSoupSettingDialog extends AbsDialog {

    @InjectView(R.id.fan8700_remindsoupsetting_wheel_hour)//小时
    DeviceFanNumWheel fan8700_remindsoupsetting_wheel_hour;
    @InjectView(R.id.fan8700_remindsoupsetting_wheel_min)//分钟
     DeviceFanNumWheel fan8700_remindsoupsetting_wheel_min;
    @InjectView(R.id.fan8700_remindsoupsetting_tv_confirm)//确认
    TextView fan8700_remindsoupsetting_tv_confirm;
    public static Fan8700RemindSoupSettingDialog dlg;
    Context cx;
    @InjectView(R.id.tv_cancel)
    TextView tvCancel;
    private int res;
    private String title;
    private String tag;
    private boolean beforeZero = false;

    public Fan8700RemindSoupSettingDialog(Context context, String tag,
                                          int res, String title) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.cx = context;
        this.res = res;
        this.title = title;
        this.tag = tag;
        init();
    }

    private void init() {
        LogUtils.i("20180504", " tag:" + tag);

//        FanDeviceRemindSoup fanDeviceRemindSoup = DeviceJsonToBeanUtils.JsonToObject(tag, FanDeviceRemindSoup.class);
        try {
            FanDeviceRemindSoup fanDeviceRemindSoup = JsonUtils.json2Pojo(tag, FanDeviceRemindSoup.class);
            if (fanDeviceRemindSoup == null) return;
            List<Integer> hour = fanDeviceRemindSoup.getParam().getHour().getValue();
            final List<Integer> minute = fanDeviceRemindSoup.getParam().getMinute().getValue();
            int hourDefault = fanDeviceRemindSoup.getParam().getHourDefault().getValue();
            int minuteDefault = fanDeviceRemindSoup.getParam().getMinuteDefault().getValue();
            if (hour == null || minute == null) return;
            fan8700_remindsoupsetting_wheel_hour.setData(generateModelWheelData(hour.get(hour.size() - 1), hour.get(0)));
            fan8700_remindsoupsetting_wheel_hour.setDefault(hourDefault);
            fan8700_remindsoupsetting_wheel_hour.setUnit("小时");
            fan8700_remindsoupsetting_wheel_min.setData(generateModelWheelData(minute.get(1), minute.get(0) + 1));
            fan8700_remindsoupsetting_wheel_min.setDefault(minuteDefault - 1);

            fan8700_remindsoupsetting_wheel_min.setUnit("分钟");
            fan8700_remindsoupsetting_wheel_hour.setOnSelectListener(new DeviceFanNumWheel.OnSelectListener() {
                @Override
                public void endSelect(int index, Object item) {

                    if (index != 0 && fan8700_remindsoupsetting_wheel_min.getData().size() == minute.get(1)) {
                        fan8700_remindsoupsetting_wheel_min.setData(generateModelWheelData(minute.get(1), minute.get(0)));
                        fan8700_remindsoupsetting_wheel_min.setDefault(minute.get(0));
                        fan8700_remindsoupsetting_wheel_min.setUnit("分钟");
                    } else if (index == 0) {
                        fan8700_remindsoupsetting_wheel_min.setData(generateModelWheelData(59, 1));
                        fan8700_remindsoupsetting_wheel_min.setDefault(minute.get(0));
                        fan8700_remindsoupsetting_wheel_min.setUnit("分钟");
                    }
                }

                @Override
                public void selecting(int index, Object item) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Integer> generateModelWheelData(int max, int start) {
        List<Integer> list = Lists.newArrayList();
        for (int i = start; i <= max; i++) {
            list.add(i);
        }
        return list;
    }


    @Override
    protected int getViewResId() {
        return R.layout.dialog_fan8700_remindsoupsetting;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    @OnClick(R.id.fan8700_remindsoupsetting_tv_confirm)//确认
    public void onClickConfirm() {
        if (listener != null) {
            listener.onConfirm(Short.valueOf(String.valueOf(fan8700_remindsoupsetting_wheel_hour.getSelectedTag())),
                    Short.valueOf(String.valueOf(fan8700_remindsoupsetting_wheel_min.getSelectedTag())));
        }
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    static public Fan8700RemindSoupSettingDialog show(Context context, String tag,
                                                      int res, String title) {
        dlg = new Fan8700RemindSoupSettingDialog(context, tag, res, title);
        Window win = dlg.getWindow();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }

    @OnClick(R.id.tv_cancel)
    public void onViewClicked() {
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
        }

    }

    public interface PickListener {
        void onCancel();

        void onConfirm(short hour, short min);
    }

    private PickListener listener;

    public void setPickListener(PickListener listener) {
        Log.i("mic", "setPickListener");
        this.listener = listener;
    }

}
