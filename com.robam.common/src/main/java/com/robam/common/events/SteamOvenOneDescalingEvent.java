package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/10.
 */

public class SteamOvenOneDescalingEvent extends AbsEvent<AbsSteameOvenOne> {

    public SteamOvenOneDescalingEvent(AbsSteameOvenOne absSteameOvenOne) {
        super(absSteameOvenOne);
    }
}
