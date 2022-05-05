package com.robam.common.events;

import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;

/**
 * Created by zhaiyuanyi on 15/11/21.
 */
public class SteriAlarmEvent {
    public short alarm;
    public AbsSterilizer absSterilizer;

    public SteriAlarmEvent(AbsSterilizer absSterilizer, short alarmId){
        this.absSterilizer = absSterilizer;
        this.alarm = alarmId;
    }


}
