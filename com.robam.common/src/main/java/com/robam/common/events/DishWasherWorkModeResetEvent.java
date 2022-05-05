package com.robam.common.events;

import com.robam.common.pojos.device.dishWasher.AbsDishWasher;

public class DishWasherWorkModeResetEvent {
    private AbsDishWasher washer;
    private short mode;

    public DishWasherWorkModeResetEvent(AbsDishWasher washer, short mode) {
        this.washer = washer;
        this.mode = mode;
    }
}
