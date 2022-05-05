package com.robam.common.events;

import com.robam.common.pojos.device.Stove.Stove;

public class StoveLevelEvent {
    public Stove stove;
    public short level;

    public StoveLevelEvent(Stove stove, short level) {
        this.stove = stove;
        this.level = level;
    }
}
