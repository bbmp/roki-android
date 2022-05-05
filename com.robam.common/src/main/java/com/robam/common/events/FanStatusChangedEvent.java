package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.AbsFanPAD;

/**
 * 油烟机状态刷新事件
 *
 * @author sylar
 */
public class FanStatusChangedEvent extends AbsEvent<AbsFan> {


    public FanStatusChangedEvent(AbsFan pojo) {
        super(pojo);
    }

    public FanStatusChangedEvent(AbsFanPAD pojo) {
        super(pojo);
    }

}