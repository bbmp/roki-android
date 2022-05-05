package com.robam.common.events;

import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.AbsFanPAD;

public class FanPowerEvent {
    public AbsFan fan;
    public boolean power;

    public FanPowerEvent(AbsFan fan, boolean power) {
        this.fan = fan;
        this.power = power;
    }
    public FanPowerEvent(AbsFanPAD fan, boolean power) {
        this.fan = fan;
        this.power = power;
    }
}
