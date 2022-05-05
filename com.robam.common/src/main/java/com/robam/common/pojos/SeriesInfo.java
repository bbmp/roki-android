package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

import java.io.Serializable;

/**
 * Created by 14807 on 2018/8/22.
 */

public class SeriesInfo extends AbsPojo implements Serializable{

    @JsonProperty("seiresId")
    public String seiresId;//栏目ID

    @JsonProperty("seriesName")
    public String seriesName;//栏目名称

    @JsonProperty("seriesTitle")
    public String seriesTitle;//栏目标题

    @JsonProperty("seriesImage")
    public String seriesImage;//栏目图片

    @JsonProperty("album")
    public String album;//栏目专题

    @JsonProperty("albumLogo")
    public String albumLogo;//栏目专题LOGO

    @JsonProperty("type")
    public String type;//栏目类型，3-美食

    @JsonProperty("episode")
    public int episode;//总集数

    @JsonProperty("play")
    public long play;//总播放数

    @JsonProperty("source")
    public int source;//资源来源,1-掌厨


}
