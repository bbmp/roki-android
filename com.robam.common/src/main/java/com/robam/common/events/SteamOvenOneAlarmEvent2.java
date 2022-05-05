package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/10.
 * 缺水警报
 */

public class SteamOvenOneAlarmEvent2 {

    public short alarm;



    public SteamOvenOneAlarmEvent2(short alarm) {
        this.alarm = alarm;
    }
}
