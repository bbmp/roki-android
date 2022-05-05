package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Dell on 2018/6/12.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayLoadKuF {

    @JsonProperty("image")
    public String imageUrl;

    @JsonProperty("ingredient")
    public List<Accessories> ingredients;

    @JsonProperty("accessories")
    public List<Accessories> accessories;

    @JsonProperty("updateTime")
    public String updataTime;

    @JsonProperty("maxGrade")
    public int maxGrade;

    @JsonProperty("cookingSteps")
    public List<KuCookingSteps> kuCookingStepses;

    @JsonProperty("prepareSteps")
    public List<KuPrepareSteps> kuPrepareStepses;

    @JsonProperty("name")
    public String name;

    @JsonProperty("id")
    public int id;


    @JsonProperty("tag")
    public String tag;



    @JsonProperty("time")
    public int totalTime;

    @JsonProperty("keyword")
    public String keyword;

    @JsonProperty("desc")
    public String desc;


}
