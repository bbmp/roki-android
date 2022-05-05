package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author r210190
 * des 菜谱所需设备
 */
public class Categories implements Serializable {
    @JsonProperty("dc")
    public String dc;

    @JsonProperty("name")
    public String name;

    @JsonProperty("imgUrl")
    public String imgUrl;

    @JsonProperty("desc")
    public String desc;

    @JsonProperty("alterNativeCategories")
    public List<Categories> alterNativeCategories;


}
