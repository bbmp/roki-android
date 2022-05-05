package com.robam.common.events;

import com.robam.common.pojos.device.gassensor.GasSensor;

/**
 * Created by Dell on 2018/6/1.
 */

public class GasAlarmEvent {
    public GasSensor gasSensor;
    public short alarmId;

    public GasAlarmEvent(GasSensor gasSensor, short alarmId){
        this.gasSensor = gasSensor;
        this.alarmId = alarmId;
    }
}
