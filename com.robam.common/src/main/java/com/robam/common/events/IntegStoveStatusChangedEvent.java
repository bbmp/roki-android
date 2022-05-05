package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.rika.AbsRika;

/**
 * Created by Administrator on 2017/7/8.
 * 集成灶状态事件
 */

public class IntegStoveStatusChangedEvent extends AbsEvent<AbsIntegratedStove> {

    public IntegStoveStatusChangedEvent(AbsIntegratedStove absIntegratedStove) {
        super(absIntegratedStove);
    }
}
