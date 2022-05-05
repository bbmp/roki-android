package com.robam.common.events;

import com.robam.common.pojos.device.Oven.AbsOven;

/**
 * Created by zdj on 2016/09/27.
 */
public class OvenWorkFinishEvent {
    public AbsOven oven;
    public short finish;

    public OvenWorkFinishEvent(AbsOven oven, short finish) {
        this.oven = oven;
        this.finish = finish;
    }
}
