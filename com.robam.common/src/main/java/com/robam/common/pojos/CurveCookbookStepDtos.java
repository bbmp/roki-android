package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CurveCookbookStepDtos implements Serializable {

    @JsonProperty("id")
    public int id;

    @JsonProperty("no")
    public int no;

    @JsonProperty("description")
    public String description;

    @JsonProperty("voiceDescription")
    public String voiceDescription;

    @JsonProperty("descType")
    public String descType;

    @JsonProperty("image")
    public String image;

    @JsonProperty("video")
    public String video;

    @JsonProperty("showType")
    public String showType;

    @JsonProperty("curveCookbook")
    public int curveCookbook;

    @JsonProperty("time")
    public int time;

    @JsonProperty("curveStageParams")
    public String curveStageParams;

}
