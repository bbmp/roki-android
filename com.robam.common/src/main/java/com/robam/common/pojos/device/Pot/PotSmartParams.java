package com.robam.common.pojos.device.Pot;

import java.io.Serializable;

/**
 * Created by sylar on 15/6/10.
 */
public class PotSmartParams implements Serializable{


    public short potBurningWarnSwitch = 3;


    @Override
    public String toString() {
        return "PotSmartParams{" +
                "potBurningWarnSwitch=" + potBurningWarnSwitch +
                '}';
    }
}
