package com.robam.common.events;

import android.os.Bundle;

/**
 * Created by WZTCM on 2016/1/11.
 */
public class DeviceSteamSwitchViewEvent {
    public int type;
    public Bundle bd;
    public DeviceSteamSwitchViewEvent(int type, Bundle bundle) {
        this.type = type;
        this.bd = bundle;
    }
}
