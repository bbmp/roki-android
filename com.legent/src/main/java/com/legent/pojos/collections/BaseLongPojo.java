//package com.legent.pojos.collections;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.j256.ormlite.field.DatabaseField;
//import com.legent.pojos.AbsStorePojo;
//
///**
// * Created by sylar on 15/8/25.
// */
//public class BaseLongPojo extends AbsStorePojo<Long> implements Parcelable {
//
//    @DatabaseField
//    @JsonProperty
//    public long id;
//
//    @DatabaseField
//    @JsonProperty
//    public String name;
//
//    @Override
//    public Long getID() {
//        return id;
//    }
//
//    @Override
//    public String getName() {
//        return name;
//    }
//
//    @Override
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    //TODO---------------------------------------Parcelable---------------------------------------
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeLong(this.id);
//        dest.writeString(this.name);
//    }
//
//    public BaseLongPojo() {
//    }
//
//    protected BaseLongPojo(Parcel in) {
//        this.id = in.readLong();
//        this.name = in.readString();
//    }
//
//}
