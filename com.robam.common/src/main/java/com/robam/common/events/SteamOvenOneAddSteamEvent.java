package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/10.
 * 一体机加蒸汽事件
 */

public class SteamOvenOneAddSteamEvent {

    public AbsSteameOvenOne steameOvenOne;


    public SteamOvenOneAddSteamEvent(AbsSteameOvenOne steameOvenOne) {
        this.steameOvenOne = steameOvenOne;
    }
}
