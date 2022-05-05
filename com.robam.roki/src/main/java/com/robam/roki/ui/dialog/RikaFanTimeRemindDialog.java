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
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.adapter.TimeAdapter;
import com.robam.roki.ui.wheel.WheelView;
import com.robam.roki.utils.SteamOvenDatas;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/7/20.
 */

public class RikaFanTimeRemindDialog extends AbsDialog {

    @InjectView(R.id.cannel)
    TextView cannel;
    @InjectView(R.id.confirm)
    TextView confirm;
    @InjectView(R.id.wv1)
    WheelView wv1;
    @InjectView(R.id.wv3)
    WheelView wv3;
    String hourContent = "小时";
    String minContent = "分钟";
    List<Integer> hours;
    List<Integer> listData;
    List<Integer> minutes;
    int hourDefault;
    int minuteDefault;
    int indexSelectHour;
    int indexSelectMinute;

    public RikaFanTimeRemindDialog(Context context, List<Integer> hours, List<Integer> minutes,
                                   int hourDefault, int minuteDefault, List<Integer> listData) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.hours = hours;
        this.minutes = minutes;
        this.listData = listData;
        this.hourDefault = hourDefault;
        this.minuteDefault = minuteDefault;
        init();
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    @Override
    protected int getViewResId() {
        return R.layout.rika_time_remind_dialog;
    }

    private void init() {

        wv1.setDefaultPosition(hourDefault);
        wv3.setDefaultPosition(minuteDefault - 1);
        indexSelectHour = hourDefault;
        indexSelectMinute = minuteDefault - 1;
        wv1.setAdapter(new TimeAdapter<Integer>(hours, hourContent));
//        wv3.setAdapter(new TimeAdapter<Integer>(HelperRikaData.getListData(minutes), minContent));
        wv3.setAdapter(new TimeAdapter<Integer>(listData, minContent));
        wv1.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                LogUtils.i("20181119", "1index:" + index);
                indexSelectHour = index;
                if (index == 0) {
                    List<Integer> listData = HelperRikaData.getListRemoveData(minutes);
                    if (listData.get(0) == 1) {
                        wv3.setDefaultPosition(0);
                    } else {
                        wv3.setDefaultPosition(1);
                    }
                    wv3.setAdapter(new TimeAdapter<Integer>(listData, minContent));
                } else {
                    indexSelectMinute = 0;
                    wv3.setDefaultPosition(0);
                    wv3.setAdapter(new TimeAdapter<Integer>(HelperRikaData.getListData(minutes), minContent));
                }
            }
        });
        wv3.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                LogUtils.i("20181119", "3index:" + index);
                indexSelectMinute = index;

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
                    if (indexSelectHour == 0) {
                        listener.onConfirm(hours.get(indexSelectHour), listData.get(indexSelectMinute));
                    } else {
                        listener.onConfirm(hours.get(indexSelectHour), HelperRikaData.getListData(minutes).get(indexSelectMinute));
                    }
                }
                this.dismiss();
                break;
        }
    }

    public void show(RikaFanTimeRemindDialog absSetting) {
        Window win = absSetting.getWindow();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        WindowManager.LayoutParams wl = win.getAttributes();
//         wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.width = displayMetrics.widthPixels;
        wl.height = (int) (displayMetrics.heightPixels * 0.45);
        win.setAttributes(wl);
        absSetting.show();
        absSetting.setCanceledOnTouchOutside(true);
    }

}
