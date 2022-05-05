package com.legent.plat;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.common.collect.Lists;
import com.legent.IDispose;
import com.legent.LogTags;
import com.legent.utils.EventUtils;

import java.util.List;

abstract public class PlatApp extends Application implements IDispose {

    protected final static String TAG = LogTags.TAG_APP;
    private static PlatApp instance;

    public static PlatApp getInstance() {
        return instance;
    }

    protected List<Activity> activities = Lists.newArrayList();

    // -------------------------------------------------------------------------------
    // App Start
    // -------------------------------------------------------------------------------
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.i(TAG, "App created...");

        init();
        Log.i(TAG, "App inited...");
    }

    @Override
    public void dispose() {
        clearActivityList();
    }

    public void exit() {
        dispose();
        System.exit(0);
    }


    protected void init() {

    }

    // -------------------------------------------------------------------------------
    // Activity 管理
    // -------------------------------------------------------------------------------

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    protected void clearActivityList() {
        if (activities.size() > 0) {
            for (Activity atv : activities) {
                atv.finish();
            }
        }
        activities.clear();
    }




}
