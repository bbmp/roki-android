package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/2/22.
 */

public class Images  {
    @JsonProperty("image")
    String image;
    @JsonProperty("type")
    int type;
    @JsonProperty("description")
    String description;
    @JsonProperty("content")
    String content;

    public String getContent() {
        return content;
    }
}
