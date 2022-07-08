package com.legent.ui.ext.views;

import android.content.Context;
import android.util.AttributeSet;

import com.legent.ui.ext.adapters.SimpleAreaAdapter;

import java.util.List;

/**
 * Created by sylar on 15/7/21.
 */
public class SimpleAreaWheelView extends AbsThreeWheelView {

    protected SimpleAreaAdapter adapter = SimpleAreaAdapter.getInstance();

    public SimpleAreaWheelView(Context cx) {
        super(cx);
    }

    public SimpleAreaWheelView(Context cx, AttributeSet attrs) {
        super(cx, attrs);
    }

    public String getSelected() {
        return String.format("%s%s%s", getSelectedItem1(), getSelectedItem2(), getSelectedItem3());
    }

    @Override
    protected List<?> getList1() {
        return adapter.getProvinceDatas();
    }

    @Override
    protected List<?> getList2(Object province) {
        return adapter.getCities(province.toString());
    }

    @Override
    protected List<?> getList3(Object city) {
        return adapter.getConties(city.toString());
    }

}
