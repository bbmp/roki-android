package com.robam.common.events;

import com.robam.common.pojos.device.Oven.AbsOven;

/**
 * Created by RuanWei on 2019/11/27.
 */

public class OvenWorkTopFinishEvent {
    public AbsOven oven;
    public short finish;

    public OvenWorkTopFinishEvent(AbsOven oven, short finish) {
        this.oven = oven;
        this.finish = finish;
    }
}
