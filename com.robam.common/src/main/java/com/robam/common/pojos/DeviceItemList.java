package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.legent.Callback;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.pojos.AbsStorePojo;
import com.robam.common.services.CookbookManager;
import com.robam.common.services.SysCfgManager;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * 联网设备详细列表
 *
 * @author zdj
 */
public class DeviceItemList extends AbsStorePojo<Long> implements Serializable {

    public final static String DeviceItemList_ID = "DeviceItemList";

    @DatabaseField(generatedId = true)
    private long id;
    /**
     * 产品型号
     */
    @DatabaseField
    @JsonProperty("dt")
    public String dt;

    /**
     * 产品平台
     */
    @DatabaseField
    @JsonProperty("dp")
    public String dp;


    /**
     * 产品缩略图
     */
    @DatabaseField
    @JsonProperty("iconUrl")
    public String iconUrl;


    /**
     * 联网图片地址
     */
    @DatabaseField
    @JsonProperty("netImgUrl")
    public String netImgUrl;

    /**
     * displayType
     */
    @DatabaseField
    @JsonProperty("displayType")
    public String displayType;


    /**
     * 联网说明文字
     */
    @DatabaseField
    @JsonProperty("netTips")
    public String netTips;

    @DatabaseField(foreign = true, columnName = DeviceItemList_ID)
    public DeviceGroupList deviceGroupList;

    // ----------------------------------------------------------------------------------------------------

    public String getDt() {
        return dt;
    }

    public String getDp() {
        return dp;
    }

    /**
     * 获取产品缩略图的url链接
     *
     * @return
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * 获取联网图片地址url链接
     *
     * @return
     */
    public String getNetImgUrl() {
        return netImgUrl;
    }

    public String getNetTips() {
        return netTips;
    }

    @Override
    public String getName() {
        return dt;
    }

    public void setName(String dt) {
        this.dt = dt;
    }

    public DeviceGroupList getParent() {
        return deviceGroupList;
    }

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String toString() {
        return dt+dp;
    }

    public String tag;
}