package com.robam.common.events;

import com.robam.common.pojos.device.Oven.AbsOven;

/**
 * Created by linxiaobin on 2015/12/27.
 */
public class OvenTimeResetEvent {
    public AbsOven oven;
    public short time;

    public OvenTimeResetEvent(AbsOven oven, short time) {
        this.oven = oven;
        this.time = time;
    }
}
