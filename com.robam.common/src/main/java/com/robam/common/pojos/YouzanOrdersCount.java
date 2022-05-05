package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/7/4.
 */

public class YouzanOrdersCount {

    @JsonProperty("status")
    public String status;

    @JsonProperty("count")
    public int count;

}
