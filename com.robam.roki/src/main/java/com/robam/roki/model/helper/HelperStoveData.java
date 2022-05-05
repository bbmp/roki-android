package com.robam.roki.model.helper;

import com.google.common.collect.Lists;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.List;

/**
 * Created by 14807 on 2018/2/7.
 */

public class HelperStoveData {


    //灶具获取时间
    public static List<String> getTimeData(List<Integer> timeList) {
        List<String> times = Lists.newArrayList();

        for (int i = timeList.get(0); i <= timeList.get(1); i += timeList.get(2)) {
            times.add(i + StringConstantsUtil.STRING_MINUTES);
        }
        return times;
    }


}
