package com.robam.common.events;

import com.robam.common.pojos.device.microwave.AbsMicroWave;

/**
 * Created by WZTCM on 2015/12/17.
 */
public class MicroWaveTimeEvent {
    AbsMicroWave absMicroWave;
    short time;

    public MicroWaveTimeEvent(AbsMicroWave absMicroWave, short time) {
        this.absMicroWave = absMicroWave;
        this.time = time;
    }
}
