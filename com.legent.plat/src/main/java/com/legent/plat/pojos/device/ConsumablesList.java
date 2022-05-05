package com.legent.plat.pojos.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

public class ConsumablesList extends AbsPojo {
    @JsonProperty("orderId")
    public int orderId;
    @JsonProperty("accessoryId")
    public long accessoryId;
    @JsonProperty("accessoryName")
    public String accessoryName;
    @JsonProperty("spec")
    public String spec;
    @JsonProperty("imgUrl")
    public String imgUrl;
    @JsonProperty("remarks")
    public String remarks;
    @JsonProperty("deviceType")
    public String deviceType;
    @JsonProperty("deviceCategory")
    public String deviceCategory;
    @JsonProperty("popularRecommend")
    public boolean popularRecommend;
    @JsonProperty("accessoryRecommend")
    public boolean accessoryRecommend;

}
