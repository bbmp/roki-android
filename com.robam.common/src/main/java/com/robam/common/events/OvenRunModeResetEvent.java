package com.robam.common.events;

import com.robam.common.pojos.device.Oven.AbsOven;

/**
 * Created by linxiaobin on 2015/12/27.
 */
public class OvenRunModeResetEvent {
    public AbsOven oven;
    public short runMode;

    public OvenRunModeResetEvent(AbsOven oven, short runMode) {
        this.oven = oven;
        this.runMode = runMode;
    }
}
