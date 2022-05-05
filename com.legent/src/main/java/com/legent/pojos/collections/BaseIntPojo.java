package com.legent.pojos.collections;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

/**
 * Created by sylar on 15/8/25.
 */
public class BaseIntPojo extends AbsStorePojo<Integer> implements Parcelable {

    @DatabaseField
    @JsonProperty
    public int id;

    @DatabaseField
    @JsonProperty
    public String name;

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }


    //TODO---------------------------------------Parcelable---------------------------------------

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public BaseIntPojo() {
    }

    protected BaseIntPojo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<BaseIntPojo> CREATOR = new Parcelable.Creator<BaseIntPojo>() {
        public BaseIntPojo createFromParcel(Parcel source) {
            return new BaseIntPojo(source);
        }

        public BaseIntPojo[] newArray(int size) {
            return new BaseIntPojo[size];
        }
    };
}
