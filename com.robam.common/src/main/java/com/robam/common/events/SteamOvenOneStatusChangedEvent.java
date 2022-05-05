package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/8.
 * 一体机状态事件
 */

public class SteamOvenOneStatusChangedEvent extends AbsEvent<AbsSteameOvenOne> {

    public SteamOvenOneStatusChangedEvent(AbsSteameOvenOne absSteameOvenOne) {
        super(absSteameOvenOne);
    }
}
