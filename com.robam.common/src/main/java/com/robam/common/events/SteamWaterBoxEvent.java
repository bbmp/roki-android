package com.robam.common.events;

import com.robam.common.pojos.device.Steamoven.AbsSteamoven;

/**
 * Created by Dell on 2018/2/1.
 */

public class SteamWaterBoxEvent {
    public AbsSteamoven steam;
    public short alarmId;

    public SteamWaterBoxEvent(AbsSteamoven steam, short alarmId){
        this.steam = steam;
        this.alarmId = alarmId;
    }
}
