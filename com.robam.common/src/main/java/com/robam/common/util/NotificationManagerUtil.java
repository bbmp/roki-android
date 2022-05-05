package com.robam.common.util;

import android.app.NotificationManager;
import android.content.Context;

/**
 * Created by Administrator on 2016/7/7.
 */
public class NotificationManagerUtil {
    private static NotificationManager instance;

    public static NotificationManager getInstance(Context cx) {
        if (instance != null) {
            return instance;
        } else {
            String ns = Context.NOTIFICATION_SERVICE;
            return (NotificationManager) cx.getSystemService(ns);
        }
    }
}
