package com.legent.plat.pojos.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

public class PayloadBean extends AbsPojo {
    @JsonProperty("lastWaterCost")
    public double lastWaterCost;
    @JsonProperty("lastPowerCost")
    public double lastPowerCost;
    @JsonProperty("totalWaterUsed")
    public double totalWaterUsed;
    @JsonProperty("totalWaterSaved")
    public double totalWaterSaved;
}
