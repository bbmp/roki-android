package com.robam.common.util;

/**
 * Created by yinwei on 2017/12/11.
 */

public interface IsendCommand {
    void onStart();
    void onPause();
    void onPreSend();
    void onFinish();
    void onRestart();
}
