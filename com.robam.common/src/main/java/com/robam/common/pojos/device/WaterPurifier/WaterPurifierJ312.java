package com.robam.common.pojos.device.WaterPurifier;

import com.legent.plat.pojos.device.DeviceInfo;
import com.robam.common.pojos.device.IRokiFamily;

/**
 * Created by Administrator on 2017/5/9.
 */

public class WaterPurifierJ312 extends AbsWaterPurifier implements IWaterPurifier{
    public WaterPurifierJ312(DeviceInfo devInfo) {
        super(devInfo);
    }

    //待修改
    @Override
    public String getWaterModel() {
        return IRokiFamily.RJ312;
    }
}
