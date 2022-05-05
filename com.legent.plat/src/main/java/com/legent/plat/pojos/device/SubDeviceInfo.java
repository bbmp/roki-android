package com.legent.plat.pojos.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.dao.DaoHelper;
import com.legent.pojos.AbsKeyPojo;
import com.legent.pojos.IJsonPojo;
import com.legent.utils.JsonUtils;

public class SubDeviceInfo extends AbsKeyPojo<String> implements IJsonPojo, Parcelable {
    public final static String COLUMN_ID = "guid";
    public static final String COLUMN_DEVICEINFO_ID = "DeviceInfo_ID";
    /**
     * 唯一编码
     */
    @DatabaseField(id = true, columnName = COLUMN_ID)
    @JsonProperty("guid")
    public String guid;

    /**
     * 设备名称
     */
    @DatabaseField
    @JsonProperty("name")
    public String name;

    /**
     * 业务编码（供应商定制ID）
     */
    @DatabaseField
    @JsonProperty("bid")
    public String bid;

    /**
     * 设备固件版本
     */
    @DatabaseField
    @JsonProperty("ver")
    public int ver;

    public int mcuType;
    public boolean isConnected;

    //周定钧

    /**
     * 设备名称
     */
    @DatabaseField
    @JsonProperty("dc")
    public String dc;

    /**
     * 设备平台
     */
    @DatabaseField
    @JsonProperty("dp")
    public String dp;

    /**
     * 设备类型
     */
    @DatabaseField
    @JsonProperty("dt")
    public String dt;

    /**
     * 展示的设备类型名称
     */
    @DatabaseField
    @JsonProperty("displayType")       //获取设备上线？？
    public String displayType;

    @DatabaseField
    @JsonProperty("categoryName")
    public String categoryName;

    @DatabaseField
    @JsonProperty("categoryEnglishName")
    public String categoryEnglishName;

    @DatabaseField
    @JsonProperty("categoryIconUrl")
    public String categoryIconUrl;



    @DatabaseField(foreign = true, columnName = COLUMN_DEVICEINFO_ID)
    public DeviceInfo deviceInfo;

    public SubDeviceInfo() {
    }

    public SubDeviceInfo(String guid, String bid) {
        this();
        this.guid = guid;
        this.bid = bid;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    @Override
    public String getID() {
        return guid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getDc() {
        return dc;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryEnglishName() {
        return categoryEnglishName;
    }

    public void setCategoryEnglishName(String categoryEnglishName) {
        this.categoryEnglishName = categoryEnglishName;
    }

    public String getCategoryIconUrl() {
        return categoryIconUrl;
    }

    public void setCategoryIconUrl(String categoryIconUrl) {
        this.categoryIconUrl = categoryIconUrl;
    }

    public String getDp() {
        return dp;
    }

    public String getDt() {
        return dt;
    }

    public DeviceGuid getDeviceGuid() {
        return DeviceGuid.newGuid(guid);
    }

    @Override
    public String toString() {
        try {
            return JsonUtils.pojo2Json(this);
        } catch (Exception e) {
            e.printStackTrace();
            return guid;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.guid);
        dest.writeString(this.name);
        dest.writeString(this.bid);
        dest.writeInt(this.ver);
        dest.writeInt(this.mcuType);
        dest.writeByte(isConnected ? (byte) 1 : (byte) 0);
        //
        dest.writeString(this.dc);
        dest.writeString(this.dp);
        dest.writeString(this.dt);
        dest.writeString(this.displayType);
        dest.writeString(this.categoryName);
        dest.writeString(this.categoryEnglishName);
        dest.writeString(this.categoryIconUrl);
    }

    private SubDeviceInfo(Parcel in) {
        this.guid = in.readString();
        this.name = in.readString();
        this.bid = in.readString();
        this.ver = in.readInt();
        this.mcuType = in.readInt();
        this.isConnected = in.readByte() != 0;
        //
        this.dc = in.readString();
        this.dp = in.readString();
        this.dt = in.readString();
        this.displayType = in.readString();
        this.categoryName = in.readString();
        this.categoryEnglishName = in.readString();
        this.categoryIconUrl = in.readString();
    }

    public static final Creator<SubDeviceInfo> CREATOR = new Creator<SubDeviceInfo>() {
        public SubDeviceInfo createFromParcel(Parcel source) {
            return new SubDeviceInfo(source);
        }

        public SubDeviceInfo[] newArray(int size) {
            return new SubDeviceInfo[size];
        }
    };

    public void save2db() {
        if (DaoHelper.isExists(getClass(), getID())) {
            DaoHelper.update(this);
        } else {
            DaoHelper.create(this);
        }
        DaoHelper.refresh(this);
    }
}
