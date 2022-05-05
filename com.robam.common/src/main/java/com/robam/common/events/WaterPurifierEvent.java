package com.robam.common.events;

import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;

/**
 * Created by yinwei on 2017/8/3.
 */

public class WaterPurifierEvent {
    public AbsWaterPurifier purifier;
    public WaterPurifierEvent(AbsWaterPurifier purifier){
        this.purifier=purifier;
    }
}
