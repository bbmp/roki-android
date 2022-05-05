package com.legent.plat.pojos.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

/**
 * Created by Administrator on 2019/9/6.
 */

public class Payload extends AbsPojo {

    @JsonProperty("duration")
    public int duration;

    @JsonProperty("enable")
    public boolean enable;

    @JsonProperty("cmd")
    public int cmd;

    @JsonProperty("category")
    public String category;

    @JsonProperty("gear")
    public int gear;


}
