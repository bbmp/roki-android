package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;
import java.util.List;

public class DicDto extends AbsStorePojo<Long> implements Serializable {

    public static final String FOREIGN_COLUMNNAME_ID = "DicGroupDto_ID";

    @DatabaseField(generatedId = true)
    private long id;

    @JsonProperty("code")
    public String code;

    @JsonProperty("codeValue")
    public String codeValue;

    @JsonProperty("codeDesc")
    public String codeDesc;


    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = DicDto.FOREIGN_COLUMNNAME_ID)
    protected DicGroupDto dicGroupDto;

    @Override
    public void save2db() {
        super.save2db();
    }

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return codeDesc;
    }
}
