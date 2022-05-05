package com.robam.roki.model.helper;

import com.google.common.collect.Lists;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.List;

/**
 * Created by 14807 on 2018/2/7.
 */

public class HelperMicroWaveData {


    public static List<String> getMinuteData(List<Integer> minuteList) {
        List<String> minutes = Lists.newArrayList();
        if (minuteList.size() > 1) {
            for (int i = minuteList.get(0); i <= minuteList.get(1); i += minuteList.get(2)) {
                minutes.add(i + StringConstantsUtil.STRING_MINUTES);
            }
        } else {
            minutes.add(minuteList.get(0) + StringConstantsUtil.STRING_MINUTES);
        }

        return minutes;
    }

    public static List<String> getSecondData(List<Integer> timeList) {
        List<String> times = Lists.newArrayList();

        for (int i = timeList.get(0); i <= timeList.get(1); i += timeList.get(2)) {
            times.add(i + StringConstantsUtil.STRING_SEN);
        }

        return times;
    }

    public static List<String> getListData(List<Integer> valueList, String unit) {
        List<String> list = Lists.newArrayList();
        for (int i = valueList.get(0); i <= valueList.get(1); i += valueList.get(2)) {
            list.add(i + unit);
        }
        return list;
    }
}
