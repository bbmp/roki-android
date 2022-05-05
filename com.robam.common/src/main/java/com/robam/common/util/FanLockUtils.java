package com.robam.common.util;

/**
 * Created by 14807 on 2018/5/25.
 */

public class FanLockUtils {

    public static Short lock = 0;
    public static String mGuid;

    public static void setLock(Short lk, String guid) {
        lock = lk;
        mGuid = guid;
    }

    public static Short getLock() {
        return lock;
    }

    public static String getGuid() {
        return mGuid;
    }
}
