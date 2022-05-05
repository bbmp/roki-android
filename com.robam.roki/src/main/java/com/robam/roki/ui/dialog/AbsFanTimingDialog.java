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

import com.google.common.collect.Lists;
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
 * Created by Dell on 2018/7/20.
 */

public class AbsFanTimingDialog extends AbsDialog {

    @InjectView(R.id.cancel)
    TextView cancel;
    @InjectView(R.id.confirm)
    TextView confirm;
    @InjectView(R.id.wv1)
    WheelView wv1;
    @InjectView(R.id.wv2)
    WheelView wv2;
    List<Integer> hour;
    List<Integer> minute;
    int default1;
    int default2;
    List<String> buttonList;
    String str1;//单位1
    String str2;//单位2
    int indexSelectHour;
    int indexSelectMinute;


    public AbsFanTimingDialog(Context context,
                              List<Integer> hour,
                              List<Integer> minute,
                              List<String> liststr,
                              int defalutHour,
                              int defalutMinute) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.hour = hour;
        this.minute = minute;
        this.buttonList = liststr;
        this.default1 = defalutHour;
        this.default2 = defalutMinute;
        init();
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    @Override
    protected int getViewResId() {
        return R.layout.abs_module_fan_timing_dialog;
    }

    int tag = -1;

    protected void init() {
        str1 = buttonList.get(0);
        str2 = buttonList.get(1);
        wv1.setDefaultPosition(default1);
        wv2.setDefaultPosition(default2);
        indexSelectHour = default1;
        indexSelectMinute = default2;
        wv1.setAdapter(new TimeAdapter<>(hour, str1));
        wv2.setAdapter(new TimeAdapter<>(minute, str2));
        wv1.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                LogUtils.i("20191113999", "index:" + index);
                indexSelectHour = index;
                //小时==0
                if (index == 0) {
                    tag = 1;

                    wv2.setDefaultPosition(0);
                    //更新分钟的数据源 1-59
                    wv2.setAdapter(new TimeAdapter<>(generateModelWheelData(59, 1), str2));
                    indexSelectMinute=1;

                } else {
                    tag = 2;

                    wv2.setDefaultPosition(0);
                    //小时!=0
                    //更新分钟的数据源
                    wv2.setAdapter(new TimeAdapter<>(generateModelWheelData(59, 0), str2));


                }

            }
        });
        wv2.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectMinute = index;

                //小时 WheelView 没有拨过
                if (tag == -1) {
                    indexSelectMinute = index;
                    if (index == 0) {
                        wv1.setDefaultPosition(1);
                        indexSelectHour=1;
                        wv1.setAdapter(new TimeAdapter<>(hour, str1));
                    }

                }else if (tag==1){
                    indexSelectMinute = index+1;

                }else if (tag==2){
                    indexSelectMinute = index;
                }


            }
        });


    }


    public interface PickListener {
        void onCancel();

        void onConfirm(int index1, int index2);
    }

    private PickListener listener;

    public void setListener(PickListener listener) {
        this.listener = listener;
    }

    @OnClick({R.id.cancel, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                if (this != null && this.isShowing()) {
                    this.dismiss();
                    listener.onCancel();
                }
                break;
            case R.id.confirm:
                if (listener != null) {
                    listener.onConfirm(hour.get(indexSelectHour), minute.get(indexSelectMinute));
                }
                this.dismiss();
                break;
        }
    }

    public void show(AbsFanTimingDialog absSetting) {
        Window win = getWindow();
        win.setGravity(Gravity.BOTTOM);
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


    private List<Integer> generateModelWheelData(int max, int start) {
        List<Integer> list = Lists.newArrayList();
        for (int i = start; i <= max; i++) {
            list.add(i);
        }
        return list;
    }

}
