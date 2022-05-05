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

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/7/12.
 */

public class AbsOvenModeSettingDialog extends AbsDialog {
    @InjectView(R.id.cannel)
    TextView cannel;
    @InjectView(R.id.confirm)
    TextView confirm;
    @InjectView(R.id.wv1)
    WheelView wv1;
    @InjectView(R.id.wv2)
    WheelView wv2;
    @InjectView(R.id.text_desc)
    TextView textDesc;

    List<Integer> temp;
    List<Integer> time;
    int defaultTemp;
    int defaultTime;
    List<String> buttonList;
    String str1;//单位1
    String str2;//单位2

    public AbsOvenModeSettingDialog(
                                    Context context,
                                    List<Integer> temp,
                                    List<Integer> time,
                                    List<String> liststr,
                                    int defaultTemp,
                                    int defaultTime) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.temp = temp;
        this.time = time;
        this.defaultTemp = defaultTemp;
        this.defaultTime = defaultTime;
        this.buttonList = liststr;
        str1 = liststr.get(0);
        str2 = liststr.get(1);
        initOvenModeSetting();
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    int indexSelectTemp;
    int indexSelectTime;

    public interface PickListener {
        void onCancel();

        void onConfirm(int index1,int index2);
    }

    private PickListener listener;

    public void setListener(PickListener listener) {
        this.listener = listener;
    }

    protected void initOvenModeSetting() {
        cannel.setText(buttonList.get(2));
        confirm.setText(buttonList.get(3));
        textDesc.setText(buttonList.get(4));
        wv1.setDefaultPosition(defaultTemp);
        wv2.setDefaultPosition(defaultTime);
        indexSelectTemp = defaultTemp;
        indexSelectTime = defaultTime;
        wv1.setAdapter(new TimeAdapter<Integer>(temp, str1));
        wv1.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                LogUtils.i("20180609","index:"+index);
                indexSelectTemp = index;
            }
        });
        wv2.setAdapter(new TimeAdapter<Integer>(time,str2));
        wv2.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTime = index;
            }
        });
    }

    @OnClick({R.id.cannel, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cannel:
                if (this!=null&&this.isShowing()){
                    this.dismiss();
                    listener.onCancel();
                }
                break;
            case R.id.confirm:
                if (listener!=null){
                    listener.onConfirm(temp.get(indexSelectTemp),time.get(indexSelectTime));
                }
                this.dismiss();
                break;
        }
    }

    @Override
    protected int getViewResId() {
        return R.layout.time_temp_dialog;
    }

    public void show(AbsOvenModeSettingDialog absSetting) {
        Window win = absSetting.getWindow();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        WindowManager.LayoutParams wl = win.getAttributes();
        // wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.width = displayMetrics.widthPixels;
        LogUtils.i("20190917", "set wl.width:" + wl.width);
        wl.height =(int) (displayMetrics.heightPixels * 0.45);
        win.setAttributes(wl);
        absSetting.show();
        absSetting.setCanceledOnTouchOutside(true);
    }
}
