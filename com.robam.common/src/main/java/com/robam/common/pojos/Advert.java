package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

public class Advert extends AbsStorePojo<Long> {

    /**
     * 推送内容
     */
    final static public int Type_Link = 1;

    /**
     * 推送菜谱
     */
    final static public int Type_Cookbook = 2;

    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField
    @JsonProperty("type")
    public int type;

    @DatabaseField
    @JsonProperty("description")
    public String desc;

    @DatabaseField
    @JsonProperty("content")
    public String content;

    @DatabaseField
    @JsonProperty("image")
    public String imgUrl;

    @Override
    public String toString() {
        return String
                .format("type:%s content:%s img:%s", type, content, imgUrl);
    }

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    static public class MobAdvert extends Advert {

    }

    static public class PadAdvert extends Advert {

        public final static String FIELD_Localtion = "localtion";

        static final public int LEFT = 0;
        static final public int MIDDLE = 1;

        @DatabaseField(columnName = FIELD_Localtion)
        public int localtion;

    }
}
