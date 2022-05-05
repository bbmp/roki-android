package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.pojos.AbsStorePojo;
import com.legent.utils.LogUtils;
import com.robam.common.paramCode;

import java.io.Serializable;
import java.util.List;

/**
 * 烧菜步骤
 *
 * @author zhoudingjun
 */
public class PlatformCode extends AbsStorePojo<Long> implements Serializable {

    public final static String COLUMN_PlatformCode_ID = "platformCode_id";

    @DatabaseField(generatedId = true)
    private long id;


    @DatabaseField
    @JsonProperty("deviceCategory")
    public String deviceCategory;

    /**
     * 技术平台
     */
    @DatabaseField
    @JsonProperty("platCode")
    public String platCode;

    @ForeignCollectionField()
    private ForeignCollection<paramCode> db_paramCodes;

    public List<paramCode> getJs_paramCodes() {
        if (db_paramCodes != null && db_paramCodes.size() > 0) {
            js_paramCodes = Lists.newArrayList(db_paramCodes);
            if (Plat.DEBUG)
                LogUtils.i("20170614", "js_paramCodes::" + js_paramCodes.toString());
            return js_paramCodes;
        }

        if (js_paramCodes==null){
            js_paramCodes=Lists.newArrayList();
        }
        return js_paramCodes;
    }

    /**
     * 技术平台参数
     */
    @JsonProperty("params")
    public List<paramCode> js_paramCodes;

    /**
     * 获取设备平台参数
     */
    @ForeignCollectionField()
    private ForeignCollection<Rules> db_rule;

    public List<Rules> getjs_Rule() {
        if (db_rule != null && db_rule.size() > 0){
            return Lists.newArrayList(db_rule);
        }
        if (js_rule == null)
            js_rule = Lists.newArrayList();
        return js_rule;
    }

    @JsonProperty("rules")
    public List<Rules> js_rule;

    public PlatformCode() {
    }

    @DatabaseField(foreign = true, columnName = COLUMN_PlatformCode_ID)
    public CookStep cookStep;

    public String getPlatCode() {
        return platCode;
    }


    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    @Override
    public String getName() {
        return platCode;
    }

    @Override
    public Long getID() {
        return id;
    }

    public CookStep getParent() {
        return cookStep;
    }

    public String getDeviceCategory() {
        return deviceCategory;
    }

    @Override
    public void save2db() {
        super.save2db();
        if (js_rule != null) {
            for (Rules streams : js_rule) {
                streams.platformCode = this;
                streams.save2db();
            }
        }
        if (js_paramCodes != null) {
            for (paramCode paramCode : js_paramCodes) {
                paramCode.platformCode = this;
                paramCode.save2db();
            }
        }

        DaoHelper.update(this);
        DaoHelper.refresh(this);
    }

    @Override
    public void delete(long id) {
        try {
            if (db_rule != null) {
                for (Rules streams : db_rule) {
                    streams.delete(0);
                }
            }
            DaoHelper.deleteWhereEq(paramCode.class, paramCode.PARACODE_ID, this.id);
            super.delete(id);
        } catch (Exception e) {

        }
    }
}