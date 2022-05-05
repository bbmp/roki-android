package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dell on 2018/6/12.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredient {
    @JsonProperty("dosage")
    public String dosage;

    @JsonProperty("unit")
    public String unit;

    @JsonProperty("name")
    public String name;

    @JsonProperty("id")
    public int id;

}
