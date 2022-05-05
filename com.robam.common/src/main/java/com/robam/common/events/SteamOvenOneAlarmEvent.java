package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/10.
 */

public class SteamOvenOneAlarmEvent {

    public AbsSteameOvenOne steameOvenOne;
    public short alarmId;
    public short[] alarms;


    public SteamOvenOneAlarmEvent(AbsSteameOvenOne steameOvenOne, short aalarms) {
        this.steameOvenOne = steameOvenOne;
        this.alarmId = aalarms;
    }

    public SteamOvenOneAlarmEvent(AbsSteameOvenOne steameOvenOne, short[] aalarms) {
        this.steameOvenOne = steameOvenOne;
        this.alarms = aalarms;
    }
}
