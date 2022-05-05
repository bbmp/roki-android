package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/10.
 */

public class NewSteamOvenOneAlarmEvent {

    public AbsSteameOvenOne steameOvenOne;
    public short alarmId;

    public NewSteamOvenOneAlarmEvent(AbsSteameOvenOne steameOvenOne, short alarms) {
        this.steameOvenOne = steameOvenOne;
        this.alarmId = alarms;
    }

}
