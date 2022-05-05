package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dell on 2019/1/29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckPayLoad {
    @JsonProperty("explain")
    public String explain;

    @JsonProperty("version")
    public String version;

    @JsonProperty("newVersion")
    public String newVersion;
}
