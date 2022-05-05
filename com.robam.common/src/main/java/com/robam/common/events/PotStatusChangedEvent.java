package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.Pot.Pot;

/**
 * Created by as on 2017-04-06.
 */

public class PotStatusChangedEvent extends AbsEvent<Pot> {
    public PotStatusChangedEvent(Pot pot) {
        super(pot);
    }
}
