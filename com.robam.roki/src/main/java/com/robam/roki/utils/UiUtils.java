package com.robam.roki.utils;

import android.content.Context;
import android.content.res.Resources;

import com.robam.roki.MobApp;

/**
 * Created by zhoudingjun on 2017/6/5.
 */

public class UiUtils {

    /**
     * 获取上下文
     *
     * @return
     */
    public static Context getContext() {
        return MobApp.getInstance().getApplicationContext();
    }

    /**
     * 获取 Resources 对象
     *
     * @return
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 等到string.xml 中的字符数组
     *
     * @param resId
     * @return
     */
    public static String[] getStringArr(int resId) {
        return getResources().getStringArray(resId);
    }


}
