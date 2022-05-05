package com.robam.common.events;

import com.robam.common.pojos.device.microwave.AbsMicroWave;

/**
 * Created by WZTCM on 2015/12/17.
 */
public class MicroWaveSwitchEvent {
    AbsMicroWave absMicroWave;
    short status;

    public MicroWaveSwitchEvent(AbsMicroWave absMicroWave, short status) {
        this.absMicroWave = absMicroWave;
        this.status = status;
    }
}
