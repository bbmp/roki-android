package com.robam.roki.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.legent.pojos.AbsKeyPojo;

import java.util.List;

/**
 * Created by sylar on 15/8/5.
 */
public class PCR extends AbsKeyPojo<Integer> {
    @JsonProperty("PCR_ID")
    public int id;

    @JsonProperty("PCR_NAME")
    public String name;

    @JsonProperty("PCR_PARENT")
    public int parentId;

    public List<PCR> children = Lists.newArrayList();


    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name;
    }
}
