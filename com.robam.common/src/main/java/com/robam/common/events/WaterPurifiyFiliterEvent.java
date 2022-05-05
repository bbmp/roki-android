package com.robam.common.events;

import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;

/**
 * Created by Rent on 2016/06/02.
 */
public class WaterPurifiyFiliterEvent {
    public int filiterId;
    public AbsWaterPurifier purifier;
    public WaterPurifiyFiliterEvent(AbsWaterPurifier purifier, int id){
        this.purifier=purifier;
        this.filiterId=id;
    }
}
