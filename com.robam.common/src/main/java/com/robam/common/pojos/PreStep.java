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
 * 备菜步骤
 *
 * @author sylar
 */
public class PreStep extends AbsStorePojo<Long> implements Serializable {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    @JsonProperty("img")
    public String imageUrl;

    @DatabaseField
    @JsonProperty("desc")
    public String desc;

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------
    /*外部关联表	@JsonProperty("steps")
                     private List<PreSubStep> js_steps;
	                 解析出数据来之后，传给
	                 @ForeignCollectionField
	private ForeignCollection<PreSubStep> steps;
	                 存放到本地（ForeignCollectionField表示表）

	*/
    @ForeignCollectionField
    private ForeignCollection<PreSubStep> steps;

    public List<PreSubStep> getPreSubSteps() {
        if (steps != null && steps.size() > 0)
            return Lists.newArrayList(steps);
        if(js_steps==null)
            js_steps=Lists.newArrayList();
        return js_steps;

    }

    @JsonProperty("steps")
    private List<PreSubStep> js_steps;

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return desc;
    }


    @Override
    public void save2db() {
        super.save2db();

        if (js_steps != null) {
            for (PreSubStep pst : js_steps) {
                pst.preStep = this;
                pst.save2db();
            }
        }
        DaoHelper.update(this);
        DaoHelper.refresh(this);
    }

    @Override
    public void delete(long id) {
        try {
            DaoHelper.deleteWhereEq(PreSubStep.class, PreSubStep.FOREIGN_COLUMNNAME_ID, this.id);
            super.delete(id);
        } catch (Exception e) {
        }
    }

}