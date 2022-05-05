package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dell on 2018/6/12.
 */

public class StyleInfo {

    @JsonProperty("styleId")
    public int styleId;

    @JsonProperty("styleName")
    public String styleName;

}
