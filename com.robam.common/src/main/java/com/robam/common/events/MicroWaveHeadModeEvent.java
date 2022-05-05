package com.robam.common.events;

import com.robam.common.pojos.device.microwave.AbsMicroWave;

/**
 * Created by WZTCM on 2015/12/17.
 */
public class MicroWaveHeadModeEvent {
    AbsMicroWave absMicroWave;
    short mode;

    public MicroWaveHeadModeEvent(AbsMicroWave absMicroWave, short mode) {
        this.absMicroWave = absMicroWave;
        this.mode = mode;
    }
}
