package com.robam.common.events;

import com.robam.common.pojos.device.Oven.AbsOven;

/**
 * Created by linxiaobin on 2015/12/27.
 */
public class OvenTempResetEvent {
    public AbsOven oven;
    public short temp;

    public OvenTempResetEvent(AbsOven oven, short temp) {
        this.oven = oven;
        this.temp = temp;
    }
}
