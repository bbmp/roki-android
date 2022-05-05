package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

/**
 * Created by 14807 on 2018/8/24.
 */
public class SeriesInfoCell extends AbsPojo {


    @JsonProperty("seriesId")
    public String seriesId;//栏目ID

    @JsonProperty("courseId")
    public String courseId;//课程ID

    @JsonProperty("courseTitle")
    public String courseTitle;//课程标题

    @JsonProperty("subTitle")
    public String subTitle;//课程ID

    @JsonProperty("courseImage")
    public String courseImage;//集数图片

    @JsonProperty("episode")
    public int currentEpisode;//当前第几集

    @JsonProperty("orderNo")
    public int orderNo;//排序

    @JsonProperty("videoWatchcount")
    public int videoWatchcount;//观看数

    @JsonProperty("type")
    public String type;//类型，列：3-美食

    @JsonProperty("playUrl")
    public String playUrl;//视频URL

    @JsonProperty("updateTime")
    public String updateTime;//更新时间



}
