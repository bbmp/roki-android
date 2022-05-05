package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;
import java.util.List;

public class CookbookPlatforms extends AbsStorePojo<Long> implements Serializable {

    @DatabaseField(generatedId = true)
    @JsonProperty("id")
    public long id;

    @JsonProperty("cookbookId")
    public long cookbookId;

    @JsonProperty("platformCode")
    public String platformCode;

    @JsonProperty("createDate")
    public long createDate;

    @JsonProperty("updateDate")
    public long updateDate;


    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return platformCode;
    }

    public String getPlatformCode(){

        return platformCode;
    }
}
