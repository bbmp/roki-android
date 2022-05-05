package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.rika.AbsRika;

/**
 * Created by Administrator on 2017/7/8.
 * 一体机状态事件
 */

public class RikaStatusChangedEvent extends AbsEvent<AbsRika> {

    public RikaStatusChangedEvent(AbsRika absRika) {
        super(absRika);
    }
}
