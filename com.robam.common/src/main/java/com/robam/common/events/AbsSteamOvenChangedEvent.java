package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;

public class AbsSteamOvenChangedEvent extends AbsEvent<AbsSteameOvenOneNew> {
    public AbsSteamOvenChangedEvent(AbsSteameOvenOneNew absSteameOvenOneNew) {
        super(absSteameOvenOneNew);
    }
}
