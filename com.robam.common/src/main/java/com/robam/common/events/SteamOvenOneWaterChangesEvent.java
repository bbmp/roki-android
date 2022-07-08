package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;

/**
 * Created by Administrator on 2017/7/10.
 * 一体机水箱更改事件
 */

public class SteamOvenOneWaterChangesEvent {

    public AbsSteameOvenOne steameOvenOne;
    public AbsSteameOvenOneNew steameOvenOneNew;

    public SteamOvenOneWaterChangesEvent(AbsSteameOvenOneNew steameOvenOneNew) {
        this.steameOvenOneNew = steameOvenOneNew;
    }

    public SteamOvenOneWaterChangesEvent(AbsSteameOvenOne steameOvenOne) {
        this.steameOvenOne = steameOvenOne;

    }
}
