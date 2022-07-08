package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CookingCurveMarkStepList extends AbsPojo implements Serializable {

    @JsonProperty("curveStepId")
    public String curveStepId;

    @JsonProperty("curveCookbookId")
    public String curveCookbookId;

    @JsonProperty("no")
    public Object no;

    @JsonProperty("markName")
    public String markName;

    @JsonProperty("markTime")
    public String markTime;

    @JsonProperty("markTemp")
    public String markTemp;

    @JsonProperty("curveStageParams")
    public String curveStageParams;

    @JsonProperty("imageUrl")
    public String imageUrl;

    @JsonProperty("voiceUrl")
    public String voiceUrl;

    @JsonProperty("videoUrl")
    public String videoUrl;

    @JsonProperty("description")
    public String description;


}
