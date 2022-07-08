package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yinwei on 2016/12/29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TodayDrinking  {
    @JsonProperty("timeType")
    public String timeType;

    @JsonProperty("time")
    public String time;

    @JsonProperty("volume")
    public String volume;

    @JsonProperty("ranking")
    public int ranking;

    @Override
    public String toString() {
        return "TodayDrinking{" +
                "timeType='" + timeType + '\'' +
                ", time='" + time + '\'' +
                ", volume='" + volume + '\'' +
                ", ranking=" + ranking +
                '}';
    }
}
