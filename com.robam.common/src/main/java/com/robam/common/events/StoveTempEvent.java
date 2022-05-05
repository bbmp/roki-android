package com.robam.common.events;

import com.robam.common.pojos.device.Stove.Stove;

/**
 * Created by zhaiyuanyi on 15/10/30.
 */
public class StoveTempEvent {
    public Stove stove;
    public float temp;

    public StoveTempEvent() {
    }

    public StoveTempEvent(Stove stove, float temp) {
        this.stove = stove;
        this.temp = temp;
    }
}
