package com.robam.common.events;

import com.robam.common.pojos.device.dishWasher.AbsDishWasher;

public class DishWasherOpenDoorResetEvent {
    private AbsDishWasher washer;
    private short open;

    public DishWasherOpenDoorResetEvent(AbsDishWasher washer, short open) {
        this.washer = washer;
        this.open = open;
    }
}
