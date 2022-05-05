package com.robam.common.events;

import com.robam.common.pojos.device.Oven.AbsOven;

/**
 * Created by zdj on 2016/09/27.
 */
public class OvenAutoModeAdjustResetEvent {
    public AbsOven oven;
    public short adjustReset;

    public OvenAutoModeAdjustResetEvent(AbsOven oven, short adjustReset) {
        this.oven = oven;
        this.adjustReset = adjustReset;
    }
}
