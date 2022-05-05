package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.Oven.AbsOven;

/**
 * Created by linxiaobin on 2015/12/27.
 */
public class OvenStatusChangedEvent extends AbsEvent<AbsOven> {
    public OvenStatusChangedEvent(AbsOven absOven) {
        super(absOven);
    }
}
