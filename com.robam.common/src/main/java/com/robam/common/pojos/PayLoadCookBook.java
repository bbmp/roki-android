package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PayLoadCookBook extends AbsRecipe implements Serializable {

    @JsonProperty("curveCookbookDto")
    public CurveCookbookDto curveCookbookDto;

    @JsonProperty("curveCookbookPrepareStepDtos")
    public List<CurveCookbookStepDtos> curveCookbookPrepareStepDtos;

    @JsonProperty("curveCookbookStepDtos")
    public List<CurveCookbookStepDtos> curveCookbookStepDtos;

}
