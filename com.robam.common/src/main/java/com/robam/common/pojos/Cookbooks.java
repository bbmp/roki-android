package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */

public class Cookbooks extends AbsRecipe implements Serializable {

    @DatabaseField
    @JsonProperty("id")
    public long id;

    @DatabaseField
    @JsonProperty("name")
    public String name;


    @DatabaseField
    @JsonProperty("imgSmall")
    public String imgSmall;


    @DatabaseField
    @JsonProperty("imgMedium")
    public String imgMedium;

    @DatabaseField
    @JsonProperty("collectCount")
    public int collectCount;

    @DatabaseField
    @JsonProperty("sourceType")
    public int sourceType;

    @DatabaseField
    @JsonProperty("providerImage")
    public String providerImage;

    @DatabaseField
    @JsonProperty("stampLogo")
    public String stampLogo;

    @ForeignCollectionField()
    public ForeignCollection<Dc> db_dcs;
    /**
     * 获取菜谱用到的设备品类
     */
    @JsonProperty("dcs")
    public List<Dc> js_dcs;


    public List<Dc> getDcs() {
        if (db_dcs == null || db_dcs.size() == 0)
            return Lists.newArrayList();

        List<Dc> list = Lists.newArrayList(db_dcs);
        return list;
    }


    /*@JsonProperty("cookbookTagGroup")
    public List<Group> js_group;

    public List<Group> getJs_group(){
        if (db_group==null||db_group.size()==0){
            return Lists.newArrayList();
        }

        List<Group> list=Lists.newArrayList(db_group);
        return list;
    }*/

}
