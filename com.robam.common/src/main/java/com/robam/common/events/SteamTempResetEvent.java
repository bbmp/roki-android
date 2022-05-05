package com.robam.common.events;

import com.robam.common.pojos.device.Steamoven.AbsSteamoven;

/**
 * Created by Rosicky on 15/12/15.
 */
public class SteamTempResetEvent {
    public AbsSteamoven steamoven;
    public short temp;

    public SteamTempResetEvent(AbsSteamoven steamoven, short temp) {
        this.steamoven = steamoven;
        this.temp = temp;
    }
}
