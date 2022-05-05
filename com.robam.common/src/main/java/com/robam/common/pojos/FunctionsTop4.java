package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

/**
 * Created by Administrator on 2019/8/21.
 */

public class FunctionsTop4 extends AbsPojo{

    @JsonProperty("functionCode")
    public String functionCode;

    @JsonProperty("number")
    public int number;




}
