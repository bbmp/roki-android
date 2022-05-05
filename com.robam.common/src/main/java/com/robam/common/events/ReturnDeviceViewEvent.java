package com.robam.common.events;

/**
 * Created by Administrator on 2019/8/6.
 */

public class ReturnDeviceViewEvent {
    private Boolean isReturn;

    public ReturnDeviceViewEvent(Boolean isReturn) {
        this.isReturn = isReturn;
    }

    public Boolean getReturn() {
        return isReturn;
    }
}
