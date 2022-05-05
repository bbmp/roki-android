package com.robam.common.events;

import com.robam.common.pojos.device.rika.AbsRika;

public class RikaSteamOvenWorkEvent {
    public AbsRika rika;
    public short steamOvenEventCode;
    public short steamOvenEventArg;
    public RikaSteamOvenWorkEvent(AbsRika rika, short steamOvenEventCode, short steamOvenEventArg) {
        this.rika = rika;
        this.steamOvenEventCode = steamOvenEventCode;
        this.steamOvenEventArg = steamOvenEventArg;
    }

}
