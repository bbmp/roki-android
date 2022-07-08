package com.robam.roki.utils.audio.recorder;

/**
 *
 * 在 UI thread 触发
 */
public interface RecorderCallback {

    void onRecord();

    void onProgress(int sec);

    void onPause();

    void onStop();
}
