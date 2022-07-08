package com.robam.roki.model.helper;

import com.google.common.collect.Lists;
import com.robam.roki.R;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.List;

import static com.legent.ContextIniter.cx;


/**
 * Created by 14807 on 2018/5/7.
 */

public class HelperFanData {

    public static List<String> getListDay(List<Integer> data) {
        ArrayList<String> datas = Lists.newArrayList();
        for (int i = data.get(0); i <= data.get(1);
             i = data.get(2) + i) {
            datas.add(i + cx.getString(R.string.fan_day));
        }
        return datas;
    }


    //小时
    public static List<String> getTimeHour(List<Integer> hour) {
        List<String> hourList = new ArrayList<>();
        for (int i = 0; i <= hour.get(1); i = i + hour.get(hour.size() - 1)) {
            hourList.add(i + cx.getString(R.string.fan_time_remind_hour));
        }
        return hourList;
    }
    //时
    public static List<String> getTimeHour2(List<Integer> hour) {
        List<String> hourList = new ArrayList<>();
        for (int i = 0; i <= hour.get(1); i = i + hour.get(hour.size() - 1)) {
            hourList.add(i + cx.getString(R.string.fan_time_remind_hour2));
        }
        return hourList;
    }



    //分钟
    public static List<String> getTimeMinute(List<Integer> minute) {
        List<String> minuteList = new ArrayList<>();
        for (int i = minute.get(0); i <= minute.get(1); i = i + minute.get(minute.size() - 1)) {
            minuteList.add(i + cx.getString(R.string.minute));
        }
        return minuteList;
    }
    //分
    public static List<String> getTimeMinute2(List<Integer> minute) {
        List<String> minuteList = new ArrayList<>();
        for (int i = minute.get(0); i <= minute.get(1); i = i + minute.get(minute.size() - 1)) {
            minuteList.add(i + cx.getString(R.string.minute2));
        }
        return minuteList;
    }
    //度
    public static List<String>getTemp(List<Integer>temp){
        List<String>tempList=new ArrayList<>();
        for (int i = temp.get(0); i <= temp.get(1); i = i + temp.get(temp.size() - 1)) {
            tempList.add(i + cx.getString(R.string.dialog_degree_text));
        }
        return tempList;
    }



    //星期一 ->  1
    public static short getWeekValue(String date) {

        short week = 0;
        if (cx.getString(R.string.on_monday).equals(date)) {
            week = 1;
        } else if (cx.getString(R.string.on_tuesday).equals(date)) {
            week = 2;

        } else if (cx.getString(R.string.on_wednesday).equals(date)) {
            week = 3;

        } else if (cx.getString(R.string.on_thursdays).equals(date)) {
            week = 4;

        } else if (cx.getString(R.string.on_fridays).equals(date)) {
            week = 5;

        } else if (cx.getString(R.string.on_saturday).equals(date)) {
            week = 6;

        } else if (cx.getString(R.string.on_sunday).equals(date)) {
            week = 7;

        }
        return week;
    }

}
