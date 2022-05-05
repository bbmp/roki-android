package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.Stove.Stove;

/**
 * 电磁灶状态刷新事件
 *
 * @author sylar
 */
public class StoveStatusChangedEvent extends AbsEvent<Stove> {

    public StoveStatusChangedEvent(Stove pojo) {
        super(pojo);
    }
}