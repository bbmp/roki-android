package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Administrator on 2017/9/1.
 */

public class CookStepDetails {

    public CookStepDetails() {
    }

    @DatabaseField
    @JsonProperty("scheduledTime")
    public int scheduledTime;

    @DatabaseField
    @JsonProperty("stepNo")
    public int stepNo;

    @DatabaseField
    @JsonProperty("actualTime")
    public int actualTime;

    @DatabaseField
    @JsonProperty("actionType")
    public String actionType;

}
