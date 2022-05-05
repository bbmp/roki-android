package com.robam.common.events;

import com.robam.common.pojos.device.rika.AbsRika;

public class RikaFanCleanNoticEvent {
    public AbsRika rika;

    public RikaFanCleanNoticEvent(AbsRika rika) {
        this.rika = rika;
    }

}
