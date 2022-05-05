package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.google.common.collect.Lists;
import com.legent.ui.ext.views.WheelView;
import com.robam.roki.R;
import com.robam.roki.model.DeviceWorkMsg;

import java.util.List;

/**
 * Created by Rosicky on 15/12/12.
 */
public class SteamovenResetWheelView extends FrameLayout{

    private List<Integer> list = Lists.newArrayList();
    private List<Integer> list1 = Lists.newArrayList();
    private WheelView wv1, wv2;

    public SteamovenResetWheelView(Context cx, String index) {
        super(cx);
        init(cx, null, index);
    }

    public SteamovenResetWheelView(Context context, AttributeSet attrs, String index) {
        super(context, attrs);
        init(context, attrs, index);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        loadData();
    }

    private void loadData() {
        wv1.setData(list);
        wv2.setData(list1);
    }

    private void init(Context cx, AttributeSet attrs, String s) {
        if (s.equals("蔬菜")) {
            for (int i = 95; i <= 100; i++) {
                list.add(i);
            }
            for (int i = 5; i <= 45; i++) {
                list1.add(i);
            }
        } else if (s.equals("水蒸蛋")) {
            for (int i = 85; i <= 95; i++) {
                list.add(i);
            }
            for (int i = 5; i <= 45; i++) {
                list1.add(i);
            }
        } else if (s.equals("肉类")) {
            for (int i = 85; i <= 100; i++) {
                list.add(i);
            }
            for (int i = 5; i <= 60; i++) {
                list1.add(i);
            }
        } else if (s.equals("海鲜")) {
            for (int i = 75; i <= 95; i++) {
                list.add(i);
            }
            for (int i = 5; i <= 45; i++) {
                list1.add(i);
            }
        } else if (s.equals("糕点")) {
            for (int i = 85; i <= 95; i++) {
                list.add(i);
            }
            for (int i = 5; i <= 45; i++) {
                list1.add(i);
            }
        } else if (s.equals("面条")) {
            for (int i = 85; i <= 95; i++) {
                list.add(i);
            }
            for (int i = 5; i <= 45; i++) {
                list1.add(i);
            }
        } else if (s.equals("蹄筋")) {
            for (int i = 90; i <= 100; i++) {
                list.add(i);
            }
            for (int i = 5; i <= 60; i++) {
                list1.add(i);
            }
        } else if (s.equals("解冻")) {
            for (int i = 55; i <= 65; i++) {
                list.add(i);
            }
            for (int i = 5; i <= 60; i++) {
                list1.add(i);
            }
        }
        LayoutInflater.from(cx).inflate(R.layout.steamoven_view_two_wheel, this,
                true);

        wv1 = findViewById(R.id.wv1);
        wv2 = findViewById(R.id.wv2);

    }

    public DeviceWorkMsg getSelected() {
        DeviceWorkMsg msg = new DeviceWorkMsg();
        try {
            msg.setTemperature(wv1.getSelectedText());
            msg.setTime(wv2.getSelectedText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

}
