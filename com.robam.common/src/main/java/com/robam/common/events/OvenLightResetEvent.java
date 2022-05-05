package com.robam.common.events;

import com.robam.common.pojos.device.Oven.AbsOven;

/**
 * Created by linxiaobin on 2015/12/27.
 */
public class OvenLightResetEvent {
    public AbsOven oven;
    public short light;

    public OvenLightResetEvent(AbsOven oven, short light) {
        this.oven = oven;
        this.light = light;
    }
}
