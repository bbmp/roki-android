package com.legent.plat.pojos.device;

import android.os.Parcel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.legent.dao.DaoHelper;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;

import java.util.List;

/**
 * 设备静态信息封装
 */
public class DeviceInfo extends SubDeviceInfo implements android.os.Parcelable {

    /**
     * 拥有者编码
     */
    @DatabaseField
    @JsonProperty("ownerId")
    public long ownerId;

    /**
     * 设备组编码
     */
    @DatabaseField
    @JsonProperty("groupId")
    public long groupId;

    /**
     * MAC地址
     */
    @DatabaseField
    @JsonProperty("mac")
    public String mac;





//	/**
//	 * 设备名称
//	 */
//	@JsonProperty("dc")
//	public String dc;
//
//	/**
//	 * 设备平台
//	 */
//	@JsonProperty("dp")
//	public String dp;
//
//	/**
//	 * 设备类型
//	 */
//	@JsonProperty(" dt")
//	public String dt;


    /**
     * 子设备列表
     */
    @JsonProperty("subDevices")
    public List<SubDeviceInfo> subDevices = Lists.newArrayList();

    @ForeignCollectionField()
    public ForeignCollection<SubDeviceInfo> db_subDevices;

    public DeviceInfo() {
    }

    public DeviceInfo(String guid, String bid, long ownerId) {
        super(guid, bid);
        LogUtils.i("20180709","guid:"+guid+"bid:"+bid+"ownerId:"+ownerId);
        this.ownerId = ownerId;
    }

    public void addSubDevice(SubDeviceInfo device) {
        subDevices.add(device);
    }

    public SubDeviceInfo getSubDevice() {
        if (subDevices != null) {
            for (SubDeviceInfo device : subDevices) {
                if (Objects.equal(device.guid, guid)) {
                    return device;
                }
            }
        }
        return null;
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
        dest.writeLong(this.ownerId);
        dest.writeLong(this.groupId);
        dest.writeString(this.mac);
        dest.writeTypedList(subDevices);
        dest.writeString(this.guid);
        dest.writeString(this.name);
        dest.writeString(this.bid);
        dest.writeInt(this.ver);
        dest.writeInt(this.mcuType);
        dest.writeByte(isConnected ? (byte) 1 : (byte) 0);

    }

    private DeviceInfo(Parcel in) {
        this.ownerId = in.readLong();
        this.groupId = in.readLong();
        this.mac = in.readString();
        in.readTypedList(subDevices, SubDeviceInfo.CREATOR);
        this.guid = in.readString();
        this.name = in.readString();
        this.bid = in.readString();
        this.ver = in.readInt();
        this.mcuType = in.readInt();
        this.isConnected = in.readByte() != 0;

    }

    public static final Creator<DeviceInfo> CREATOR = new Creator<DeviceInfo>() {
        public DeviceInfo createFromParcel(Parcel source) {
            return new DeviceInfo(source);
        }

        public DeviceInfo[] newArray(int size) {
            return new DeviceInfo[size];
        }
    };

    public void save2db() {
        if (DaoHelper.isExists(getClass(), getID())) {
            DaoHelper.update(this);
        } else {
            DaoHelper.create(this);
        }
        DaoHelper.refresh(this);
        if (subDevices != null) {
            for (SubDeviceInfo subDeviceInfo : subDevices) {
                subDeviceInfo.deviceInfo = this;
                subDeviceInfo.save2db();
            }
        }
    }
}
