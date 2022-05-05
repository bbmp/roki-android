package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2018/12/24.
 * PS: Not easy to write code, please indicate.
 */
public class MallManagement extends AbsPojo {

    @JsonProperty("id")
    public long id;

    @JsonProperty("portraitUrl")
    public String portraitUrl;

    @JsonProperty("title")
    public String title;

    @JsonProperty("url")
    public String url;

    @JsonProperty("isShow")
    public int isShow;


}
