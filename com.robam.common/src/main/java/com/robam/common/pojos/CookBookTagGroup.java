package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;
import java.util.List;



/**
 * Created by Administrator on 2017/4/9.
 */

public class CookBookTagGroup extends AbsStorePojo<Long> implements Serializable {
    public final static String COLUMN_recipeCook = "recipeCook";

    public final static String COLUMN_recipe3rdCook = "recipe3rdCook";


   // @DatabaseField(id = true)
    @JsonProperty("id")
    public long id;

   // @DatabaseField()
    @JsonProperty("name")
    public String name;

   // @DatabaseField()
    @JsonProperty("type")
    public int type;

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

   /* @ForeignCollectionField
    private ForeignCollection<CookBookTag> db_tags;*/

    @JsonProperty("cookbookTags")
    public List<CookBookTag> js_tags= Lists.newArrayList();

   /* @DatabaseField(foreign = true, columnName = COLUMN_recipeCook)
    public Recipe cookbook;

    @DatabaseField(foreign = true, columnName = COLUMN_recipe3rdCook)
    public Recipe3rd cookbook3rd;*/
    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

   /* public List<CookBookTag> getTags() {
        *//*List<Tag> list = Lists.newArrayList();
        if (db_tags != null&&db_tags.size()>0) {
			list.addAll(db_tags);
		}
		return list;*//*
        if (db_tags != null && db_tags.size() > 0) {
            js_tags = Lists.newArrayList(db_tags);
        }
        if (js_tags == null) {
            js_tags = Lists.newArrayList();
        }
        return js_tags;
    }*/

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


}
