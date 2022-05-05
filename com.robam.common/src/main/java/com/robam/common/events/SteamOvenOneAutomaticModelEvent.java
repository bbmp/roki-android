package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/10.
 * 一体机自动模式调整事件
 */

public class SteamOvenOneAutomaticModelEvent {

    public AbsSteameOvenOne steameOvenOne;

    public SteamOvenOneAutomaticModelEvent(AbsSteameOvenOne steameOvenOne) {
        this.steameOvenOne = steameOvenOne;
    }
}
