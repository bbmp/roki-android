package com.robam.common.events;

import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaAlarmCodeBean;

/**
 * Created by Administrator on 2017/7/10.
 */

public class RikaAlarmEvent {

    public AbsRika mRika;
    public RikaAlarmCodeBean mAlarmCodeBean;


    public RikaAlarmEvent(AbsRika rika, RikaAlarmCodeBean alarmCodeBean) {

        this.mRika = rika;
        this.mAlarmCodeBean = alarmCodeBean;
    }

}
