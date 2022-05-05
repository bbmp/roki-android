package com.robam.common.events;

import android.os.Bundle;

/**
 * Created by Administrator on 2016/6/6.
 */
public class DeviceSterilizerSwitchView {
    public int type;
    public Bundle bd;
    public DeviceSterilizerSwitchView(int type, Bundle bundle) {
        this.type = type;
        this.bd = bundle;
    }
}
