package com.legent.plat.pojos.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 14807 on 2018/4/4.
 */

public class SubViewModelMapSubView extends AbsPojo implements Serializable{

    @JsonProperty("id")
    public long id;

    @JsonProperty("deviceConfigurationFunctions")
    public List<DeviceConfigurationFunctions> deviceConfigurationFunctions;

    @JsonProperty("modelCode")
    public String modelCode;

    @JsonProperty("modelName")
    public String modelName;

    @JsonProperty("modelBkimg")
    public String modelBkimg;

}
