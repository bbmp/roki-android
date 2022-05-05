package com.robam.roki.ui.view.networkoptimization;

import com.robam.common.pojos.DeviceGroupList;

import java.util.Comparator;

/**
 * Created by zhoudingjun on 2016/12/19.
 */

public class SortComparatorGroupListBean implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {
        DeviceGroupList a = (DeviceGroupList) lhs;
        DeviceGroupList b = (DeviceGroupList) rhs;
        return (a.getSortId() - b.getSortId());
    }
}

