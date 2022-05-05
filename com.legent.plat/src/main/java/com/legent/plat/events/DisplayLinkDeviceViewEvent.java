package com.legent.plat.events;

/**
 * Created by Administrator on 2017/6/8.
 */
public class DisplayLinkDeviceViewEvent {
    private int cookStepIndex;

    public DisplayLinkDeviceViewEvent(int cookStepIndex) {
        this.cookStepIndex = cookStepIndex;
    }

    public int getCookStepIndex() {
        return cookStepIndex;
    }
}
