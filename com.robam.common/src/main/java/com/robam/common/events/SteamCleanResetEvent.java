package com.robam.common.events;

import com.robam.common.pojos.device.Steamoven.AbsSteamoven;

/**
 * Created by Rosicky on 15/12/15.
 */
public class SteamCleanResetEvent {
    public AbsSteamoven steamoven;
    public short finish;

    public SteamCleanResetEvent(AbsSteamoven steamoven, short finish) {
        this.steamoven = steamoven;
        this.finish = finish;
    }
}
