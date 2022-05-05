package com.robam.common.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.AlarmUtils;

/**
 * Created by sylar on 15/7/25.
 */
public class UpdateApkReceiver extends BroadcastReceiver {

    static public Intent getIntent(Context cx) {
        Intent intent = new Intent(cx, UpdateApkReceiver.class);
        intent.setAction(UpdateApkReceiver.class.getName());
        return intent;
    }

    public static long updatePollingPeriodInBack = 1000 * 60 * 60 * 24;  // 毫秒，秒，分，时, 一天更新一次

    @Override
    public void onReceive(Context cx, Intent i) {
        try {
            AppUpdateService.getInstance().start(UIService.getInstance().getFormManager("MainForm").getActivity());
        } catch (Exception e) {
            LogUtils.logFIleWithTime("更新轮训出错:" + e.getMessage());
        }

        if (Build.VERSION.SDK_INT >= 19) {
            /** API 19 后的 AlarmManager 不再提供精准闹钟
             *
             * From API level 19, all repeating alarms are inexact—that is, if our application targets KitKat or above, our repeat alarms will be inexact even if we use setRepeating.
             If we really need exact repeat alarms, we can use setExact instead, and schedule the next alarm while handling the current one.
             *
             * **/
            AlarmUtils.startPollingWithBroadcast(UIService.getInstance().getFormManager("MainForm").getActivity(),
                    UpdateApkReceiver.getIntent(UIService.getInstance().getFormManager("MainForm").getActivity()),
                    updatePollingPeriodInBack,
                    Plat.deviceService.getUpdatePollingTaskId());
        }

    }

}
