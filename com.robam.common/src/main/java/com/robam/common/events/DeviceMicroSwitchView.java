package com.robam.common.events;

import com.robam.common.pojos.device.microwave.MicroWaveWheelMsg;

/**
 * Created by Administrator on 2016/6/6.
 */
public class DeviceMicroSwitchView {
    public int type;
    public MicroWaveWheelMsg msg;
    public DeviceMicroSwitchView(int type, MicroWaveWheelMsg msg) {
        this.type = type;
        this.msg = msg;
    }
}
