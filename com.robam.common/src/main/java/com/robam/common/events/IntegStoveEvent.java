package com.robam.common.events;

import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;

/**
 * 蒸烤一体机事件
 */

public class IntegStoveEvent {

    public AbsIntegratedStove integratedStove;
    public short eventCode;
    public short category;


    public IntegStoveEvent(AbsIntegratedStove integratedStove , short category , short eventCode ) {

        this.integratedStove = integratedStove;
        this.category = category;
        this.eventCode = eventCode;
    }

}
