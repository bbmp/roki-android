package com.robam.common.util;

import android.util.Log;

import com.legent.plat.Plat;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by as on 2017-08-08.
 */

public class PollingSignalUtils {
    static short range_min = 2;//最大检测分钟数
    static short maxSize = (short) (range_min * 60 / 2);//最大检测轮训数
    /**
     * String guid ,  Short[0] 代表轮训次数  Short[0]回复次数
     */
    static ConcurrentHashMap<String, LinkedList<Short>> mSignalMap = new ConcurrentHashMap();

    /**
     * 每次轮序 创建位图
     */
    public synchronized static void polling(String guid) {
        LinkedList<Short> list = mSignalMap.get(guid);
        if (list == null)
            list = new LinkedList<Short>();
        if (Plat.DEBUG)
            Log.i("oven_st", list.toString());
        if (list.size() >= maxSize) {
            list.remove(0);
        }
        if (Plat.DEBUG)
            Log.i("oven_st", list.toString());
        list.add((short) 0);
        mSignalMap.put(guid, list);
    }

    public synchronized static void pollingFeed(String guid) {
        LinkedList<Short> list = mSignalMap.get(guid);
        if (list == null)
            return;
        for (int index = 1; index <= list.size(); index++) {
            short s = list.get(list.size() - index);
            if (s == 0) {
                list.set(list.size() - index, (short) 1);
                break;
            }
        }
        if (Plat.DEBUG)
            Log.i("oven_st", list.toString());
    }

    public static float getConnectSingal(String guid) {
        LinkedList<Short> list = mSignalMap.get(guid);
        if (list == null)
            return 0f;
        int validBit = 0;
        for (short bit : list) {
            validBit = validBit + bit;
        }
        if (Plat.DEBUG)
            Log.i("oven_st", "validBit:" + validBit + "  size:" + list.size());
        float ft = ((float) validBit) / (float) (list.size());
        BigDecimal bd = new BigDecimal((double) ft);
        bd = bd.setScale(2, 4);
        ft = bd.floatValue();
        return ft;
    }

    public static void clear() {
        if (mSignalMap != null)
            mSignalMap.clear();
    }
}
