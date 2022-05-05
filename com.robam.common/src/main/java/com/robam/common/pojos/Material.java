package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import com.j256.ormlite.field.DatabaseField;
import com.legent.dao.DaoHelper;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;
import java.util.List;

public class Material extends AbsStorePojo<Long> implements Serializable {

    static final public String FIELD_NAME_Materials_ID = "materials_id";
    static final public String FIELD_NAME_isMain = "isMain";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = FIELD_NAME_isMain)
    public boolean isMain;

    @JsonProperty("id")
    public long serviceId;

    @DatabaseField
    @JsonProperty("name")
    public String name;

    @DatabaseField
    @JsonProperty("standardUnit")
    public String standardUnit;

    @DatabaseField
    @JsonProperty("standardWeight")
    public String standardWeight;

    @DatabaseField
    @JsonProperty("popularUnit")
    public String popularUnit;

    @DatabaseField
    @JsonProperty("popularWeight")
    public String popularWeight;

    @JsonProperty("isRemove")
    public boolean isRemove;

    @DatabaseField(foreign = true, columnName = FIELD_NAME_Materials_ID)
    protected Materials materials;

    public Material() {
    }

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (!Strings.isNullOrEmpty(popularWeight) && !Strings.isNullOrEmpty(popularUnit)) {
            sb.append(popularWeight).append(popularUnit);
        }
        if (!Strings.isNullOrEmpty(standardWeight) && !Strings.isNullOrEmpty(standardUnit)) {
            sb.append("(").append(standardWeight).append(standardUnit).append(")");
        }
        return sb.toString();
    }


    static public List<Material> getMaterialList(long materialsId, boolean isMain) {

        List<Material> res = DaoHelper.getWhereEqAnd(Material.class,
                new String[]{FIELD_NAME_Materials_ID, FIELD_NAME_isMain},
                new Object[]{materialsId, isMain});
        return res;
    }
}
