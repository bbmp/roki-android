package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/10.
 */

public class SteamOvenOneLightResetEvent {

    public AbsSteameOvenOne steameOvenOne;


    public SteamOvenOneLightResetEvent(AbsSteameOvenOne steameOvenOne) {
        this.steameOvenOne = steameOvenOne;

    }
}
