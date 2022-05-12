package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.legent.pojos.AbsStorePojo;
import com.robam.common.services.DaoService;

import java.util.List;

public class Group extends AbsStorePojo<Long> {
    /* public final static String COLUMN_recipeCook = "recipeCook";

     public final static String COLUMN_recipe3rdCook = "recipe3rdCook";*/
    static final String FIELD_TYPE = "type";
    static final int HOME_GROUP_TYPE = 2;

    @DatabaseField(id = true)
    @JsonProperty("id")
    public long id;

    @DatabaseField()
    @JsonProperty("name")
    public String name;

    @DatabaseField(columnName = FIELD_TYPE)
    @JsonProperty("type")
    public int type;

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    @ForeignCollectionField
    private ForeignCollection<Tag> db_tags;

    @JsonProperty("cookbookTags")
    private List<Tag> cookbookTags;

  /*  @DatabaseField(foreign = true, columnName = COLUMN_recipeCook)
    public Recipe cookbook;

    @DatabaseField(foreign = true, columnName = COLUMN_recipe3rdCook)
    public Recipe3rd cookbook3rd;*/
    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    public List<Tag> getTags() {
        /*List<Tag> list = Lists.newArrayList();
        if (db_tags != null&&db_tags.size()>0) {
			list.addAll(db_tags);
		}
		return list;*/
        if (db_tags != null && db_tags.size() > 0) {
            cookbookTags = Lists.newArrayList(db_tags);
        }
        if (cookbookTags == null) {
            cookbookTags = Lists.newArrayList();
        }
        return cookbookTags;
    }

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


    @Override
    public void save2db() {
        super.save2db();

        if (cookbookTags != null) {
            for (Tag tag : cookbookTags) {
                tag.group = this;
                tag.save2db();
            }
        }
    }

    @Override
    public void delete(long id) {
        if (db_tags != null) {
            for (Tag tag : db_tags) {
                tag.delete(0);
            }
        }
        super.delete(id);
    }


    static public Group getHomeGroup() {
        try {
            Dao<Group, Long> dao = DaoService.getInstance().getDao(Group.class);
            QueryBuilder<Group, Long> qb = dao.queryBuilder();
            qb.where().eq(FIELD_TYPE, HOME_GROUP_TYPE);
            return qb.queryForFirst();
        } catch (Exception e) {
            return null;
        }
    }

    static public List<Group> getGroupsWithoutHome() {
        try {
            Dao<Group, Long> dao = DaoService.getInstance().getDao(Group.class);
            QueryBuilder<Group, Long> qb = dao.queryBuilder();
            qb.where().ne(FIELD_TYPE, HOME_GROUP_TYPE);
            List<Group> list = qb.query();
            return list;
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

}
