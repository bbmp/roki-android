package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;

/**
 * Created by zhaiyuanyi on 15/11/21.
 */
public class SteriStatusChangedEvent extends AbsEvent<AbsSterilizer> {
    public SteriStatusChangedEvent(AbsSterilizer o) {
        super(o);
    }
}
