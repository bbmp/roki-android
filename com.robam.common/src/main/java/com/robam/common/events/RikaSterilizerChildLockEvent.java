package com.robam.common.events;

import com.robam.common.pojos.device.rika.AbsRika;

public class RikaSterilizerChildLockEvent {
    public AbsRika rika;
    public short sterilEventArg;

    public RikaSterilizerChildLockEvent(AbsRika rika , short sterilEventArg) {
        this.rika = rika;
        this.sterilEventArg = sterilEventArg;
    }

}
