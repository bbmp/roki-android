package com.legent.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.legent.BuildConfig;

public class EventUtils {

    static final String TAG = "EventBus";
    static boolean ENABLE_LOG = false;

    static EventBus bus = new EventBus();

    static {
        bus.register(new DeadEventListener());
    }

    public static void regist(Object listener) {
        bus.register(listener);
    }

    public static void unregist(Object listener) {
        bus.unregister(listener);
    }

    public static void postEvent(final Object event) {

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {

                @Override
                public void run() {
                    bus.post(event);
                }
            });
            h = null;

    }

    static public class DeadEventListener {

        @Subscribe
        public void listen(DeadEvent event) {

            if (BuildConfig.DEBUG && ENABLE_LOG) {
                Log.w(TAG,
                        String.format("###DeadEvent###\nevent:%s", event
                                .getEvent().getClass().getName()));
            }
        }
    }

}
