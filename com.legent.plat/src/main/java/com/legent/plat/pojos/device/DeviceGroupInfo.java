package com.legent.plat.pojos.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsKeyPojo;

public class DeviceGroupInfo extends AbsKeyPojo<Long> implements Parcelable {

    @JsonProperty("id")
    public Long id;

    @JsonProperty("name")
    public String name;


    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
    }

    public DeviceGroupInfo() {
    }

    private DeviceGroupInfo(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Parcelable.Creator<DeviceGroupInfo> CREATOR = new Parcelable.Creator<DeviceGroupInfo>() {
        public DeviceGroupInfo createFromParcel(Parcel source) {
            return new DeviceGroupInfo(source);
        }

        public DeviceGroupInfo[] newArray(int size) {
            return new DeviceGroupInfo[size];
        }
    };
}
