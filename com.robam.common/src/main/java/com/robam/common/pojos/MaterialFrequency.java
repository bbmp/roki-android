package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

/**
 * Created by sylar on 15/6/24.
 */
public class MaterialFrequency extends AbsStorePojo<Long> {

    @DatabaseField(id = true)
    @JsonProperty("id")
    public long id;

    @DatabaseField
    @JsonProperty("name")
    public String name;

    @DatabaseField
    @JsonProperty
    public int frequency;


    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
