package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceOvenDiyParams;
import com.robam.roki.ui.adapter.DiyParamAdapter;
import com.robam.roki.ui.adapter.TimeAdapter;
import com.robam.roki.ui.wheel.WheelView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/9/11.
 */

public class AbsOvenDIYDialog extends AbsDialog {
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

    @InjectView(R.id.rg_roaste)
    RadioGroup rgRoaste;

    @InjectView(R.id.rb_switch_open)
    RadioButton rbSwitchOpen;

    @InjectView(R.id.rb_switch_close)
    RadioButton rbSwitchClose;

    @InjectView(R.id.text_desc)
    TextView textDesc;

    List<DeviceOvenDiyParams> modeList;
    List<Integer> keyList;
    int default1;
    int default2;
    int default3;

    List<String> buttonList;


    String str1;//单位1
    String str2;//单位2
    String str3;//单位3

    int indexSelectMode;
    int indexSelectTemp;
    int indexSelectTime;
    private List<Integer> tempList;
    private List<Integer> timeList;


    public AbsOvenDIYDialog(
            Context context,
            List<DeviceOvenDiyParams> modeList,
            List<Integer> keyList,
            List<String> liststr
            ) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.modeList = modeList;
        this.keyList = keyList;

        this.buttonList = liststr;
        initDiy();
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    protected void initDiy() {
        str1 = buttonList.get(0);//""
        str2 = buttonList.get(1);//℃
        str3 = buttonList.get(2);//min
        wv1.setDefaultPosition(default1);

        String defaultTemp = modeList.get(0).getDefaultTemp();
        String defaultMinute = modeList.get(0).getDefaultMinute();
        tempList = modeList.get(0).getTempList();
        timeList = modeList.get(0).getTimeList();
        for (int j = 0; j < tempList.size(); j++) {
            if ((tempList.get(j).toString()).equals(defaultTemp)) {
                default2 = j;
                wv2.setDefaultPosition(j);
            }
        }

        for (int j = 0; j < timeList.size(); j++) {
            if ((timeList.get(j).toString()).equals(defaultMinute)) {
                default3 = j;
                wv3.setDefaultPosition(j);
            }
        }

        indexSelectMode = default1;
        indexSelectTemp = default2;
        indexSelectTime = default3;
        wv1.setAdapter(new DiyParamAdapter(modeList, str1));
        wv2.setAdapter(new TimeAdapter<Integer>(tempList, str2));
        wv3.setAdapter(new TimeAdapter<Integer>(timeList, str3));
        String hasRotate = modeList.get(0).getHasRotate();
        if ("true".equals(hasRotate)) {
            rbSwitchOpen.setChecked(true);
        } else if ("false".equals(hasRotate)) {
            rbSwitchClose.setChecked(true);
        }

        wv1.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectMode = index;
                String hasRotate = modeList.get(index).getHasRotate();
                if ("true".equals(hasRotate)) {
                    rbSwitchOpen.setChecked(true);
                    rbSwitchClose.setChecked(false);
                } else if ("false".equals(hasRotate)) {
                    rbSwitchClose.setChecked(true);
                    rbSwitchOpen.setChecked(false);
                }
                String defaultMinute = modeList.get(index).getDefaultMinute();
                LogUtils.i("201912113", "defaultMinute::" + defaultMinute);
                String defaultTemp = modeList.get(index).getDefaultTemp();
                LogUtils.i("201912113", "defaultTemp::" + defaultTemp);

                List<Integer> tempList = modeList.get(index).getTempList();
                List<Integer> timeList = modeList.get(index).getTimeList();


                for (int i = 0; i < tempList.size(); i++) {
                    if ((tempList.get(i).toString()).equals(defaultTemp)) {
                        indexSelectTemp=i;
                        wv2.setDefaultPosition(i);
                        wv2.setAdapter(new TimeAdapter<Integer>(tempList, str2));
                    }
                }

                for (int i = 0; i < timeList.size(); i++) {
                    if ((timeList.get(i).toString()).equals(defaultMinute)) {
                        indexSelectTime=i;
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
            }
        });

        wv3.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTime = index;
            }
        });
        rbSwitchOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });


    }

    public interface PickListener {
        void onCancel();

        void onConfirm(DeviceOvenDiyParams deviceOvenDiyParams, Integer integer2, Integer integer,
                       Integer integer1, boolean isOpen, boolean isClose);
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

                    isOpen = rbSwitchOpen.isChecked();
                    isClose = rbSwitchClose.isChecked();
                    listener.onConfirm(modeList.get(indexSelectMode), keyList.get(indexSelectMode),
                            tempList.get(indexSelectTemp), timeList.get(indexSelectTime), isOpen, isClose);
                }
                this.dismiss();
                break;
        }
    }

    public void showDiyDialog(AbsOvenDIYDialog diyDialog) {
        Window win = diyDialog.getWindow();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
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
