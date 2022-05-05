package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;

/**
 * Created by rent on 2016/8/22.
 */

public class RecipeConsultation extends AbsStorePojo<Long> implements Serializable {
    @DatabaseField(id = true)
    @JsonProperty("id")
    public long id;

    @JsonProperty("title")
    @DatabaseField
    public String title;

    @JsonProperty("contentUrl")
    @DatabaseField
    public String contentUrl;

    @JsonProperty("imageUrl")
    @DatabaseField
    public String imageUrl;

    @JsonProperty("insertTime")
    @DatabaseField
    public String insertTime;

    @JsonProperty("topFlag")
    @DatabaseField
    public String topFlag;

    @JsonProperty("sortNo")
    @DatabaseField
    public String sortNo;

    @JsonProperty("description")
    @DatabaseField
    public String description;

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return title;
    }
}
