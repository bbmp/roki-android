package com.robam.common.events;

import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.integratedStove.IntegStoveAlarmCodeBean;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaAlarmCodeBean;

/**
 * Created by Administrator on 2017/7/10.
 */

public class IntegStoveAlarmEvent {

    public AbsIntegratedStove integratedStove;
    public short faulCode;
    public short category ;


    public IntegStoveAlarmEvent(AbsIntegratedStove integratedStove ,short faulCode , short category) {

        this.integratedStove = integratedStove;
        this.faulCode = faulCode ;
        this.category = category ;
    }

}
