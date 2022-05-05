package com.robam.common.util;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by as on 2017-02-22.
 */

public class ButtonUtils {
    static private boolean ButtonLock;

    public static boolean isButtonLock() {
        return isButtonLock(1000);
    }

    public static boolean isButtonLock(int millisecond) {
        if (!ButtonLock) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ButtonLock = false;
                }
            }, millisecond);
            ButtonLock = true;
            return false;
        }
        return ButtonLock;
    }
}
