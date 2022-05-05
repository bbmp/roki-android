package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.common.collect.Lists;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;
import com.robam.roki.ui.view.DeviceNumWheel;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rent on 2015/12/25.
 */
public class Oven026OrderTimeDialog extends AbsDialog {
    public static Oven026OrderTimeDialog dlg;
    Context cx;
    short model;//模式
    private View customView;
    PickListener callback;

    @InjectView(R.id.oven026_ordertime_hour)
    DeviceNumWheel oven026_ordertime_hour;
    @InjectView(R.id.oven026_ordertime_min)
    DeviceNumWheel oven026_ordertime_min;
    @InjectView(R.id.oven026_order_close)
    Button oven026_order_close;
    @InjectView(R.id.oven026_order_start)
    Button oven026_order_start;

    public Oven026OrderTimeDialog(Context context, PickListener callback) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.cx = context;
        this.callback = callback;
        init();
    }

    private void init() {
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_oven026_ordertime_setting;
    }


    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        ArrayList<Integer> list_hour = Lists.newArrayList();
        for (int i = 0; i < 24; i++) {
            list_hour.add(i);
        }
        oven026_ordertime_hour.setData(list_hour);
        oven026_ordertime_hour.setDefault(12);
        oven026_ordertime_hour.setUnit("时");
        ArrayList<Integer> list_min = Lists.newArrayList();
        for (int i = 0; i < 60; i++) {
            list_min.add(i);
        }
        oven026_ordertime_min.setData(list_min);
        oven026_ordertime_min.setDefault(30);
        oven026_ordertime_min.setUnit("分");
    }

    @OnClick(R.id.oven026_order_close)
    public void OnCloseClick() {
        if (Oven026OrderTimeDialog.dlg != null && Oven026OrderTimeDialog.dlg.isShowing()) {
            Oven026OrderTimeDialog.dlg.dismiss();
        }
    }

    @OnClick(R.id.oven026_order_start)
    public void OnStartClick() {
        if (callback != null) {
            callback.onConfirm((Integer) oven026_ordertime_hour.getSelectedTag(), (Integer) oven026_ordertime_min.getSelectedTag());
        }
        if (Oven026OrderTimeDialog.dlg != null && Oven026OrderTimeDialog.dlg.isShowing()) {
            Oven026OrderTimeDialog.dlg.dismiss();
        }
    }


    static public Oven026OrderTimeDialog show(Context context, PickListener callback) {
        dlg = new Oven026OrderTimeDialog(context, callback);
        Window win = dlg.getWindow();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }

    public interface PickListener {
        void onCancel();

        void onConfirm(Integer hour, Integer min);
    }

    private Oven026OrderTimeDialog.PickListener listener;

    public void setPickListener(Oven026OrderTimeDialog.PickListener listener) {
        this.listener = listener;
    }

}

