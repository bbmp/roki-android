package com.robam.common.events;

import com.robam.common.pojos.device.microwave.AbsMicroWave;

/**
 * Created by WZTCM on 2015/12/17.
 */
public class MicroWaveWeightEvent {
    AbsMicroWave absMicroWave;
    short weight;

    public MicroWaveWeightEvent(AbsMicroWave absMicroWave, short weight) {
        this.absMicroWave = absMicroWave;
        this.weight = weight;
    }
}
