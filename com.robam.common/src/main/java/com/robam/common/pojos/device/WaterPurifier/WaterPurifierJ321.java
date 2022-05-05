package com.robam.common.pojos.device.WaterPurifier;

import com.legent.plat.pojos.device.DeviceInfo;
import com.robam.common.pojos.device.IRokiFamily;

/**
 * Created by Administrator on 2017/5/18.
 */

public class WaterPurifierJ321 extends AbsWaterPurifier implements IWaterPurifier {
    public WaterPurifierJ321(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public String getWaterModel() {
        return IRokiFamily.RJ321;
    }
}
