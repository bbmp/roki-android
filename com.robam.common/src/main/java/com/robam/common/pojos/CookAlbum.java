package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

/**
 * 烧菜图片分享圈
 *
 * @author sylar
 */
public class CookAlbum extends AbsStorePojo<Long> {

    public final static String Col_ID = "id";
    public final static String COL_praiseCount = "praiseCount";

    @DatabaseField(id = true)
    @JsonProperty("id")
    public long id;

    @DatabaseField
    @JsonProperty("cookbookId")
    public long bookId;

    @DatabaseField
    @JsonProperty("ownerId")
    public long ownerId;

    @DatabaseField
    @JsonProperty("imgUrl")
    public String imgUrl;

    @DatabaseField
    @JsonProperty("desc")
    public String desc;

    @DatabaseField
    @JsonProperty("ownerName")
    public String ownerName;

    @DatabaseField(columnName = COL_praiseCount)
    @JsonProperty("praiseCount")
    public int praiseCount;

    @DatabaseField
    @JsonProperty("hasPraised")
    public boolean hasPraised;

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return desc;
    }


}
