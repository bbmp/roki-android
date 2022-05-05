package com.robam.common.util;

/**
 * Created by 14807 on 2018/5/25.
 */

public class FanLockSE638Utils {

    public static Short lock = 0;

    public static void setLock(Short lk) {
        lock = lk;
    }

    public static Short getLock() {
        return lock;
    }
}
