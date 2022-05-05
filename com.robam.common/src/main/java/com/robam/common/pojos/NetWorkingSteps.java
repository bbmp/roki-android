package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;

public class NetWorkingSteps extends AbsStorePojo<Long> implements Serializable {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    @JsonProperty("step")
    public String step;

    @DatabaseField
    @JsonProperty("netImgUrl")
    public String netImgUrl;

    @DatabaseField
    @JsonProperty("netRichText")
    public String netRichText;

    @DatabaseField
    @JsonProperty("buttonDesc")
    public String buttonDesc;

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return "dc";
    }
}
