package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;

/**
 * Created by sylar on 15/6/26.
 */
public class CookStepTipMaterial extends AbsStorePojo<Long> implements Serializable{

    public final static String TIP_ID = "tip_id";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    @JsonProperty("name")
    public String name;

    @DatabaseField
    @JsonProperty("imgUrl")
    public String imgUrl;

    @DatabaseField
    @JsonProperty("weight")
    public String weight;

    @DatabaseField
    @JsonProperty("unit")
    public String unit;

    @DatabaseField(foreign = true, columnName = TIP_ID)
    protected CookStepTip tip;

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------


    public CookStepTip getParent() {
        return tip;
    }
}
