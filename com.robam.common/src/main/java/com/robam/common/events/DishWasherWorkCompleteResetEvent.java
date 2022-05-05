package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;

public class DishWasherWorkCompleteResetEvent  extends AbsEvent<AbsDishWasher> {
    private short powerConsumption;
    private short waterConsumption;

    public DishWasherWorkCompleteResetEvent(AbsDishWasher washer, short powerConsumption, short waterConsumption) {
        super(washer);
        this.powerConsumption = powerConsumption;
        this.waterConsumption = waterConsumption;
    }

    public short getPowerConsumption() {
        return powerConsumption;
    }

    public short getWaterConsumption() {
        return waterConsumption;
    }
}
