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
//public class BaseStringPojo extends AbsStorePojo<String> implements Parcelable {
//
//    @DatabaseField
//    @JsonProperty
//    public String id;
//
//    @DatabaseField
//    @JsonProperty
//    public String name;
//
//    @Override
//    public String getID() {
//        return id;
//    }
//
//    @Override
//    public String getName() {
//        return name;
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
//        dest.writeString(this.id);
//        dest.writeString(this.name);
//    }
//
//    public BaseStringPojo() {
//    }
//
//    protected BaseStringPojo(Parcel in) {
//        this.id = in.readString();
//        this.name = in.readString();
//    }
//
//    public static final Parcelable.Creator<BaseStringPojo> CREATOR = new Parcelable.Creator<BaseStringPojo>() {
//        public BaseStringPojo createFromParcel(Parcel source) {
//            return new BaseStringPojo(source);
//        }
//
//        public BaseStringPojo[] newArray(int size) {
//            return new BaseStringPojo[size];
//        }
//    };
//}
