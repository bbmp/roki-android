package com.robam.common.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

/**
 * Created by sylar on 15/7/17.
 */
public class OrderContacter extends AbsStorePojo<Long> implements Parcelable {


    @DatabaseField(id = true)
    public long id;

    @DatabaseField
    @JsonProperty()
    public String name;

    @DatabaseField
    @JsonProperty()
    public String phone;

    @DatabaseField
    @JsonProperty()
    public String city;

    @DatabaseField
    @JsonProperty()
    public String address;


    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.city);
        dest.writeString(this.address);
    }

    public OrderContacter() {
    }

    protected OrderContacter(Parcel in) {
        this.name = in.readString();
        this.phone = in.readString();
        this.city = in.readString();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<OrderContacter> CREATOR = new Parcelable.Creator<OrderContacter>() {
        public OrderContacter createFromParcel(Parcel source) {
            return new OrderContacter(source);
        }

        public OrderContacter[] newArray(int size) {
            return new OrderContacter[size];
        }
    };
}
