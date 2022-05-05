package com.robam.common.events;

import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.AbsFanPAD;

public class FanLightEvent {
    public AbsFan fan;
    public boolean power;

    public FanLightEvent(AbsFan fan, boolean power) {
        this.fan = fan;
        this.power = power;
    }
    public FanLightEvent(AbsFanPAD fan, boolean power) {
        this.fan = fan;
        this.power = power;
    }
}