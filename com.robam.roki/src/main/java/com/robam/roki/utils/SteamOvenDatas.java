package com.robam.roki.utils;

import com.legent.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by you on 2017/10/13.
 */

public class SteamOvenDatas {

    //蒸烤一体机模块化数据------------------------------
    public static List<Integer> createExpDownDatas(int upTemp, int stepC, int dev, int max, int min) {
        LogUtils.i("20180720", " upTemp:" + upTemp + " stepC:" + stepC + " max:" + max + " min:" + min + " dev:" + dev);
        List<Integer> timeList = new ArrayList<>();
        if (upTemp - stepC <= dev) {
            for (int i = dev; i <= upTemp + stepC; i++) {
                timeList.add(i);
            }
            return timeList;
        }
        if (upTemp > dev && upTemp <= max - stepC) {
            for (int i = upTemp - stepC; i <= upTemp + stepC; i++) {
                timeList.add(i);
            }
            return timeList;
        }

        if (upTemp >= (max - stepC)) {
            for (int i = upTemp - stepC; i <= max; i++) {
                timeList.add(i);
            }
            return timeList;
        }
        return timeList;
    }


}
