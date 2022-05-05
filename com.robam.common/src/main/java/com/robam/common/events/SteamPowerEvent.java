package com.robam.common.events;

import com.robam.common.pojos.device.Steamoven.AbsSteamoven;

/**
 * Created by Rosicky on 15/12/15.
 */
public class SteamPowerEvent {
    public AbsSteamoven steamoven;
    public boolean power;

    public SteamPowerEvent(AbsSteamoven steamoven, boolean power) {
        this.steamoven = steamoven;
        this.power = power;
    }
}
