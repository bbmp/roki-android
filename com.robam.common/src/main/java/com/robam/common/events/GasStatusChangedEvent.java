package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.gassensor.GasSensor;

/**
 * Created by Dell on 2018/5/31.
 */

public class GasStatusChangedEvent extends AbsEvent<GasSensor> {

    public GasStatusChangedEvent(GasSensor gasSensor) {
        super(gasSensor);
    }
}
