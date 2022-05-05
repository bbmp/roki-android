package com.robam.common.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsPojo;

/**
 * Created by sylar on 15/7/31.
 */
public class MaintainInfo extends AbsPojo implements Parcelable {

    @DatabaseField
    @JsonProperty()
    public String name;

    @DatabaseField
    @JsonProperty()
    public String phone;

    @DatabaseField
    @JsonProperty()
    public String address;

    @JsonProperty()
    public String province;

    @JsonProperty()
    public String city;

    @JsonProperty()
    public String county;

    @JsonProperty()
    public long bookTime;

    @JsonProperty()
    public long postTime;

    @JsonProperty()
    public String status;

    @JsonProperty()
    public String category;

    @JsonProperty()
    public String productType;

    @JsonProperty()
    public String productId;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.county);
        dest.writeLong(this.bookTime);
        dest.writeLong(this.postTime);
        dest.writeString(this.status);
        dest.writeString(this.category);
        dest.writeString(this.productType);
        dest.writeString(this.productId);
    }

    public MaintainInfo() {
    }

    protected MaintainInfo(Parcel in) {
        this.name = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.county = in.readString();
        this.bookTime = in.readLong();
        this.postTime = in.readLong();
        this.status = in.readString();
        this.category = in.readString();
        this.productType = in.readString();
        this.productId = in.readString();
    }

    public static final Parcelable.Creator<MaintainInfo> CREATOR = new Parcelable.Creator<MaintainInfo>() {
        public MaintainInfo createFromParcel(Parcel source) {
            return new MaintainInfo(source);
        }

        public MaintainInfo[] newArray(int size) {
            return new MaintainInfo[size];
        }
    };
}
