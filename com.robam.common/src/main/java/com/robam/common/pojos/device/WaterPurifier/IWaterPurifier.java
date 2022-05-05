package com.robam.common.pojos.device.WaterPurifier;

import com.legent.VoidCallback;
import com.robam.common.pojos.device.IPauseable;

/**
 * Created by Rent on 16/05/25.
 */
public interface IWaterPurifier extends IPauseable {
    /**
     * 启动净水功能
     *
     * @param model
     * @param callback
     */
    void setWaterPurifier(short model, VoidCallback callback);

    /**
     * 净水器的型号
     * @return
     */
    String getWaterModel();
}
