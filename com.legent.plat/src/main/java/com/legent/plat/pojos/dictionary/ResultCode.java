package com.legent.plat.pojos.dictionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsKeyPojo;
import com.legent.utils.api.ResourcesUtils;

/**
 * Created by sylar on 15/6/3.
 */
public class ResultCode extends AbsKeyPojo<Integer> {

    @JsonProperty
    protected int id;

    @JsonProperty
    protected String name;

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public String getName() {
        return ResourcesUtils.getStringOrFromRes(name);
    }

}
