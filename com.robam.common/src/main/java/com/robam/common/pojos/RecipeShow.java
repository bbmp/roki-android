package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;

/**
 * Created by as on 2016/8/3.
 */

public class RecipeShow extends AbsStorePojo<Long> implements Serializable{
    @DatabaseField(id = true)
    @JsonProperty("id")
    public long id;

    @JsonProperty("imgUrl")
    @DatabaseField
    public String imgUrl;

    @JsonProperty("uploadTime")
    @DatabaseField
    public String uploadTime;

    @JsonProperty("desc")
    @DatabaseField
    public String desc;

    @JsonProperty("praiseCount")
    @DatabaseField
    public String praiseCount;

    @JsonProperty("hasPraised")
    @DatabaseField
    public String hasPraised;

    @JsonProperty("ownerName")
    @DatabaseField
    public String ownerName;

    @JsonProperty("cookbookId")
    @DatabaseField
    public String cookbookId;

    @JsonProperty("cookbookName")
    @DatabaseField
    public String cookbookName;

    @JsonProperty("ownerFigureUrl")
    @DatabaseField
    public String ownerFigureUrl;

    @JsonProperty("owenerId")
    @DatabaseField
    public String owenerId;

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return ownerName;
    }
}
