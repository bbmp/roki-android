package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.microwave.AbsMicroWave;

/**
 * Created by Rosicky on 15/12/14.
 */
public class MicroWaveStatusChangedEvent extends AbsEvent<AbsMicroWave> {

    public MicroWaveStatusChangedEvent(AbsMicroWave absMicroWave) {
        super(absMicroWave);
    }
}
