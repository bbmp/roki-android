package com.robam.roki.broadcast;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.legent.utils.api.PreferenceUtils;
import com.robam.common.util.NotificationManagerUtil;
import com.robam.roki.R;
import com.robam.roki.ui.form.MainActivity;


import java.util.List;

import static com.robam.roki.ui.adapter.FanOtherFuncAdapter.SOUP_REMIND;

/**
 * Created by Administrator on 2016/7/7.
 */
public class Fan8700alarmReceiver extends BroadcastReceiver {
    public static String timeString= "0:0";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("fan8700-->receiver-ex", "------->广播事件收到---------" + intent.getAction());
        //在进程中去寻找当前APP的信息，判断是否在前台运行
        /*if(isAppOnForeground(context)){
            return;
        }*/
        //String timeString=intent.getStringExtra("broaddata");
        String string=timeString;
        String str1=PreferenceUtils.getString(SOUP_REMIND,"none");
        if(!"none".equals(str1)){
            string=str1;
        }
        Log.i("fan8700-->receiver",string);
        String[] strings = string.split(":");
        int totalTime=Integer.parseInt(strings[1]);
        int hour=totalTime/(1000*60*60);
        int min=((totalTime/1000)% 3600)/60;
        Log.i("fan8700_remind",min+":"+totalTime);
        NotificationManager notificationManager = NotificationManagerUtil.getInstance(context);


        int importance = NotificationManager.IMPORTANCE_HIGH;
        //关键代码：版本sdk大于或等于8.0.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(context.getPackageName(), "ROKI智能烹饪", importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("ROKI智能烹饪")//设置通知栏标题
                .setContentText("计时时间已到，请注意查看关火！") //设置通知栏显示内容
                .setTicker("计时提醒") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setChannelId(context.getPackageName()) //必须添加（Android 8.0） 【唯一标识】
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.ic_recipe_roki_logo);//设置通知小ICON
        Intent it = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, it, 0);
        mBuilder.setContentIntent(pendingIntent);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notification);



    }

    private boolean isAppOnForeground(Context cx) {
        Context applicationContext=cx.getApplicationContext();
        ActivityManager activityManager = (ActivityManager) applicationContext.getSystemService(
                Context.ACTIVITY_SERVICE);
        String packageName =applicationContext.getPackageName();
        Log.i("fan8700-->receiver-ex","packageName"+packageName);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
