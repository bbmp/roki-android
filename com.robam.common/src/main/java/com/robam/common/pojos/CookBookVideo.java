package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author r210190
 * des 菜谱详情视频
 */
public class CookBookVideo {

    @JsonProperty("viewCount")
    public long viewCount;

    @JsonProperty("video")
    public String video;

    @JsonProperty("showType")
    public String showType ;
}
