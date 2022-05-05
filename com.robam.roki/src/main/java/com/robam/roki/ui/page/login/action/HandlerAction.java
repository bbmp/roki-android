package com.robam.roki.ui.page.login.action;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

public interface HandlerAction {
    public static final Handler HANDLER = new Handler(Looper.getMainLooper());

    default Handler getHandler() {
        return HANDLER;
    }

    default boolean post(Runnable r) {
        return postDelayed(r, 0L);
    }

    default boolean postDelayed(Runnable r, long delayMillis) {
        if (delayMillis < 0L)
            delayMillis = 0L;
        return postAtTime(r, SystemClock.uptimeMillis() + delayMillis);
    }

    default boolean postAtTime(Runnable r, long uptimeMillis) {
        return HANDLER.postAtTime(r, this, uptimeMillis);
    }

    default void removeCallbacks(Runnable r) {
        HANDLER.removeCallbacks(r);
    }

    default void removeCallbacks() {
        HANDLER.removeCallbacksAndMessages(this);
    }
}

