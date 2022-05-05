package com.robam.common.events;

import com.robam.common.pojos.device.dishWasher.AbsDishWasher;

public class DishWasherSwitchControlResetEvent {
    private AbsDishWasher washer;
    private short switchControlStatus;

    public DishWasherSwitchControlResetEvent(AbsDishWasher washer, short switchControlStatus) {
        this.washer = washer;
        this.switchControlStatus = switchControlStatus;
    }
}
