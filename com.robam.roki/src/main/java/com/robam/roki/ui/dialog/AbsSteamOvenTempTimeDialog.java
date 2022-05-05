package com.robam.roki.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceOvenDiyParams;
import com.robam.roki.model.bean.SteamOvenBean;
import com.robam.roki.ui.adapter.TimeAdapter;
import com.robam.roki.ui.wheel.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AbsSteamOvenTempTimeDialog extends AbsDialog {
    @InjectView(R.id.cancel)
    TextView Cancel;
    @InjectView(R.id.confirm)
    TextView Confirm;

    @InjectView(R.id.wv1)
    WheelView wv1;
    @InjectView(R.id.wv2)
    WheelView wv2;
    @InjectView(R.id.wv3)
    WheelView wv3;
    @InjectView(R.id.wv4)
    WheelView wv4;

    @InjectView(R.id.rg_roaste)
    RadioGroup rgRoaste;

    @InjectView(R.id.rb_switch_open)
    RadioButton rbSwitchOpen;

    @InjectView(R.id.rb_switch_close)
    RadioButton rbSwitchClose;

    @InjectView(R.id.text_desc)
    TextView textDesc;

    @InjectView(R.id.tv_roast_title)
    TextView tvRoastTitle;


    List<DeviceOvenDiyParams> modeList;
    int default1;
    int default2;
    int default3;

    List<String> buttonList;


    String str1;//单位1
    String str2;//单位2
    String str3;//单位3

    int indexSelectUpTemp;
    int indexSelectDownTemp;
    int indexSelectMode;
    int indexSelectTemp;
    int indexSelectTime;
    private List<Integer> tempList;
    private List<Integer> timeList;
    private List<Integer> upTemp;
    private List<Integer> downTemp;
    private String defaultUpTemp;
    private String defaultDownTemp;


    public AbsSteamOvenTempTimeDialog(Context context, List<DeviceOvenDiyParams> modeList, List<String> str) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.modeList = modeList;
        this.buttonList = str;
        initDataSelectDialog();
    }

    public AbsSteamOvenTempTimeDialog(Context context, List<DeviceOvenDiyParams> modeList, List<String> str, int index) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.modeList = modeList;
        this.buttonList = str;
        default1 = index;
        initDataSelectDialog();
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    protected void initDataSelectDialog() {
        if (rgRoaste != null) {
            rgRoaste.setVisibility(View.GONE);
        }
        if (tvRoastTitle != null) {
            tvRoastTitle.setVisibility(View.GONE);
        }

        str1 = buttonList.get(0);//""
        str2 = buttonList.get(1);//℃
        str3 = buttonList.get(2);//min
        wv1.setDefaultPosition(default1);
        wv1.setVisibility(View.GONE);

        String defaultTemp = modeList.get(default1).getDefaultTemp();
        defaultUpTemp = modeList.get(default1).getUpTempDefault();
        defaultDownTemp = modeList.get(default1).getDownTempDefault();
        String defaultMinute = modeList.get(default1).getDefaultMinute();
        tempList = modeList.get(default1).getTempList();
        timeList = modeList.get(default1).getTimeList();
        upTemp = modeList.get(default1).getUpTemp();
        downTemp = modeList.get(default1).getDownTemp();
        if (upTemp != null) {
            for (int i = 0; i < upTemp.size(); i++) {
                if (String.valueOf(upTemp.get(i)).equals(defaultUpTemp)) {
                    wv2.setDefaultPosition(i);
                    indexSelectUpTemp = i;
                    wv2.setAdapter(new TimeAdapter(upTemp, buttonList.get(1)));
                }
            }
            for (int i = 0; i < downTemp.size(); i++) {
                if (String.valueOf(downTemp.get(i)).equals(defaultDownTemp)) {
                    wv4.setDefaultPosition(i);
                    indexSelectDownTemp = i;
                    wv4.setAdapter(new TimeAdapter(downTemp, buttonList.get(1)));
                    wv4.setVisibility(View.VISIBLE);
                }
            }
        } else {
            for (int j = 0; j < tempList.size(); j++) {
                if ((tempList.get(j).toString()).equals(defaultTemp)) {
                    default2 = j;
                    wv2.setDefaultPosition(j);
                    wv2.setAdapter(new TimeAdapter<Integer>(tempList, str2));
                    wv4.setVisibility(View.GONE);
                }
            }
        }

        for (int j = 0; j < timeList.size(); j++) {
            if ((timeList.get(j).toString()).equals(defaultMinute)) {
                default3 = j;
                wv3.setDefaultPosition(j);
            }
        }

        List<String> modeNameList = new ArrayList<>();
        for (int i = 0; i < modeList.size(); i++) {
            String modeName = modeList.get(i).getValue();
            modeNameList.add(modeName);
        }

        indexSelectMode = default1;
        indexSelectTemp = default2;
        indexSelectTime = default3;
        wv1.setAdapter(new TimeAdapter<String>(modeNameList, str1));
        wv3.setAdapter(new TimeAdapter<Integer>(timeList, str3));

        wv1.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectMode = index;
                rbSwitchOpen.setChecked(true);
                rbSwitchClose.setChecked(false);
                String defaultMinute = modeList.get(index).getDefaultMinute();
                String defaultTemp = modeList.get(index).getDefaultTemp();

                tempList = modeList.get(index).getTempList();
                timeList = modeList.get(index).getTimeList();


                for (int i = 0; i < tempList.size(); i++) {
                    if ((tempList.get(i).toString()).equals(defaultTemp)) {
                        indexSelectTemp = i;
                        indexSelectUpTemp = i;
                        wv2.setDefaultPosition(i);
                        wv2.setAdapter(new TimeAdapter<Integer>(tempList, str2));
                    }
                }

                for (int i = 0; i < timeList.size(); i++) {
                    if ((timeList.get(i).toString()).equals(defaultMinute)) {
                        indexSelectTime = i;
                        wv3.setDefaultPosition(i);
                        wv3.setAdapter(new TimeAdapter<Integer>(timeList, str3));
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

        void onConfirm(DeviceOvenDiyParams deviceOvenDiyParams, Integer integer2, Integer integer,
                       Integer integer1);
    }

    private PickListener listener;

    public void setListener(PickListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getViewResId() {
        return R.layout.abs_module_diy_dialog;
    }

    boolean isOpen;
    boolean isClose;

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

                    if (rbSwitchOpen.isChecked()) {
                        isOpen = true;
                    } else {
                        isOpen = false;
                    }
                    if (rbSwitchClose.isChecked()) {
                        isClose = true;
                    } else {
                        isClose = false;
                    }
                    if (TextUtils.equals(modeList.get(indexSelectMode).getValue(), "EXP")) {
                        listener.onConfirm(modeList.get(indexSelectMode), downTemp.get(indexSelectDownTemp),
                                upTemp.get(indexSelectUpTemp), timeList.get(indexSelectTime));
                    } else {
                        listener.onConfirm(modeList.get(indexSelectMode), -1,
                                tempList.get(indexSelectTemp), timeList.get(indexSelectTime));
                    }

                }
                this.dismiss();
                break;
        }
    }

    public void showDiyDialog(AbsSteamOvenTempTimeDialog diyDialog) {
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
