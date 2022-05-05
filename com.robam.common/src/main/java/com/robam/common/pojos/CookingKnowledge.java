package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;

/**
 * Created by zhoudingjun on 2017/6/9.
 */

public class CookingKnowledge extends AbsStorePojo<Long> implements Serializable{

    @DatabaseField(generatedId = true)
    @JsonProperty("id")
    public long id;

    /**
     * 图片封面url
     */
    @DatabaseField()
    @JsonProperty("pictureCoverUrl")
    public String pictureCoverUrl;
    /**
     * 视频封面url
     */
    @DatabaseField()
    @JsonProperty("videoCoverUrl")
    public String videoCoverUrl;
    /**
     * 视频url
     */
    @DatabaseField()
    @JsonProperty("videoUrl")
    public String videoUrl;

    /**
     * 0-图片；1-视频
     */
    @DatabaseField()
    @JsonProperty("contentType")
    public int contentType;

    /**
     * 视频
     */
    @DatabaseField()
    @JsonProperty("videoId")
    public String videoId;
    /**
     * 头像url
     */
    @DatabaseField()
    @JsonProperty("portraitUrl")
    public String portraitUrl;
    /**
     * 标题
     */
    @DatabaseField()
    @JsonProperty("title")
    public String title;

    /**
     * 标签
     */
    @DatabaseField()
    @JsonProperty("lable")
    public String lable;

    /**
     * 获取唯一标识符
     *
     * @return
     */
    @Override
    public Long getID() {
        return null;
    }

    /**
     * 获取名称
     *
     * @return
     */
    @Override
    public String getName() {
        return null;
    }

}
