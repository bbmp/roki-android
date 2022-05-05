package com.legent.pojos.collections;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

/**
 * Created by sylar on 15/8/25.
 */
public class BaseShortPojo extends AbsStorePojo<Short> implements Parcelable {

    @DatabaseField
    @JsonProperty
    public short id;

    @DatabaseField
    @JsonProperty
    public String name;

    @Override
    public Short getID() {
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

    //TODO---------------------------------------Parcelable---------------------------------------

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public BaseShortPojo() {
    }

    protected BaseShortPojo(Parcel in) {
        this.id = in.readParcelable(short.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Creator<BaseShortPojo> CREATOR = new Creator<BaseShortPojo>() {
        public BaseShortPojo createFromParcel(Parcel source) {
            return new BaseShortPojo(source);
        }

        public BaseShortPojo[] newArray(int size) {
            return new BaseShortPojo[size];
        }
    };
}
