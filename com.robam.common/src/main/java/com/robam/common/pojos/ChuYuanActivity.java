package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dell on 2018/2/6.
 */

public class ChuYuanActivity {
    @JsonProperty("id")
    public long id;

    @JsonProperty("pictureCoverUrl")
    public String pictureCoverUrl;

    @JsonProperty("address")
    public String address;

    @JsonProperty("videoCoverUrl")
    public String videoCoverUrl;

    @JsonProperty("contentType")
    public int contentType;

    @JsonProperty("isTop")
    public String isTop;

    @JsonProperty("isHot")
    public int isHot;

    @JsonProperty("title")
    public String title;

    @JsonProperty("videoId")
    public String videoId;
}
