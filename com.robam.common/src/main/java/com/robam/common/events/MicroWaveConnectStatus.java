package com.robam.common.events;

import com.robam.common.pojos.device.microwave.AbsMicroWave;

/**
 * Created by Administrator on 2017/5/17.
 */

public class MicroWaveConnectStatus {
    AbsMicroWave microWave;
     public boolean flag;
    public  MicroWaveConnectStatus(AbsMicroWave microWave, boolean flag){
        this.microWave=microWave;
        this.flag=flag;
    }
}
