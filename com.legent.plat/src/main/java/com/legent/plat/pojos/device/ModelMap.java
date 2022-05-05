package com.legent.plat.pojos.device;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

/**
 * Created by 14807 on 2018/4/4.
 */

public class ModelMap extends AbsPojo {

    @JsonProperty("mainFunc")
    public MainFunc mainFunc;

    @JsonProperty("otherFunc")
    public OtherFunc otherFunc;

    @JsonProperty("backgroundFunc")
    public BackgroundFunc backgroundFunc;

    @JsonProperty("hideFunc")
    public HideFunc hideFunc;

}
