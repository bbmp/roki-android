package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;

public class PreSubStep extends AbsStorePojo<Long> implements Serializable {
    public static final String FOREIGN_COLUMNNAME_ID = "PreStep_ID";
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    @JsonProperty("no")
    public int order;

    @DatabaseField()
    @JsonProperty("desc")
    public String desc;

    @DatabaseField
    @JsonProperty("imgUrl")
    public String imageUrl;

    @DatabaseField
    @JsonProperty("fanGear")
    public short fanLevel;

    @DatabaseField
    @JsonProperty("stoveGear")
    public short stoveLevel;

    @DatabaseField
    @JsonProperty("stepNote")
    public String stepNote;

    @DatabaseField
    @JsonProperty("videoSrc")
    public String videoSrc;

    @DatabaseField
    @JsonProperty("video")
    public String video;

    @DatabaseField
    @JsonProperty("dc")
    public String dc;

    @DatabaseField
    @JsonProperty("needPreheat")
    public boolean needPreheat;

    /**
     * 所需时间 (秒)
     */
    @DatabaseField
    @JsonProperty("needTime")
    public int needTime;

    @JsonProperty("stepVideo")
    public String stepVideo;

    @JsonProperty("showType")
    public String showType;
    /**
     * 是否备菜步骤
     */
    @JsonProperty("prepareStep")
    public boolean isPrepareStep;

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = PreSubStep.FOREIGN_COLUMNNAME_ID)
    protected PreStep preStep;

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return String.valueOf(order);
    }


    public PreStep getParent() {
        return preStep;
    }

    @Override
    public void save2db() {
        super.save2db();
    }

}
