package com.robam.common.events;

import com.robam.common.pojos.device.fan.AbsFan;

/**
 * Created by Administrator on 2016/4/8.
 */
public class FanPlateRemoveEvent {
    public AbsFan fan;
    public short flag_PlateRemove;

    public FanPlateRemoveEvent(AbsFan fan, short flag_PlateRemove) {
        this.fan = fan;
        this.flag_PlateRemove = flag_PlateRemove;
    }
}
