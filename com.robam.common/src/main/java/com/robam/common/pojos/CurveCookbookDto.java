package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CurveCookbookDto implements Serializable {

    @JsonProperty("id")
    public int id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("deviceCategoryCode")
    public String deviceCategoryCode;

    @JsonProperty("devicePlatformCode")
    public String devicePlatformCode;

    @JsonProperty("deviceTypeCode")
    public String deviceTypeCode;

    @JsonProperty("deviceParams")
    public String deviceParams;

    @JsonProperty("temperatureCurveParams")
    public String temperatureCurveParams;

    @JsonProperty("time")
    public int time;

    @JsonProperty("curveStageParams")
    public String curveStageParams;
}
