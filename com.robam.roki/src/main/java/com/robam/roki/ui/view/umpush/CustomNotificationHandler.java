package com.robam.roki.ui.view.umpush;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.events.UMPushInfoEvent;
import com.robam.common.events.UMPushRecipeEvent;
import com.robam.common.events.UMPushThemeEvent;
import com.robam.common.events.UMPushVideoEvent;
import com.umeng.message.UmengNotificationClickHandler;

import com.umeng.message.entity.UMessage;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhoudingjun on 2017/4/12.
 */

public class CustomNotificationHandler  extends UmengNotificationClickHandler{

    private static final String TAG = CustomNotificationHandler.class.getName();
    protected static final String APPPACKAGENAME = "com.robam.roki";

    public CustomNotificationHandler() {
    }

    @Override
    public void dismissNotification(Context context, UMessage msg) {
        super.dismissNotification(context, msg);
    }


    @Override
    public void launchApp(Context context, UMessage msg) {
        Log.d(TAG, "launchApp");
        super.launchApp(context, msg);
        if (isRokiInBackground(context)) {
            if (msg.extra!=null) {
                startRokiAPP(context, APPPACKAGENAME, msg.extra.entrySet());
            }
        } else {
            String key = "0";
            String value = "0";
            if (msg.extra.entrySet() != null) {
                for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
                    key = entry.getKey();
                    value = entry.getValue();
                    if (key.contains("type")) {
                        UMPushMsg.setMsgType(Integer.valueOf(value));
                    }
                    if (key.contains("id")) {
                        UMPushMsg.setMsgId(Integer.valueOf(value));
                    }
                }
            }

                switch (UMPushMsg.getMsgType()) {
                    case PushContent.PUSHCONINFOMATION:
                        EventUtils.postEvent(new UMPushInfoEvent(true));
                        break;
                    case PushContent.PUSHTHEME:
                        EventUtils.postEvent(new UMPushThemeEvent());
                        break;
                    case PushContent.PUSHRECIPE:
                        EventUtils.postEvent(new UMPushRecipeEvent(true));
                        break;
                    case PushContent.PUSHVIDEO:
                        EventUtils.postEvent(new UMPushVideoEvent(true));
                        break;
                }

        }
    }

    @Override
    public void openActivity(Context context, UMessage msg) {
        Log.d(TAG, "openActivity");
        super.openActivity(context, msg);
    }


    @Override
    public void openUrl(Context context, UMessage msg) {
        Log.d(TAG, "openUrl");
        super.openUrl(context, msg);
    }

    @Override
    public void dealWithCustomAction(Context context, UMessage msg) {
        Log.d(TAG, "dealWithCustomAction");
        super.dealWithCustomAction(context, msg);
    }

//    @Override
//    public void autoUpdate(Context context, UMessage msg) {
//        Log.d(TAG, "autoUpdate");
//        super.autoUpdate(context, msg);
//    }

    //打开roki app
    public void startRokiAPP(Context cx, String appPackageName, Set<Map.Entry<String, String>> set) {
        LogUtils.i("20190917", "startRokiAPP");
        try {
            Intent intent = cx.getPackageManager().getLaunchIntentForPackage(appPackageName);
            String key;
            String value;
            if (set != null) {
                for (Map.Entry<String, String> entry : set) {
                    key = entry.getKey();
                    value = entry.getValue();
                    if (key != null)
                        intent.putExtra(key, value);
                }
            }
            cx.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(cx, "没有安装", Toast.LENGTH_LONG).show();
        }
    }

    //Android 判断app是否在前台还是在后台运行
    public static boolean isRokiInBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
				GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
				 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
}
