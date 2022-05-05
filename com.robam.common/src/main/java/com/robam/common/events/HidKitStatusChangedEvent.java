package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.hidkit.AbsHidKit;

/**
 * Created by Administrator on 2017/7/8.
 * 藏宝盒状态事件
 */

public class HidKitStatusChangedEvent extends AbsEvent<AbsHidKit> {

    public HidKitStatusChangedEvent(AbsHidKit absHidKit) {
        super(absHidKit);
    }
}
