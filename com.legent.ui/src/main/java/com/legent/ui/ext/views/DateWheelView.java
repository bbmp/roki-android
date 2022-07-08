package com.legent.ui.ext.views;

import android.content.Context;
import android.util.AttributeSet;

import com.google.common.collect.Lists;

import java.util.Calendar;
import java.util.List;

/**
 * Created by sylar on 15/7/21.
 */
public class DateWheelView extends AbsThreeWheelView {

    List<String> years;
    Calendar defDate;

    public DateWheelView(Context cx) {
        super(cx);
    }

    public DateWheelView(Context cx, AttributeSet attrs) {
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
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH) - 1;

        int yearIndex = getList1().indexOf(String.valueOf(year));

        wv1.setDefault(yearIndex);
        wv2.setDefault(month);
        wv3.setDefault(day);
    }

    public Calendar getSelected() {
        int year = Integer.parseInt(getSelectedItem1().toString());
        int month = Integer.parseInt(getSelectedItem2().toString());
        int day = Integer.parseInt(getSelectedItem3().toString());

        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day, 0, 0, 0);
        return c;
    }

    @Override
    protected List<?> getList1() {
        if (years == null) {
            years = Lists.newArrayList();
            for (int i = 2000; i < 2050; i++)
                years.add(String.valueOf(i));
        }

        return years;
    }

    @Override
    protected List<?> getList2(Object item) {
        List<String> list = Lists.newArrayList();
        for (int i = 1; i < 13; i++)
            list.add(String.valueOf(i));

        if (list == null || list.size() == 0) {
            list = Lists.newArrayList();
            list.add("");
        }

        return list;
    }

    @Override
    protected List<?> getList3(Object item) {

        int year = Integer.parseInt(getSelectedItem1().toString());
        int month = Integer.parseInt(item.toString());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);//Java月份才0开始算
        int dateOfMonth = cal.getActualMaximum(Calendar.DATE);

        List<String> list = Lists.newArrayList();
        for (int i = 1; i <= dateOfMonth; i++)
            list.add(String.valueOf(i));

        if (list == null || list.size() == 0) {
            list = Lists.newArrayList();
            list.add("");
        }

        return list;
    }
}
