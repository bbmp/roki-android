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

public class CookStepTip extends AbsStorePojo<Long> implements Serializable {
    public final static String COLUMN_STEP_ID = "step_id";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    @JsonProperty("time")
    public int time;

    /**
     * 语音提醒文本
     */
    @DatabaseField
    @JsonProperty("prompt")
    public String prompt;

    @ForeignCollectionField()
    private ForeignCollection<CookStepTipMaterial> db_materials;

    public List<CookStepTipMaterial> getTipMaterials() {
        if (db_materials != null && db_materials.size() > 0)
            return Lists.newArrayList(db_materials);
        if(js_materials==null)
            js_materials=Lists.newArrayList();
        return js_materials;
    }

    @JsonProperty("materials")
    private List<CookStepTipMaterial> js_materials;

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    @DatabaseField(foreign = true, columnName = COLUMN_STEP_ID)
    protected CookStep cookStep;

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return prompt;
    }

    public CookStep getParent() {
        return cookStep;
    }


    @Override
    public void save2db() {
        super.save2db();
        if (js_materials != null) {
            for (CookStepTipMaterial ctm : js_materials) {
                ctm.tip = this;
                ctm.save2db();
            }
        }
        DaoHelper.update(this);
        DaoHelper.refresh(this);
    }

    @Override
    public void delete(long id) {
        try {
            DaoHelper.deleteWhereEq(CookStepTipMaterial.class, CookStepTipMaterial.TIP_ID, this.id);
            super.delete(id);
        } catch (Exception e) {

        }
    }


}
