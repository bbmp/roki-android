package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;

/**
 * Created by Dell on 2018/10/29.
 */

public class SterFinishEvent extends AbsEvent<AbsSterilizer> {
    public short eventId;
    public short eventParam;


    public SterFinishEvent(AbsSterilizer absSterilizer, short eventId, short eventParam) {
        super(absSterilizer);
        this.eventId = eventId;
        this.eventParam = eventParam;
    }
}
