package com.robam.common.events;

import com.robam.common.pojos.device.rika.AbsRika;

public class RikaSteamWorkEvent {
    public AbsRika rika;
    public short steamEventCode;
    public short steamEventArg;
    public RikaSteamWorkEvent(AbsRika rika, short steamEventCode, short steamEventArg) {
        this.rika = rika;
        this.steamEventCode = steamEventCode;
        this.steamEventArg = steamEventArg;
    }

}
