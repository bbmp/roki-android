package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by zhoudingjun on 2017/6/8.
 */

public class KnowledgeGroup implements Serializable {

    @DatabaseField(generatedId = true)
    @JsonProperty("id")
    public long id;
    /**
     * 类型的Code
     */
    @DatabaseField
    @JsonProperty("typeCode")
    public String typeCode;
    /**
     *类型名称
     */
    @DatabaseField
    @JsonProperty("typeName")
    public String typeName;
    /**
     *所处位置
     */
    @DatabaseField
    @JsonProperty("posiziton")
    public int posiziton;

    @DatabaseField
    @JsonProperty("activities")
    public Activities activities;

}
