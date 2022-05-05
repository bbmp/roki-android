package com.robam.common.events;

import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;

/**
 * Created by Rent on 2016/06/02.
 */
public class WaterPurifiyAlarmEvent  {
    public int alarmId;
    public AbsWaterPurifier purifier;
    public WaterPurifiyAlarmEvent(AbsWaterPurifier purifier, int id){
        this.purifier=purifier;
        this.alarmId=id;
    }
}
