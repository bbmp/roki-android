package com.robam.common.events;

import com.robam.common.pojos.device.dishWasher.AbsDishWasher;

public class DishWasherLackSaltResetEvent {
    private AbsDishWasher washer;
    private short salt;

    public DishWasherLackSaltResetEvent(AbsDishWasher washer, short salt) {
        this.washer = washer;
        this.salt = salt;
    }
}
