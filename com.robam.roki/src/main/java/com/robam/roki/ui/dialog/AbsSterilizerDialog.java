package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.ui.adapter.TimeAdapter;
import com.robam.roki.ui.wheel.WheelView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by RuanWei on 2019/11/20.
 */

public class AbsSterilizerDialog extends AbsDialog {

    @InjectView(R.id.cannel)
    TextView cancel;
    @InjectView(R.id.confirm)
    TextView confirm;
    @InjectView(R.id.wv1)
    WheelView wv1;
    @InjectView(R.id.wv2)
    WheelView wv2;
    @InjectView(R.id.text_desc)
    TextView textDesc;
    List<Integer> hours;
    List<Integer> minutes;
    int defaultHour;
    int defaultMinute;
    List<String> buttonList;

    String str1;
    String str2;
    String desc="";


    int indexSelectHour;
    int indexSelectMinute;


    public AbsSterilizerDialog(Context context, List<Integer> hours, List<Integer> minutes,
                               List<String> liststr, int defaultHour, int defaultMinute) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.hours = hours;
        this.minutes = minutes;
        this.defaultHour = defaultHour;
        this.defaultMinute = defaultMinute;
        this.buttonList = liststr;
        LogUtils.i("20191120", buttonList.toString());
        str1 = liststr.get(0);
        str2 = liststr.get(1);
        init();

    }
    public AbsSterilizerDialog(Context context, List<Integer> hours, List<Integer> minutes,
                               List<String> liststr, int defaultHour, int defaultMinute,String desc) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.hours = hours;
        this.minutes = minutes;
        this.defaultHour = defaultHour;
        this.defaultMinute = defaultMinute;
        this.buttonList = liststr;
        this.desc = desc;
        LogUtils.i("20191120", buttonList.toString());
        str1 = liststr.get(0);
        str2 = liststr.get(1);
        init();

    }


    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    protected void init() {
        textDesc.setText(desc);
        cancel.setText(buttonList.get(2));
        confirm.setText(buttonList.get(3));
        wv1.setDefaultPosition(defaultHour);
        for (int i = 0; i < minutes.size(); i++) {
            if (minutes.get(i).equals(defaultMinute)) {
                indexSelectMinute = i;
            }
        }
        wv2.setDefaultPosition(indexSelectMinute);

        indexSelectHour = defaultHour;

        wv1.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectHour = index;
                LogUtils.i("20191125666","indexSelectHour::::"+indexSelectHour);
                //如果小时==0 分钟要拨到1的位置（10分钟）
                if (index == 0) {
                    wv2.setDefaultPosition(1);
                    wv2.setAdapter(new TimeAdapter<Integer>(minutes, str2));
                    indexSelectMinute = 1;
                }

            }
        });
        wv1.setAdapter(new TimeAdapter<Integer>(hours, str1));
        LogUtils.i("20191120777", "str2:::" + str2);

        wv2.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectMinute = index;
                LogUtils.i("20191125666","indexSelectMinute::::"+indexSelectMinute);
                //如果分钟==0 判断下小时如果==0 就让小时拨到1的位置（1小时）
                if (index==0&&indexSelectHour==0) {
                    wv1.setDefaultPosition(1);
                    wv1.setAdapter(new TimeAdapter<>(hours, str1));
                    indexSelectHour=1;
                }

            }
        });
        wv2.setAdapter(new TimeAdapter<Integer>(minutes, str2));

    }

    private PickListener listener;

    public void setListener(PickListener listener) {
        this.listener = listener;
    }


    @OnClick({R.id.cannel, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cannel:
                if (this != null && this.isShowing()) {
                    this.dismiss();
                    listener.onCancel();
                }
                break;
            case R.id.confirm:
                if (listener != null) {
                    listener.onConfirm(hours.get(indexSelectHour), minutes.get(indexSelectMinute));
                }
                this.dismiss();
                break;
        }
    }

    public interface PickListener {
        void onCancel();

        void onConfirm(int index1, int index2);
    }


    @Override
    protected int getViewResId() {
        return R.layout.time_temp_dialog;
    }


    public void show(AbsSterilizerDialog absSetting) {
        Window win = absSetting.getWindow();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = displayMetrics.widthPixels;
        wl.height = (int) (displayMetrics.heightPixels * 0.45);
        win.setAttributes(wl);
        absSetting.show();
        absSetting.setCanceledOnTouchOutside(true);
    }
}
