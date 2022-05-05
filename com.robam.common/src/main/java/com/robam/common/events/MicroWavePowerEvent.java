package com.robam.common.events;

import com.robam.common.pojos.device.microwave.AbsMicroWave;

/**
 * Created by WZTCM on 2015/12/17.
 */
public class MicroWavePowerEvent {
    AbsMicroWave absMicroWave;
    short power;

    public MicroWavePowerEvent(AbsMicroWave absMicroWave, short power) {
        this.absMicroWave = absMicroWave;
        this.power = power;
    }
}
