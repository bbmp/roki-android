package com.robam.common.events;

import com.robam.common.pojos.device.dishWasher.AbsDishWasher;

public class DishWasherLackRinseResetEvent {
    private AbsDishWasher washer;
    private short rinse;

    public DishWasherLackRinseResetEvent(AbsDishWasher washer, short rinse) {
        this.washer = washer;
        this.rinse = rinse;
    }
}
