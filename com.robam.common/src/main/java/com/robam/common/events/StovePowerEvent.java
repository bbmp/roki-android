package com.robam.common.events;

import com.robam.common.pojos.device.Stove.Stove;

public class StovePowerEvent {
    public Stove stove;
    public boolean power;

    public StovePowerEvent(Stove stove, boolean power) {
        this.stove = stove;
        this.power = power;
    }
}
