package com.robam.roki.utils.audio;

import com.robam.roki.utils.audio.player.PlayerCallback;
import com.robam.roki.utils.audio.player.PlayerState;

/**
 *
 */

public interface SimpleAudioPlayer {

    // play
    void play(String tagetPath);

    void pause();

    void resume();

    void stop();

    void release();

    // state
    PlayerState getState();

    // interactive
    void setOffSet(int offSet);

    // callback
    void addCallback(PlayerCallback callback);

}
