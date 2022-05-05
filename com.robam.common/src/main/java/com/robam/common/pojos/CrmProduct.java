package com.robam.common.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsKeyPojo;

/**
 * Created by sylar on 15/7/31.
 */
public class CrmProduct extends AbsKeyPojo<String> implements Parcelable {

    @DatabaseField
    @JsonProperty()
    public String id;


    @DatabaseField
    @JsonProperty()
    public String category;

    @DatabaseField
    @JsonProperty()
    public String type;


    @DatabaseField
    @JsonProperty()
    public String buyTime;


    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.category);
        dest.writeString(this.type);
        dest.writeString(this.buyTime);
    }

    public CrmProduct() {
    }

    protected CrmProduct(Parcel in) {
        this.id = in.readString();
        this.category = in.readString();
        this.type = in.readString();
        this.buyTime = in.readString();
    }

    public static final Parcelable.Creator<CrmProduct> CREATOR = new Parcelable.Creator<CrmProduct>() {
        public CrmProduct createFromParcel(Parcel source) {
            return new CrmProduct(source);
        }

        public CrmProduct[] newArray(int size) {
            return new CrmProduct[size];
        }
    };
}
