package com.robam.roki.model.helper;

import android.content.Context;

import com.google.common.collect.Lists;
import com.legent.utils.api.PreferenceUtils;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.R;
import com.robam.roki.utils.DateUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.List;

import static com.legent.ContextIniter.cx;

/**
 * Created by 14807 on 2018/2/7.
 */

public class HelperRikaData {
    public static List<String> getList2StringHour(List<Integer> data) {

        List<String> list = Lists.newArrayList();
        list.clear();
        for (int i = 0; i < data.size(); i++) {
            Integer integer = data.get(i);
            list.add(integer + "小时");
        }

        return list;
    }

    public static List<String> getList2StringMin(List<Integer> data) {

        List<String> list = Lists.newArrayList();
        list.clear();
        for (int i = 0; i < data.size(); i++) {
            Integer integer = data.get(i);
            list.add(integer + "分钟");
        }

        return list;
    }

    public static List<String> getList2String(List<Integer> data) {

        List<String> list = Lists.newArrayList();
        list.clear();
        for (int i = 0; i < data.size(); i++) {
            Integer integer = data.get(i);
            list.add(integer + "");
        }

        return list;
    }

    //RIKA获取小时
    public static List<String> getHous() {
        List<String> hous = Lists.newArrayList();
        for (int i = 0; i <= 23; i++) {
            if (i < 10) {
                hous.add("0" + i);
            } else {
                hous.add(i + "");
            }
        }
        return hous;
    }

    /**
     * 获取小时差
     *
     * @param orderDate
     * @return
     */
    public static int getHousGap(String orderDate) {
        String currentTime = DateUtil.getCurrentTime(DateUtil.PATTERN);
        int orderMin = Integer.parseInt(orderDate.substring(orderDate.length() - 2, orderDate.length()));
        int currentMin = Integer.parseInt(currentTime.substring(currentTime.length() - 2, orderDate.length()));
        int orderHours = Integer.parseInt(orderDate.substring(0, 2));
        int currentHours = Integer.parseInt(currentTime.substring(0, 2));
        if (DateUtil.compareTime(currentTime, orderDate, DateUtil.PATTERN) == 1) {
            if (orderMin - currentMin < 0) {
                return orderHours + 23 - currentHours;
            } else {
                return orderHours + 24 - currentHours;
            }
        } else {
            if (orderMin - currentMin < 0) {
                return orderHours - currentHours - 1;
            } else {
                return orderHours - currentHours;
            }
        }
    }

    public static int getMinGap620(String orderDate) {
        String currentTime = DateUtil.getCurrentTime(DateUtil.PATTERN_TIME);
        int orderSecond = Integer.parseInt(orderDate.substring(orderDate.length() - 2));
        int currentSecond = Integer.parseInt(currentTime.substring(currentTime.length() -2));
//        String s=;
//        String ss=;
        int orderMin = Integer.parseInt(orderDate.substring(3,5));

        int currentMin =  Integer.parseInt(currentTime.substring(3,5));


        int orderHours = Integer.parseInt(orderDate.substring(0, 2));
        int currentHours = Integer.parseInt(currentTime.substring(0, 2));
        int hours=0;
        if (DateUtil.compareTime(currentTime, orderDate, DateUtil.PATTERN_TIME) == 1) {
            if (orderMin - currentMin < 0) {
                hours= (orderHours + 23 - currentHours)*60+(orderMin-currentMin)+60;
            } else {
                hours= (orderHours + 24 - currentHours)*60+(currentMin-orderMin);
            }
        }else{
            if (orderMin - currentMin < 0) {
                hours= (orderHours - currentHours - 1)+(orderMin-currentMin)+60;
            } else {
                hours= (orderHours - currentHours)*60+(orderMin-currentMin);
            }
        }
        if (orderSecond<currentSecond){
            hours=hours*60-(orderSecond+60-currentSecond);
        }else{
            hours=hours*60-(currentSecond-orderSecond);
        }
        return hours;
    }

    public static int getMinGap(String orderDate) {
        String currentTime = DateUtil.getCurrentTime(DateUtil.PATTERN);
        int orderMin = Integer.parseInt(orderDate.substring(orderDate.length() - 2));
        int currentMin = Integer.parseInt(currentTime.substring(currentTime.length() - 2, orderDate.length()));
//        int orderHours = Integer.parseInt(orderDate.substring(0, 2));
//        int currentHours = Integer.parseInt(currentTime.substring(0, 2));

//        int hours=0;
//        if (DateUtil.compareTime(currentTime, orderDate, DateUtil.PATTERN) == 1) {
//            if (orderMin - currentMin < 0) {
//                hours= (orderHours + 23 - currentHours)*60+(orderMin-currentMin)+60;
//            } else {
//                hours= (orderHours + 24 - currentHours)*60+(currentMin-orderMin);
//            }
//        }else{
//            if (orderMin - currentMin < 0) {
//                hours= (orderHours - currentHours - 1)*60+(orderMin-currentMin)+60;
//            } else {
//                hours= (orderHours - currentHours)*60+(orderMin-currentMin);
//            }
//        }

        int hours=0;
        if (DateUtil.compareTime(currentTime, orderDate, DateUtil.PATTERN) == 1) {
            if (orderMin - currentMin < 0) {
                 hours  =   orderMin - currentMin + 60;;
            } else {
                hours= currentMin - orderMin;
            }
        }else{
            if (orderMin < currentMin) {
                hours  =   orderMin - currentMin + 60;
//                hours= (orderHours - currentHours - 1)*60+(orderMin-currentMin)+60;
            } else {
                hours  =    orderMin - currentMin;
//                hours= (orderHours - currentHours)*60+(orderMin-currentMin);
            }
        }


        return hours;

    }

