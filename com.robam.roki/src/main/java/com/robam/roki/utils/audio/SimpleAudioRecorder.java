package com.robam.roki.utils.audio;


import com.robam.roki.utils.audio.recorder.RecordState;
import com.robam.roki.utils.audio.recorder.RecorderCallback;

/**
 *
 *
 */

public interface SimpleAudioRecorder {

    // act
    void record(String targetPath);

    void pause();

    void resume();

    void stop();

    void release();

    // state
    RecordState getState();

    // callback
    void addCallback(RecorderCallback callback);
}
