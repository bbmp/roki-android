package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;

public class DisherWasherStatusChangeEvent extends AbsEvent<AbsDishWasher> {
    public DisherWasherStatusChangeEvent(AbsDishWasher absDishWasher) {
        super(absDishWasher);
    }
}
