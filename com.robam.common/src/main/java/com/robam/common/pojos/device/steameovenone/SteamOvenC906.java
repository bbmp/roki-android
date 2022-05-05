package com.robam.common.pojos.device.steameovenone;

import com.legent.plat.pojos.device.DeviceInfo;
import com.robam.common.pojos.device.IRokiFamily;

/**
 * Created by 14807 on 2017/10/11.
 */

public class SteamOvenC906 extends AbsSteameOvenOne {

    public SteamOvenC906(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public String getSteamOvenOneMode() {
        return IRokiFamily.RC906;
    }
}
