package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;
import com.robam.roki.model.bean.RikaFunctionParams;
import com.robam.roki.ui.adapter.TimeAdapter;
import com.robam.roki.ui.wheel.WheelView;
import com.robam.roki.utils.TestDatas;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RikaMultiStepDialog extends AbsDialog {
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
    @InjectView(R.id.wv5)
    WheelView wv5;
    @InjectView(R.id.wv6)
    WheelView wv6;
    @InjectView(R.id.text_mode1)
    TextView mode1;
    @InjectView(R.id.text_mode2)
    TextView mode2;

    private ArrayList<RikaFunctionParams.MultiParams> mData;
    int indexSelectMode1;
    int indexSelectTemp1;
    int indexSelectTime1;
    int indexSelectMode2;
    int indexSelectTemp2;
    int indexSelectTime2;

    private PickListener listener;

    public void setListener(PickListener listener) {
        this.listener = listener;
    }

    public interface PickListener {
        void onCancel();

        void onConfirm(ArrayList<Integer> oneList, ArrayList<Integer> twoList);
    }

    public RikaMultiStepDialog(Context context, ArrayList<RikaFunctionParams.MultiParams> data) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.mData = data;
        initData();
    }

    public void showDialog(RikaMultiStepDialog multiDialog) {
        Window win = multiDialog.getWindow();
        win.setGravity(Gravity.BOTTOM);
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = displayMetrics.widthPixels;
        wl.height = (int) (displayMetrics.heightPixels * 0.66);
        win.setAttributes(wl);
        multiDialog.show();
        multiDialog.setCanceledOnTouchOutside(true);
    }

    private void initData() {
        List<Integer> tempList = TestDatas.createModeDataTemp(mData.get(0).tempList);
        List<Integer> timeList = TestDatas.createModeDataTime(mData.get(0).timeList);
        int timeCount = 1;
        if (mData.get(0).tempList.size() > 2) {
            timeCount =mData.get(0).tempList.get(2);
        }
        //拿到时间温度的索引值
        int indexTemp = mData.get(0).defaultTemp - tempList.get(0);
        int indexTime = mData.get(0).defaultTime - timeList.get(0);
        indexTime = indexTime / timeCount;
        List<String> modeNameList = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            String modeName = mData.get(i).modeName;
            modeNameList.add(modeName);
        }
        indexSelectMode1 = 1;
        indexSelectMode2 = 2;
        indexSelectTemp1 = indexTemp;
        indexSelectTemp2 = indexTemp;
        indexSelectTime1 = indexTime;
        indexSelectTime2 = indexTime;
        wv1.setDefaultPosition(indexSelectMode1);
        wv4.setDefaultPosition(indexSelectMode2);
        wv2.setDefaultPosition(indexTemp);
        wv5.setDefaultPosition(indexTemp);
        wv3.setDefaultPosition(indexTime);
        wv6.setDefaultPosition(indexTime);
        wv1.setAdapter(new TimeAdapter<String>(modeNameList, null));
        wv2.setAdapter(new TimeAdapter<Integer>(tempList, " ℃"));
        wv3.setAdapter(new TimeAdapter<Integer>(timeList, " 分钟"));
        wv4.setAdapter(new TimeAdapter<String>(modeNameList, null));
        wv5.setAdapter(new TimeAdapter<Integer>(tempList, " ℃"));
        wv6.setAdapter(new TimeAdapter<Integer>(timeList, " 分钟"));
        wv1.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectMode1 = index;
                setSelectedEvent(index, true);
            }
        });
        wv4.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectMode2 = index;
                setSelectedEvent(index, false);
            }
        });
        wv2.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTemp1 = index;
            }
        });
        wv3.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTime1 = index;
            }
        });
        wv5.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTemp2 = index;
            }
        });
        wv6.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTime2 = index;
            }
        });
        setSelectedEvent(1, true);
        setSelectedEvent(2, false);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_riki_multi_step;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
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
                    ArrayList<Integer> oneList = new ArrayList<>();
                    ArrayList<Integer> twoList = new ArrayList<>();
                    oneList.add(indexSelectMode1);
                    oneList.add(indexSelectTemp1);
                    oneList.add(indexSelectTime1);
                    twoList.add(indexSelectMode2);
                    twoList.add(indexSelectTemp2);
                    twoList.add(indexSelectTime2);
                    listener.onConfirm(oneList, twoList);
                }
                this.dismiss();
                break;
        }
    }

    private void setSelectedEvent(int index, boolean up) {
        List<Integer> tempList = TestDatas.createModeDataTemp(mData.get(index).tempList);
        List<Integer> timeList = TestDatas.createModeDataTime(mData.get(index).timeList);
        int timeCount = 1;
        if (mData.get(index).tempList.size() > 2) {
            timeCount =mData.get(index).tempList.get(2);
        }
        int tempCount = 1;
        if (mData.get(index).timeList.size() > 2) {
            tempCount =mData.get(index).timeList.get(2);
        }
        //拿到时间温度的索引值
        int indexTemp = mData.get(index).defaultTemp - tempList.get(0);
        int indexTime = mData.get(index).defaultTime - timeList.get(0);
        indexTime = indexTime / timeCount;
        indexTemp = indexTemp / tempCount;
        if (up) {
            indexSelectTemp1 = indexTemp;
            indexSelectTime1 = indexTime;
            wv2.setDefaultPosition(indexTemp);
            wv3.setDefaultPosition(indexTime);
            wv2.setAdapter(new TimeAdapter<Integer>(tempList, " ℃"));
            wv3.setAdapter(new TimeAdapter<Integer>(timeList, " 分钟"));
        } else {
            indexSelectTemp2 = indexTemp;
            indexSelectTime2 = indexTime;
            wv5.setDefaultPosition(indexTemp);
            wv6.setDefaultPosition(indexTime);
            wv5.setAdapter(new TimeAdapter<Integer>(tempList, " ℃"));
            wv6.setAdapter(new TimeAdapter<Integer>(timeList, " 分钟"));
        }
    }
}
