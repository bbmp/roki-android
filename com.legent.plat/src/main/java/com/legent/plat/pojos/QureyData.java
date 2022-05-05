package com.legent.plat.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/1/15.
 * PS: 查询回来的数据.
 */
public class QureyData extends AbsPojo {

    @JsonProperty("deviceGuid")
    public String deviceGuid;

    @JsonProperty("mode")
    public String mode;

    @JsonProperty("workTime")
    public String workTime;

    @JsonProperty("orderTime")
    public String orderTime;
}
