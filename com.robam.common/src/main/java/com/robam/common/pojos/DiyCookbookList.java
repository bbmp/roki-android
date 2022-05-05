package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/9/2.
 */

public class DiyCookbookList extends AbsPojo implements Serializable {

    @JsonProperty("id")
    public int id;

    @JsonProperty("deviceType")
    public String deviceType;

    @JsonProperty("userId")
    public String userId;

    @JsonProperty("name")
    public String name;

    @JsonProperty("modeCode")
    public String modeCode;

    @JsonProperty("temp")
    public String temp;

    @JsonProperty("minute")
    public String minute;


    @JsonProperty("hasRotate")
    public int hasRotate;

    @JsonProperty("openRotate")
    public int openRotate;

    @JsonProperty("cookbookDesc")
    public String cookbookDesc;


    @JsonProperty("tempDown")
    public String tempDown;



}
