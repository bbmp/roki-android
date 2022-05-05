package com.robam.common.events;

import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.AbsFanPAD;

/**
 * Created by Administrator on 2018/2/2.
 */
public class FanOilCupCleanEvent {
    public AbsFan fan;

    public FanOilCupCleanEvent(AbsFan fan) {
        this.fan = fan;
    }
    public FanOilCupCleanEvent(AbsFanPAD fan) {
        this.fan = fan;
    }
}
