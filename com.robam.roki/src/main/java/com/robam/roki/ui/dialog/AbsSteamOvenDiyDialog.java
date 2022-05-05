package com.robam.roki.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceOvenDiyParams;
import com.robam.roki.ui.adapter.DiyParamAdapter;
import com.robam.roki.ui.adapter.TimeAdapter;
import com.robam.roki.ui.wheel.WheelView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AbsSteamOvenDiyDialog extends AbsDialog {
    @InjectView(R.id.cancel)
    TextView cancel;
    @InjectView(R.id.confirm)
    TextView confirm;
    @InjectView(R.id.wv1)
    WheelView wv1;
    @InjectView(R.id.wv2)
    WheelView wv2;
    @InjectView(R.id.wv3)
    WheelView wv3;
    @InjectView(R.id.wv4)
    WheelView wv4;
    @InjectView(R.id.ll_roast_text)
    LinearLayout llRoastText;
    @InjectView(R.id.desc_layout)
    LinearLayout descLayout;
    @InjectView(R.id.text_desc)
    TextView tvDesc;
    List<DeviceOvenDiyParams> list;
    private List<Integer> upTemp;
    private List<Integer> downTemp;
    private List<Integer> temps;
    private List<Integer> times;
    private List<String> listButton;
    int indexSelectMode;
    int indexSelectTemp;
    int indexSelectUpTemp;
    int indexSelectDownTemp;
    int indexSelectTime;
    private String defaultTemp;
    private String defaultUpTemp;
    private String defaultDownTemp;
    private String defaultMinute;
    private int defaultIndex = -1;


    @Override
    protected int getViewResId() {
        return R.layout.abs_module_diy_dialog;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    public AbsSteamOvenDiyDialog(Context context, List<DeviceOvenDiyParams> list, List<String> listButton) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.list = list;
        this.listButton = listButton;
        init();
    }

    public AbsSteamOvenDiyDialog(Context context, List<DeviceOvenDiyParams> list, List<String> listButton, int index) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.list = list;
        this.listButton = listButton;
        defaultIndex = index;
        init();
    }


    protected void init() {
        llRoastText.setVisibility(View.GONE);
        DeviceOvenDiyParams deviceOvenDiyParams = list.get(0);
        temps = deviceOvenDiyParams.getTempList();
        times = deviceOvenDiyParams.getTimeList();
        upTemp = deviceOvenDiyParams.getUpTemp();
        downTemp = deviceOvenDiyParams.getDownTemp();
        defaultTemp = deviceOvenDiyParams.getDefaultTemp();
        defaultUpTemp = deviceOvenDiyParams.getUpTempDefault();
        defaultDownTemp = deviceOvenDiyParams.getUpTempDefault();
        defaultMinute = deviceOvenDiyParams.getDefaultMinute();
//        if (upTemp != null) {
//            for (int i = 0; i < upTemp.size(); i++) {
//                if (String.valueOf(upTemp.get(i)).equals(defaultUpTemp)) {
//                    wv2.setDefaultPosition(i);
//                    indexSelectUpTemp = i;
//                }
//            }
//            for (int i = 0; i < downTemp.size(); i++) {
//                if (String.valueOf(downTemp.get(i)).equals(defaultDownTemp)) {
//                    wv4.setDefaultPosition(i);
//                    indexSelectDownTemp = i;
//                }
//            }
//            for (int i = 0; i < times.size(); i++) {
//                if (String.valueOf(times.get(i)).equals(defaultMinute)) {
//                    wv3.setDefaultPosition(i);
//                    indexSelectTime = i;
//                }
//            }
//            wv2.setAdapter(new TimeAdapter(upTemp, listButton.get(1)));
//            wv4.setAdapter(new TimeAdapter(downTemp, listButton.get(1)));
//        } else {
            for (int i = 0; i < temps.size(); i++) {
                if (String.valueOf(temps.get(i)).equals(defaultTemp)) {
                    wv2.setDefaultPosition(i);
                    indexSelectTemp = i;
                }
            }
            for (int i = 0; i < times.size(); i++) {
                if (String.valueOf(times.get(i)).equals(defaultMinute)) {
                    wv3.setDefaultPosition(i);
                    indexSelectTime = i;
                }
            }
            wv2.setAdapter(new TimeAdapter(temps, listButton.get(1)));
//        }
        wv1.setDefaultPosition(0);
        if (defaultIndex != -1) {
            wv1.setDefaultPosition(defaultIndex);
            wv1.setVisibility(View.GONE);
        }
        wv1.setAdapter(new DiyParamAdapter(list, listButton.get(0)));
        wv3.setAdapter(new TimeAdapter(times, listButton.get(2)));
        wv1.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectMode = index;
                defaultTemp = list.get(index).getDefaultTemp();
                defaultUpTemp = list.get(index).getUpTempDefault();
                defaultDownTemp = list.get(index).getDownTempDefault();
                defaultMinute = list.get(index).getDefaultMinute();
                temps = list.get(index).getTempList();
                times = list.get(index).getTimeList();
                upTemp = list.get(index).getUpTemp();
                downTemp = list.get(index).getDownTemp();
                if (upTemp != null) {
                    for (int i = 0; i < upTemp.size(); i++) {
                        if (String.valueOf(upTemp.get(i)).equals(defaultUpTemp)) {
                            wv2.setDefaultPosition(i);
                            indexSelectUpTemp = i;
                            wv2.setAdapter(new TimeAdapter(upTemp, listButton.get(1)));
                        }
                    }
                    for (int i = 0; i < downTemp.size(); i++) {
                        if (String.valueOf(downTemp.get(i)).equals(defaultDownTemp)) {
                            wv4.setDefaultPosition(i);
                            indexSelectDownTemp = i;
                            wv4.setAdapter(new TimeAdapter(downTemp, listButton.get(1)));
                            wv4.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    for (int i = 0; i < temps.size(); i++) {
                        if (String.valueOf(temps.get(i)).equals(defaultTemp)) {
                            wv2.setDefaultPosition(i);
                            indexSelectTemp = i;
                            wv2.setAdapter(new TimeAdapter(temps, listButton.get(1)));
                            wv4.setVisibility(View.GONE);
                        }
                    }
                }
                for (int i = 0; i < times.size(); i++) {
                    if (String.valueOf(times.get(i)).equals(defaultMinute)) {
                        wv3.setDefaultPosition(i);
                        indexSelectTime = i;
                        wv3.setAdapter(new TimeAdapter(times, listButton.get(2)));
                    }
                }

            }
        });

        wv2.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTemp = index;
                indexSelectUpTemp = index;
            }
        });
        wv3.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTime = index;
            }
        });
        wv4.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectDownTemp = index;
            }
        });

    }

    public interface PickListener {
        void onCancel();

        void onConfirm(DeviceOvenDiyParams deviceOvenDiyParams, Integer integer1, Integer integer2, Integer integer3);

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
                    if (TextUtils.equals("EXP", list.get(indexSelectMode).getValue())) {
                        listener.onConfirm(list.get(indexSelectMode), upTemp.get(indexSelectUpTemp), times.get(indexSelectTime), downTemp.get(indexSelectDownTemp));
                    } else {
                        listener.onConfirm(list.get(indexSelectMode), temps.get(indexSelectTemp), times.get(indexSelectTime), -1);
                    }

                }
                this.dismiss();
                break;
        }
    }

    public void showDiyDialog(AbsSteamOvenDiyDialog diyDialog) {
        Window win = diyDialog.getWindow();
        win.setGravity(Gravity.BOTTOM);
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = displayMetrics.widthPixels;
        wl.height = (int) (displayMetrics.heightPixels * 0.45);
        win.setAttributes(wl);
        diyDialog.show();
        diyDialog.setCanceledOnTouchOutside(true);
    }
}
