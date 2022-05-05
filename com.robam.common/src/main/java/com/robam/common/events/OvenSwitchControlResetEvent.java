package com.robam.common.events;

import com.robam.common.pojos.device.Oven.AbsOven;

/**
 * Created by linxiaobin on 2015/12/27.
 */
public class OvenSwitchControlResetEvent {
    public AbsOven oven;
    public boolean switchControlStatus;

    public OvenSwitchControlResetEvent(AbsOven oven, boolean switchControlStatus) {
        this.oven = oven;
        this.switchControlStatus = switchControlStatus;
    }
}
