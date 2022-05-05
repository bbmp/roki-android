package com.robam.common.events;

import com.robam.common.pojos.device.cook.AbsCooker;

/**
 * Created by Dell on 2018/8/30.
 */

public class CookerParamReportEvent {
   public AbsCooker cooker;
    public short param;

    public CookerParamReportEvent(AbsCooker cooker, short param){
        this.cooker = cooker;
        this.param = param;
    }

}
