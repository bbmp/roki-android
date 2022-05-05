package com.robam.common.events;

import com.robam.common.pojos.device.dishWasher.AbsDishWasher;

public class DishWasherAlarmEvent  {
    public AbsDishWasher washer;
    public short alarmId;

    public DishWasherAlarmEvent(AbsDishWasher washer, short alarmId) {
        this.washer = washer;
        this.alarmId = alarmId;
    }
}
