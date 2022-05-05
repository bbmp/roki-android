package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.j256.ormlite.field.DatabaseField;

import java.util.List;

/**
 * 第三方菜谱
 *
 * @author sylar
 */
public class Recipe3rd extends AbsRecipe {

    @DatabaseField
    @JsonProperty("imgUrl")
    public String imgUrl;

    @DatabaseField
    @JsonProperty("detailUrl")
    public String detailUrl;

    /**
     * 小图
     */
    @DatabaseField
    @JsonProperty("imgSmall")
    public String imgSmall;

    /**
     * 中图
     */
    @DatabaseField
    @JsonProperty("imgMedium")
    public String imgMedium;

    /**
     * 获取菜谱用到的设备品类
     */
    @JsonProperty("dcs")
    public List<Dc> js_dcs;

    public List<Dc> getDcs() {
        if (js_dcs == null || js_dcs.size() == 0)
            return Lists.newArrayList();

        List<Dc> list = Lists.newArrayList(js_dcs);
        return list;
    }

    @JsonProperty("cookbookTagGroups")
    public List<CookBookTagGroup> js_cookbook3rd=Lists.newArrayList();

}
