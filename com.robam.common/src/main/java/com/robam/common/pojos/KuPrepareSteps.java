package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dell on 2018/6/12.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KuPrepareSteps {

    @JsonProperty("image")
    public String image;

    @JsonProperty("desc")
    public String description;

}
