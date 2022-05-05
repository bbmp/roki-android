package com.robam.common.util;

import android.app.AlarmManager;
import android.content.Context;

/**
 * Created by Rent on 2016/7/5.
 */
public class AlarmManagerUtil {
    private  static AlarmManager instance;
    public static AlarmManager getInstance(Context ctx){
        if(instance!=null){
            return instance;
        }else{
            return getAlarmManager(ctx);
        }

    }
    private static AlarmManager getAlarmManager(Context ctx){
        return (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
    }

}
