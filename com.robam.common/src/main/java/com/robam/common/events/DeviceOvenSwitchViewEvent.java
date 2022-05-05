package com.robam.common.events;

import android.os.Bundle;

/**
 * Created by linxiaobin on 2016/1/11.
 */
public class DeviceOvenSwitchViewEvent {
    public int type;
    public Bundle bd;

    public DeviceOvenSwitchViewEvent(int type, Bundle bundle) {
        this.type = type;
        this.bd = bundle;
    }
}
