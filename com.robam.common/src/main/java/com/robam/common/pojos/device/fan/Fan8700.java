package com.robam.common.pojos.device.fan;

import com.legent.plat.pojos.device.DeviceInfo;
import com.robam.common.pojos.device.IRokiFamily;

/**
 * Created by zhaiyuanyi on 15/10/19.
 */
public class Fan8700 extends AbsFan implements IFan8700 {
    public Fan8700(DeviceInfo devInfo) {
        super(devInfo);
    }

   /* @Override
    public String getFanModel() {
        return IRokiFamily.R8700;
    }*/

}
