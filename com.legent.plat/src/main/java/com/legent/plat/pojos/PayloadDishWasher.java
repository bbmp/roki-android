package com.legent.plat.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

public class PayloadDishWasher extends AbsPojo {
    @JsonProperty("path")
    public String path;
    @JsonProperty("appId")
    public String appId;
    @JsonProperty("userName")
    public String userName;
}
