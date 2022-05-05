package com.robam.common.events;

import com.robam.common.pojos.device.Oven.AbsOven;

/**
 * Created by linxiaobin on 2015/12/27.
 */
public class OvenAlarmEvent {
    public AbsOven oven;
    public short alarmId;

    public OvenAlarmEvent(AbsOven oven, short alarmId) {
        this.oven = oven;
        this.alarmId = alarmId;
    }
}
