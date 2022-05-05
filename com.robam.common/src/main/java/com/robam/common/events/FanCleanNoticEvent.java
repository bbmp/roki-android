package com.robam.common.events;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.AbsFanPAD;

public class FanCleanNoticEvent {
    public AbsFan fan;

    public FanCleanNoticEvent(AbsFan fan) {
        this.fan = fan;
    }
    public FanCleanNoticEvent(AbsFanPAD fan) {
        this.fan = fan;
    }
}
