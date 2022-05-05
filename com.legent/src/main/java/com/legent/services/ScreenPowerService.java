package com.legent.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import com.google.common.base.Objects;
import com.legent.events.ScreenPowerChangedEvent;
import com.legent.utils.EventUtils;

public class ScreenPowerService extends AbsService {

    final public static int OFF = 0;
    final public static int ON = 1;
    final public static String TAG = ScreenPowerService.class.getSimpleName();

    static private ScreenPowerService instance = new ScreenPowerService();

    synchronized static public ScreenPowerService getInstance() {
        return instance;
    }


    // 电源管理器
    PowerManager pm;
    // 唤醒锁
    PowerManager.WakeLock wakeLock;


//    // 键盘管理器
//    KeyguardManager km;
//    // 键盘锁
//    KeyguardManager.KeyguardLock keyguardLock;

    private ScreenPowerService() {

    }

    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);
        registReceiver();

        pm = (PowerManager) cx.getSystemService(Context.POWER_SERVICE);
//        km = (KeyguardManager) cx.getSystemService(Context.KEYGUARD_SERVICE);
    }

    @Override
    public void dispose() {
        super.dispose();
        unregistReceiver();

        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
//        if (keyguardLock != null) {
//            keyguardLock.reenableKeyguard();
//        }
    }

    public boolean isScreenOn() {
        PowerManager pm = (PowerManager) cx
                .getSystemService(Context.POWER_SERVICE);
        boolean flag = pm.isScreenOn();
        return flag;
    }

    public void wakeup() {
        if (isScreenOn()) return;

        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }

        wakeLock = pm.newWakeLock
                (PowerManager.PARTIAL_WAKE_LOCK | PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
                            // by zhaiyuanyi 20151026  ACQUIRE_CAUSES_WAKEUP ----> PARTIAL_WAKE_LOCK
        wakeLock.acquire();

//        keyguardLock = km.newKeyguardLock(getClass().getSimpleName());
//        keyguardLock.disableKeyguard();
    }

    public void gotoSleep() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }

        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wakeLock.acquire();
    }


    private void registReceiver() {
        IntentFilter screenStatusIF = new IntentFilter();
        screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
        screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
        cx.registerReceiver(receiver, screenStatusIF);
    }

    private void unregistReceiver() {
        cx.unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent in) {
            String inAction = in.getAction();
            if (Objects.equal(inAction, Intent.ACTION_SCREEN_OFF)) {
                EventUtils.postEvent(new ScreenPowerChangedEvent(OFF));
            } else if (Objects.equal(inAction, Intent.ACTION_SCREEN_ON)) {
                EventUtils.postEvent(new ScreenPowerChangedEvent(ON));
            }
        }
    };
}
