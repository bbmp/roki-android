package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.google.common.collect.Lists;
import com.robam.roki.R;
import com.robam.roki.model.NormalModeItemMsg;

import java.util.List;

/**
 * Created by linxiaobin on 2015/12/26.
 */
public class OvenResetWheelView extends FrameLayout {

    private List<Integer> list = Lists.newArrayList();
    private List<Integer> list1 = Lists.newArrayList();
    private WheelView1 wv1;
    private WheelView2 wv2;

    public OvenResetWheelView(Context cx, String index) {
        super(cx);
        init(cx, null, index);
    }

    public OvenResetWheelView(Context context, AttributeSet attrs, String index) {
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
        if (s.equals("焙烤")) {
            for (int i = 50; i <= 180; i++) {
                list.add(i);
            }
        } else if (s.equals("底加热")) {
            for (int i = 15; i <= 80; i++) {
                list.add(i);
            }
        } else if (s.equals("快热") || s.equals("风焙烤") || s.equals("解冻")
                || s.equals("风扇烤") || s.equals("烤烧") || s.equals("强烤烧")) {
            for (int i = 50; i <= 230; i++) {
                list.add(i);
            }
        }

        for (int i = 5; i <= 90; i++) {
            list1.add(i);
        }
        LayoutInflater.from(cx).inflate(R.layout.oven_reset_two_wheel_view, this,
                true);

        wv1 = findViewById(R.id.wv1);
        wv2 = findViewById(R.id.wv2);

    }

    public NormalModeItemMsg getSelected() {
        NormalModeItemMsg msg = new NormalModeItemMsg();
        msg.setTemperature(wv1.getSelectedText());
        msg.setTime(wv2.getSelectedText());
        return msg;
    }

}
