package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;

/**
 * Created by Rent on 2016/05/30.
 */
public class WaterPurifiyStatusChangedEvent extends AbsEvent<AbsWaterPurifier> {
    public WaterPurifiyStatusChangedEvent(AbsWaterPurifier absWater) {
        super(absWater);
    }
}
