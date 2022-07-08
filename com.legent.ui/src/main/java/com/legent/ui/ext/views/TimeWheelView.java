package com.legent.ui.ext.views;

import android.content.Context;
import android.util.AttributeSet;

import com.google.common.collect.Lists;

import java.util.Calendar;
import java.util.List;

/**
 * Created by sylar on 15/7/21.
 */
public class TimeWheelView extends AbsThreeWheelView {

    Calendar defDate;


    public TimeWheelView(Context cx) {
        super(cx);
    }

    public TimeWheelView(Context cx, AttributeSet attrs) {
        super(cx, attrs);
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (defDate != null) {
            onSetDefault(defDate);
        }
    }

    public void setDefault(Calendar c) {
        this.defDate = c;
        if (this.isActivated()) {
            onSetDefault(c);
        }
    }

    void onSetDefault(Calendar c) {
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        wv1.setDefault(hour);
        wv2.setDefault(minute);
        wv3.setDefault(second);
    }


    public Calendar getSelected() {
        int hour = Integer.parseInt(getSelectedItem1().toString());
        int minute = Integer.parseInt(getSelectedItem2().toString());
        int second = Integer.parseInt(getSelectedItem3().toString());

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        return c;
    }

    @Override
    protected List<?> getList1() {
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < 24; i++)
            list.add(String.valueOf(i));

        return list;
    }

    @Override
    protected List<?> getList2(Object item) {
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < 60; i++)
            list.add(String.valueOf(i));

        if (list == null || list.size() == 0) {
            list = Lists.newArrayList();
            list.add("");
        }

        return list;
    }

    @Override
    protected List<?> getList3(Object item) {

        List<String> list = Lists.newArrayList();
        for (int i = 0; i < 60; i++)
            list.add(String.valueOf(i));

        if (list == null || list.size() == 0) {
            list = Lists.newArrayList();
            list.add("");
        }

        return list;
    }
}
