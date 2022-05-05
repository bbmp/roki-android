package com.robam.common.pojos.dictionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsKeyPojo;
import com.legent.utils.api.ResourcesUtils;
import com.robam.common.pojos.device.Stove.Stove;

/**
 * Created by sylar on 15/6/3.
 */
public class StoveAlarm extends AbsKeyPojo<Short> {

    @JsonProperty("id")
    protected short id;

    @JsonProperty("name")
    protected String name;

    public Stove.StoveHead src;


    @Override
    public Short getID() {
        return id;
    }

    @Override
    public String getName() {
        return ResourcesUtils.getStringOrFromRes(name);
    }
}
