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
 * 菜谱食材
 *
 * @author sylar
 */
public class Materials extends AbsStorePojo<Long> implements Serializable {

    @DatabaseField(generatedId = true)
    private long id;

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    @ForeignCollectionField()
    private ForeignCollection<Material> materialList;

    @JsonProperty("main")
    private List<Material> main;
    @JsonProperty("accessory")
    private List<Material> accessory;

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return null;
    }


    public List<Material> getMain() {
        if (materialList != null && materialList.size() > 0) {
            List<Material> list = Material.getMaterialList(id, true);
            if(list==null)
                list= Lists.newArrayList();
            return list;
        } else {
            if(main==null)
                main= Lists.newArrayList();
            return main;
        }
    }

    public List<Material> getAccessory() {
        if (materialList != null && materialList.size() > 0) {
            List<Material> list = Material.getMaterialList(id, false);
            if(list==null)
                list= Lists.newArrayList();
            return list;
        } else {
            if(accessory==null)
                accessory= Lists.newArrayList();
            return accessory;
        }
    }

    @Override
    public void save2db() {
        super.save2db();

        if (main != null) {
            for (Material mm : main) {
                mm.materials = this;
                mm.isMain = true;
                mm.save2db();
            }
        }

        if (accessory != null) {
            for (Material am : accessory) {
                am.materials = this;
                am.isMain = false;
                am.save2db();
            }
        }

        DaoHelper.update(this);
        DaoHelper.refresh(this);
    }

    @Override
    public void delete(long id) {
        try {
            DaoHelper.deleteWhereEq(Material.class, Material.FIELD_NAME_Materials_ID, this.id);
            super.delete(id);
        } catch (Exception e) {

        }
    }


}
