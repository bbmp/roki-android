package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;

/**
 * Created by zhoudingjun on 2016/7/16.
 */
public class Dc extends AbsStorePojo<Long> implements Serializable{
    public final static String COLUMN_RECIPE_ID = "recipe_id";
   // public final static String COLUMN_COOKBOOKS_ID = "cookBooks_id";
    @DatabaseField(generatedId = true)
    private long id;


    @DatabaseField
    @JsonProperty("dc")
    public String dc;

    @DatabaseField(foreign = true, columnName = COLUMN_RECIPE_ID)
    public Recipe cookbook;

  /*  @DatabaseField(foreign = true, columnName = COLUMN_COOKBOOKS_ID)
    public Cookbooks cookbooks;*/

    @Override
    public String getName() {
        return dc;
    }

    @Override
    public Long getID() {
        return id;
    }


    public String getDc() {
        return dc;
    }
}
