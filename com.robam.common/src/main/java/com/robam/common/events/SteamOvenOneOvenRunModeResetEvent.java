package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/8.
 *
 */

public class SteamOvenOneOvenRunModeResetEvent {

    public AbsSteameOvenOne steameOvenOne;

    public SteamOvenOneOvenRunModeResetEvent(AbsSteameOvenOne steameOvenOne) {
        this.steameOvenOne = steameOvenOne;
    }
}
