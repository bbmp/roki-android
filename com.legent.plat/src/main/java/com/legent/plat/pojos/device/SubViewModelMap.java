package com.legent.plat.pojos.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

import java.io.Serializable;

/**
 * Created by 14807 on 2018/4/4.
 */

public class SubViewModelMap extends AbsPojo implements Serializable{

    @JsonProperty("subView")
    public SubViewModelMapSubView subViewModelMapSubView;

}
