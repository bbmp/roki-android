package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dell on 2018/6/12.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayLoad {
    @JsonProperty("id")
    public int id;

    @JsonProperty("image")
    public String image;

    @JsonProperty("keyword")
    public String keyWord;

    @JsonProperty("name")
    public String name;

    @JsonProperty("time")
    public int totalTime;

    @JsonProperty("desc")
    public String desc;

    @JsonProperty("tag")
    public String tag;


}
