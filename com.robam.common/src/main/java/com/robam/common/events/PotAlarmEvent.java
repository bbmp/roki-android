package com.robam.common.events;

import com.robam.common.pojos.device.Pot.Pot;

/**
 * 温控锅报警事件
 */
public class PotAlarmEvent {

    public short alarmId;
    public Pot pot;

    public PotAlarmEvent(Pot pot, short alarmId) {

        this.pot = pot;
        this.alarmId = alarmId;


    }
}