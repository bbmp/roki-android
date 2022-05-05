package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.legent.dao.DaoHelper;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by as on 2017-02-17.
 */

public class Rules extends AbsStorePojo<Long> implements Serializable {
    public final static String COLUMN_RuleStreams_ID = "ruleStreams_id";
    @DatabaseField(generatedId = true)
    private long id;

    public Rules(){

    }

    @ForeignCollectionField
    private ForeignCollection<RuleStream> db_rule;


    public List<RuleStream> getRuleStreams() {
        if (db_rule != null && db_rule.size() > 0)
            return Lists.newArrayList(db_rule);
        if (js_rules == null)
            js_rules = Lists.newArrayList();
        return js_rules;
    }

    @JsonProperty("ruleItem")
    protected String ruleItem;

    @JsonProperty("ruleStream")
    protected List<RuleStream> js_rules;

    @DatabaseField(foreign = true, columnName = COLUMN_RuleStreams_ID)
    public PlatformCode platformCode;

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return null;
    }

    public PlatformCode getParent() {
        return platformCode;
    }

    @Override
    public void save2db() {
        super.save2db();
        if (js_rules != null) {
            for (RuleStream ruleStream : js_rules) {
                ruleStream.ruleStreams = this;
                ruleStream.save2db();
            }
        }
    }

    @Override
    public void delete(long id) {
        try {
            DaoHelper.deleteWhereEq(RuleStream.class, RuleStream.COLUMN_RuleStream_ID, this.id);
            super.delete(id);
        } catch (Exception e) {
        }
    }
}
