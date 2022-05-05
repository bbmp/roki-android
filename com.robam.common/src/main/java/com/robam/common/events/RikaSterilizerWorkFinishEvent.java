package com.robam.common.events;

import com.robam.common.pojos.device.rika.AbsRika;

public class RikaSterilizerWorkFinishEvent {
    public AbsRika rika;
    public short sterilEventArg;

    public RikaSterilizerWorkFinishEvent(AbsRika rika , short sterilEventArg) {
        this.rika = rika;
        this.sterilEventArg = sterilEventArg;
    }

}
