package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;

/**
 * Created by as on 2017-02-17.
 */

public class RuleStream extends AbsStorePojo<Long> implements Serializable {
    public final static String COLUMN_RuleStream_ID = "ruleStream_id";
    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    @JsonProperty("ruleItem")
    public String ruleItem;
    /**
     * 规则Code
     */
    @DatabaseField
    @JsonProperty("ruleCode")
    public String ruleCode;

    /**
     * 规则类型
     */
    @DatabaseField
    @JsonProperty("ruleType")
    public String ruleType;

    /**
     * 规则值
     */
    @DatabaseField
    @JsonProperty("ruleValue")
    public String ruleValue;


    @DatabaseField
    @JsonProperty("effectTime")
    public String effectTime;

    @DatabaseField
    @JsonProperty("durationTime")
    public String durationTime;

    @DatabaseField(foreign = true, columnName = COLUMN_RuleStream_ID)
    public Rules ruleStreams;

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return null;
    }

    public Rules getParent() {
        return ruleStreams;
    }
}
