package com.robam.common.events;

import com.robam.common.pojos.device.fan.AbsFan;

public class FanTimingCompletedEvent {
    public AbsFan fan;
    public  short flag_countend;                    //定时取消与否

    public FanTimingCompletedEvent(AbsFan fan, short flag_countend) {
        this.fan = fan;
        this.flag_countend = flag_countend;

    }
}
