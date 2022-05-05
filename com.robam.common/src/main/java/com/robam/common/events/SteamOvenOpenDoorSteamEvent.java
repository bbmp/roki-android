package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

public class SteamOvenOpenDoorSteamEvent extends AbsEvent<AbsSteameOvenOne> {

    public SteamOvenOpenDoorSteamEvent(AbsSteameOvenOne absSteameOvenOne) {
        super(absSteameOvenOne);
    }


}
