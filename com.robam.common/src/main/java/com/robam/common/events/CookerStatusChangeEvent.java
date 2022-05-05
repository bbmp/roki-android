package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.cook.AbsCooker;

/**
 * Created by Dell on 2018/6/7.
 */

public class CookerStatusChangeEvent extends AbsEvent<AbsCooker> {

    public CookerStatusChangeEvent(AbsCooker absCooker) {
        super(absCooker);
    }
}
