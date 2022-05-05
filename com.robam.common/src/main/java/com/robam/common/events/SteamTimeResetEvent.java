package com.robam.common.events;

import com.robam.common.pojos.device.Steamoven.AbsSteamoven;

/**
 * Created by Rosicky on 15/12/15.
 */
public class SteamTimeResetEvent {
    public AbsSteamoven steamoven;
    public short time;

    public SteamTimeResetEvent(AbsSteamoven steamoven, short time) {
        this.steamoven = steamoven;
        this.time = time;
    }
}
