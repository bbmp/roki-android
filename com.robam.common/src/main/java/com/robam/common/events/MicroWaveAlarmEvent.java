package com.robam.common.events;

import com.robam.common.pojos.device.microwave.AbsMicroWave;

/**
 * Created by Rent on 2016/06/24.
 */
public class MicroWaveAlarmEvent {
    public AbsMicroWave absMicroWave;
    public short alarm;

    public MicroWaveAlarmEvent(AbsMicroWave absMicroWave, short am) {
        this.absMicroWave = absMicroWave;
        this.alarm = am;
    }
}
