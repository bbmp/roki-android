package com.robam.common.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsKeyPojo;

import java.util.List;

/**
 * Created by sylar on 15/7/31.
 */
public class CrmCustomer extends AbsKeyPojo<String> implements Parcelable {

    @DatabaseField
    @JsonProperty()
    public String id;


    @DatabaseField
    @JsonProperty()
    public String name;


    @DatabaseField
    @JsonProperty()
    public String phone;


    /**
     * 详情地址
     */
    @DatabaseField
    @JsonProperty()
    public String address;


    /**
     * 省
     */
    @DatabaseField
    @JsonProperty()
    public String province;


    /**
     * 市
     */
    @DatabaseField
    @JsonProperty()
    public String city;


    /**
     * 县/区
     */
    @DatabaseField
    @JsonProperty()
    public String county;


    @DatabaseField
    @JsonProperty("products")
    public List<CrmProduct> products;


    @Override
    public String getID() {
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
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.county);
        dest.writeTypedList(products);
    }

    public CrmCustomer() {
    }

    protected CrmCustomer(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.county = in.readString();
        this.products = in.createTypedArrayList(CrmProduct.CREATOR);
    }

    public static final Parcelable.Creator<CrmCustomer> CREATOR = new Parcelable.Creator<CrmCustomer>() {
        public CrmCustomer createFromParcel(Parcel source) {
            return new CrmCustomer(source);
        }

        public CrmCustomer[] newArray(int size) {
            return new CrmCustomer[size];
        }
    };
}
