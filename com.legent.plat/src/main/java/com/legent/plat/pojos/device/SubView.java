package com.legent.plat.pojos.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

import java.io.Serializable;

/**
 * Created by 14807 on 2018/4/4.
 */

public class SubView extends AbsPojo implements Serializable{

    @JsonProperty("viewBackgroundImg")
    public String viewBackgroundImg;

    @JsonProperty("viewName")
    public String viewName;

    @JsonProperty("title")
    public String title;

    @JsonProperty("text")
    public String text;

    @JsonProperty("modelMap")
    public SubViewModelMap subViewModelMap;
}
