package com.robam.roki.ui.view;

import android.content.Context;

import com.legent.ui.ext.views.AbsThreeWheelView;
import com.robam.roki.model.CrmArea;
import com.robam.roki.model.CrmHelper;
import com.robam.roki.model.PCR;

import java.util.List;

/**
 * Created by sylar on 15/8/5.
 */
public class CrmAreaWheelView extends AbsThreeWheelView {

    public CrmAreaWheelView(Context cx) {
        super(cx);
    }

    @Override
    protected void loadData() {
        super.loadData();
        wv1.setDefault(0);
        wv2.setDefault(0);
        wv3.setDefault(0);
    }

    public CrmArea getSelected() {
        CrmArea ca = new CrmArea();
        ca.province = getSelectedItem1();
        ca.city = getSelectedItem2();
        ca.county = getSelectedItem3();

        return ca;
    }

    @Override
    protected List<?> getList1() {
        return CrmHelper.getProvinces();
    }

    @Override
    protected List<?> getList2(Object item) {
        PCR pcr = (PCR) item;
        return pcr.children;
    }

    @Override
    protected List<?> getList3(Object item) {
        PCR pcr = (PCR) item;
        return pcr.children;
    }


}
