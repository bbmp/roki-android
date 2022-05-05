package com.robam.common.pojos;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.legent.dao.DaoHelper;
import com.legent.pojos.AbsStorePojo;
import com.robam.common.io.cloud.Reponses;

import java.io.Serializable;
import java.util.List;

public class DicGroupDto extends AbsStorePojo<Long> implements Serializable {


    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField()
    @JsonProperty("codeGroupName")
    public String codeGroupName;

    @DatabaseField()
    @JsonProperty("codeGroupDesc")
    public String codeGroupDesc;

    @DatabaseField()
    @JsonProperty("dicDtoList")
    public List<DicDto> dicDtoList;

    @ForeignCollectionField
    private ForeignCollection<DicDto> dicDto;


    public List<DicDto> getDicDtoList() {
        if(dicDtoList==null)
            return Lists.newArrayList();
        return dicDtoList;
    }


    @Override
    public Long getID() {
        return id;
    }

    @Override
    public void save2db() {
        super.save2db();
        if (dicDtoList != null) {
            for (DicDto dicDto : dicDtoList) {
                dicDto.dicGroupDto=this;
                dicDto.save2db();
            }
        }
        DaoHelper.update(this);
        DaoHelper.refresh(this);
    }

    @Override
    public String getName() {
        return codeGroupDesc;
    }

    @Override
    public void delete(long id) {
        super.delete(id);
        try {
            DaoHelper.deleteWhereEq(DicDto.class, DicDto.FOREIGN_COLUMNNAME_ID, this.id);
            super.delete(id);
        } catch (Exception e) {
        }
    }
}
