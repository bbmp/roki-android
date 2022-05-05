package com.robam.common.events;

import com.robam.common.pojos.device.Steamoven.AbsSteamoven;

/**
 * Created by WZTCM on 2015/12/16.
 */
public class SteamFinishEvent {

    public AbsSteamoven steam;
    public short alarmId;

    public SteamFinishEvent(AbsSteamoven steam, short alarmId) {
        this.steam = steam;
        this.alarmId = alarmId;
    }
}
