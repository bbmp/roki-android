package com.legent.plat.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsKeyPojo;

/**
 * Created by yinwei on 2017/10/23.
 */

public class ThirdInfo extends AbsKeyPojo<String> implements Parcelable {

    @JsonProperty("appSource")
    public String appSource;

    @JsonProperty("openId")
    public String openId;

    @JsonProperty("accessToken")
    public String accessToken;

    public ThirdInfo(){

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appSource);
        dest.writeString(this.openId);
        dest.writeString(this.accessToken);
    }

    private ThirdInfo(Parcel in) {
        this.appSource = in.readString();
        this.openId = in.readString();
        this.accessToken = in.readString();
    }

    public static final Parcelable.Creator<ThirdInfo> CREATOR = new Parcelable.Creator<ThirdInfo>() {
        public ThirdInfo createFromParcel(Parcel source) {
            return new ThirdInfo(source);
        }

        public ThirdInfo[] newArray(int size) {
            return new ThirdInfo[size];
        }
    };

    @Override
    public String getID() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    public String getAccessToken(){
        return accessToken;
    }
}

