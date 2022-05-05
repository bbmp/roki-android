package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

public class OvenOtherEvent  extends AbsEvent<AbsSteameOvenOne> {

    public OvenOtherEvent(AbsSteameOvenOne steameOvenOne) {
        super(steameOvenOne);
    }
}
