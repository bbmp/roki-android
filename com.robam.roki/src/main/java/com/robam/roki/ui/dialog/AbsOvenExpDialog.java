package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.robam.roki.R;
import com.robam.roki.ui.adapter.TimeAdapter;
import com.robam.roki.ui.wheel.WheelView;
import com.robam.roki.utils.TestDatas;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/7/20.
 */

public class AbsOvenExpDialog extends AbsDialog {

    @InjectView(R.id.cannel)
    TextView cannel;
    @InjectView(R.id.confirm)
    TextView confirm;
    @InjectView(R.id.wv1)
    WheelView wv1;
    @InjectView(R.id.wv2)
    WheelView wv2;
    @InjectView(R.id.wv3)
    WheelView wv3;
    @InjectView(R.id.text_desc)
    TextView textDesc;

    List<Integer> tempUp;
    List<Integer> tempDown;
    List<Integer> time;
    int default1;
    int default2;
    int default3;

    List<String> buttonList;


    String str1;//单位1
    String str2;//单位2
    String str3;//单位3

    int indexSelectTempUp;
    int indexSelectTempDown;
    int indexSelectTime;

    int defaultValue;
    int stepC;
    int min;
    int max;

    public AbsOvenExpDialog(
            Context context,
            List<Integer> tempUp,
            List<Integer> tempDwon,
            List<Integer> time,
            List<String> liststr,
            int defaultTemp,
            int defaultdwon,
            int defaultTime,
            int stepC,
            int defaultValue,
            int min) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.tempUp = tempUp;
        this.time = time;
        this.stepC = stepC;
        this.buttonList = liststr;
        this.default1 = defaultTemp;
        this.default3 = defaultTime;
        this.defaultValue = defaultValue;
        this.min = min;
        if (tempUp.size() > 0) {
            this.max = tempUp.get(tempUp.size() - 1);
        }
        this.tempDown = TestDatas.createExpDownDatas(tempUp.get(default1), stepC, defaultValue, max, min);

        initExpDialog();
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    @Override
    protected int getViewResId() {
        return R.layout.abs_module_exp_dialog;
    }

    protected void initExpDialog() {
        str1 = buttonList.get(0);
        str2 = buttonList.get(1);
        str3 = buttonList.get(2);
        textDesc.setText(buttonList.get(3));
        wv1.setDefaultPosition(default1);
        wv3.setDefaultPosition(default3);
        indexSelectTempUp = default1;
        indexSelectTempDown = default2;
        indexSelectTime = default3;
        wv1.setAdapter(new TimeAdapter<Integer>(tempUp, str1));
        wv2.setAdapter(new TimeAdapter<Integer>(tempDown, str2));
        wv3.setAdapter(new TimeAdapter<Integer>(time, str3));
        wv1.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                LogUtils.i("2020062805", "index:" + index);
                LogUtils.i("2020062805", "2222:" +  tempUp.get(index));
                indexSelectTempUp = index;
                tempDown = TestDatas.createExpDownDatas(
                        tempUp.get(index),
                        stepC,
                        defaultValue,
                        max,
                        min);


                LogUtils.i("2020062805", "tempDown:" + tempDown);
                wv2.setAdapter(new TimeAdapter<Integer>(tempDown, str2));
            }
        });
        wv2.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTempDown = index;
            }
        });

        wv3.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTime = index;
            }
        });
    }


    public interface PickListener {
        void onCancel();

        void onConfirm(int index1, int index2, int index3);
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
                    LogUtils.i("20180720", "inup:" + indexSelectTempUp + " indown:" + indexSelectTempDown
                            + " intime::" + indexSelectTime);
                    listener.onConfirm(tempUp.get(indexSelectTempUp), tempDown.get(indexSelectTempDown), time.get(indexSelectTime));
                }
                this.dismiss();
                break;
        }
    }

    public void showDialog(AbsOvenExpDialog absSetting) {
        Window win = absSetting.getWindow();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        WindowManager.LayoutParams wl = win.getAttributes();
        // wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.width = displayMetrics.widthPixels;
        wl.height = (int) (displayMetrics.heightPixels * 0.45);
        win.setAttributes(wl);
        absSetting.show();
        absSetting.setCanceledOnTouchOutside(true);
    }

}
