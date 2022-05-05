package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/10.
 */

public class NewSteamOvenOneAlarm2Event {

    public AbsSteameOvenOne steameOvenOne;
    public short alarmId;

    public NewSteamOvenOneAlarm2Event(AbsSteameOvenOne steameOvenOne, short alarms) {
        this.steameOvenOne = steameOvenOne;
        this.alarmId = alarms;
    }

}
