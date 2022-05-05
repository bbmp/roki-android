package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

public class SteamOvenOneBegainWorkSteamEvent {
    public AbsSteameOvenOne steameOvenOne;
    short setSteameOvenBasicMode;

    public SteamOvenOneBegainWorkSteamEvent(AbsSteameOvenOne steameOvenOne, short setSteameOvenBasicMode) {
        this.steameOvenOne = steameOvenOne;
        this.setSteameOvenBasicMode = setSteameOvenBasicMode;
    }
}
