package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dell on 2018/6/12.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KuCookingSteps {

    @JsonProperty("id")
    public int id;

    @JsonProperty("action")
    public int action;

    @JsonProperty("time")
    public int time;

    @JsonProperty("desc")
    public String description;

}
