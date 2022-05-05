package com.robam.common.pojos.device.WaterPurifier;

import com.legent.plat.pojos.device.DeviceInfo;
import com.robam.common.pojos.device.IRokiFamily;

/**
 * Created by Administrator on 2017/6/1.
 */

public class WaterPurifierJ320 extends AbsWaterPurifier implements IWaterPurifier {

    public WaterPurifierJ320(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public String getWaterModel() {
        return IRokiFamily.RJ320;
    }

}