    //RIKA获取时间
    public static List<String> getMin() {
        List<String> mins = Lists.newArrayList();
        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                mins.add("0" + i);
            } else {
                mins.add(i + "");
            }
        }
        return mins;
    }
//    public static List<String> getPowerData(List<Integer> powerList) {
//        List<String> powers = Lists.newArrayList();
//        for (Integer integer : powerList) {
//            powers.add(integer.toString());
//        }
//        return powers;
//
//
//    }
    //RIKA获取温度
    public static List<String> getTempData(List<Integer> tempList) {
        List<String> temps = Lists.newArrayList();
        if (tempList.size() > 1) {
            for (int i = tempList.get(0); i <= tempList.get(1); i += tempList.get(2)) {
                temps.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else {
            temps.add(tempList.get(0) + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        }

        return temps;
    }

    public static List<String> getTempData2(List<Integer> tempList) {
        List<String> temps = Lists.newArrayList();
        if (tempList.size() > 1) {
            for (int i = tempList.get(0); i <= tempList.get(tempList.size() - 1); i++) {
                temps.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else {
            temps.add(tempList.get(0) + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        }

        return temps;
    }

    public static List<String> getTempData3(List<Integer> tempList) {
        List<String> temps = Lists.newArrayList();
        if (tempList.size() > 1) {
            for (int i = tempList.get(0); i <= tempList.get(1); i += tempList.get(2)) {
                temps.add(i + "");
            }
        } else {
            temps.add(tempList.get(0) + "");
        }

        return temps;
    }

    public static List<String> getTempDataEXPCentener(List<Integer> tempList, String temp) {
        List<String> temps = Lists.newArrayList();
        int temp2 = Integer.parseInt(temp);
        if (tempList.size() > 1) {
            for (int i = tempList.get(0); i <= tempList.get(1); i += tempList.get(2)) {
                if (i >= temp2 - 20 && i <= temp2 + 20) {
                    temps.add(i + "");
                }
            }
        } else {
            temps.add(tempList.get(0) + "");
        }

        return temps;
    }


    //RIKA获取时间
    public static List<String> getTimeData(List<Integer> timeList) {
        List<String> times = Lists.newArrayList();

        for (int i = timeList.get(0); i <= timeList.get(1); i += timeList.get(2)) {
            times.add(i + StringConstantsUtil.STRING_MINUTES);
        }

        return times;
    }

    public static List<String> getTimeData2(List<Integer> timeList) {
        List<String> times = Lists.newArrayList();

        for (int i = timeList.get(0); i <= timeList.get(timeList.size() - 1); i += timeList.get(2)) {
            times.add(i + StringConstantsUtil.STRING_MINUTES);
        }

        return times;
    }

    public static List<String> getTimeData3(List<Integer> timeList, int count) {
        List<String> times = Lists.newArrayList();

        for (int i = timeList.get(0); i <= timeList.get(timeList.size() - 1); i += count) {
            times.add(i + StringConstantsUtil.STRING_MINUTES);
        }

        return times;
    }

    public static List<String> getTimeData4(List<Integer> timeList) {
        List<String> times = Lists.newArrayList();

        for (int i = timeList.get(0); i <= timeList.get(1); i += timeList.get(2)) {
            times.add(i + "");
        }

        return times;
    }

    public static List<String> getTimeDataHour(List<Integer> list) {

        List<String> hourList = Lists.newArrayList();
        for (int i = list.get(0); i <= list.get(1); i += list.get(2)) {
            hourList.add(i + StringConstantsUtil.STRING_HOUR);
        }

        return hourList;
    }

    //RIKA获取智能设定时间
    public static List<String> getLinkageTime(List<Integer> timeList) {
        List<String> times = Lists.newArrayList();

        for (int i = timeList.get(0); i <= timeList.get(1); i += timeList.get(2)) {
            times.add(i + StringConstantsUtil.STRING_MINUTES);
        }
        return times;
    }

    //RIKA获取时间
    public static List<Integer> getListData(List<Integer> timeList) {
        List<Integer> times = Lists.newArrayList();
        for (int i = timeList.get(0); i <= timeList.get(1); i += timeList.get(2)) {
            times.add(i);
        }
        return times;
    }

    //RIKA获取时间
    public static List<Integer> getListRemoveData(List<Integer> timeList) {
        List<Integer> times = Lists.newArrayList();
        for (int i = 1; i <= timeList.get(1); i += timeList.get(2)) {
            times.add(i);
        }
        return times;
    }

    /**
     * 获取报警的状态返回对应的内容
     *
     * @param cx
     * @param status
     * @return
     */
    public static String getAlarmContent(Context cx, int status) {

        switch (status) {
            case RikaStatus.STERIL_DISIDFECT:
                return cx.getString(R.string.sterilizer_complete_disinfect);

            case RikaStatus.STERIL_CLEAN:
                return cx.getString(R.string.sterilizer_complete_clean);

            case RikaStatus.STERIL_DRYING:
                return cx.getString(R.string.sterilizer_complete_drying);

            case RikaStatus.STERIL_PRE:
                return cx.getString(R.string.sterilizer_complete_pre);

            case RikaStatus.STERIL_DEGERMING:
                return cx.getString(R.string.sterilizer_complete_degerming);

            case RikaStatus.STERIL_APPOINATION:
                return cx.getString(R.string.sterilizer_complete_pre_dis);

            case RikaStatus.STERIL_APPOINATION_DRYING:
                return cx.getString(R.string.sterilizer_complete_pre_dry);

            case RikaStatus.STERIL_APPOINATION_CLEAN:
                return cx.getString(R.string.sterilizer_complete_pre_clean);

            case RikaStatus.STERIL_COER_DISIDFECT:
                return cx.getString(R.string.sterilizer_complete_coer_disidfect);
        }

        return "";
    }

}
