package com.robam.common.events;

import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;

public class AbsSteamOvenOneEvent {

    AbsSteameOvenOneNew AbsSteameOvenOneNew;

    public AbsSteamOvenOneEvent(com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew absSteameOvenOneNew) {
        AbsSteameOvenOneNew = absSteameOvenOneNew;
    }
}
