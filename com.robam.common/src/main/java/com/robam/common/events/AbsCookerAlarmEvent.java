package com.robam.common.events;

import com.robam.common.pojos.device.cook.AbsCooker;

/**
 * Created by Dell on 2018/8/30.
 */

public class AbsCookerAlarmEvent {
    public AbsCooker absCooker;
    public short alramId;

    public AbsCookerAlarmEvent(AbsCooker absCooker, short alramId){
        this.absCooker = absCooker;
        this.alramId = alramId;
    }
}
