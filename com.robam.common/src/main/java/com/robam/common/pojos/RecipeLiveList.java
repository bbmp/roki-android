package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

/**
 * Created by as on 2016/8/3.
 */

public class RecipeLiveList extends AbsStorePojo<Long> {
    @DatabaseField(id = true, columnName = "id")
    @JsonProperty("ckCode")
    public long id;

    @DatabaseField
    @JsonProperty("name")
    public String name;

    @DatabaseField
    @JsonProperty("uri")
    public String uri;

    @DatabaseField
    @JsonProperty("desc")
    public String desc;

    @DatabaseField
    @JsonProperty("imgUrl")
    public String imgUrl;

    @DatabaseField
    @JsonProperty("videoId")
    public String videoId;

    @DatabaseField
    @JsonProperty("acessToken")
    public String acessToken;

    @Override
    public Long getID() {

        return id;
    }

    @Override
    public String getName() {
        return name;
    }


}
