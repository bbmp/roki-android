package com.robam.roki.utils;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/6.
 */

public class OvenOrderTimeDataUtil {

    //预约小时数据
    public static List<String> getListOrderTimeHourData(){
        List<String> hourList = Lists.newArrayList();
        for (int i = 0; i <= 23; i++) {
            hourList.add(i+ StringConstantsUtil.STR_HOUR);

        }
        return hourList;
    }
    //预约分钟数据
    public static List<String> getListOrderTimeMinData(){
        List<String> minList = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            minList.add(i+StringConstantsUtil.STRING_MINUTES);
        }
        return minList;
    }

}
