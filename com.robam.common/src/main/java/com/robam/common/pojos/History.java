package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by 14807 on 2018/4/18.
 */

public class History {

    @JsonProperty("score")
    public int score;

    @JsonProperty("value")
    public String value;
}
