package com.legent.plat.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;


public class AppVersionInfo extends AbsPojo implements Parcelable {

    public static final Parcelable.Creator<AppVersionInfo> CREATOR = new Parcelable.Creator<AppVersionInfo>() {
        public AppVersionInfo createFromParcel(Parcel source) {
            return new AppVersionInfo(source);
        }

        public AppVersionInfo[] newArray(int size) {
            return new AppVersionInfo[size];
        }
    };
    @JsonProperty("code")
    public int code;
    @JsonProperty("name")
    public String name;
    @JsonProperty("url")
    public String url;
    @JsonProperty("desc")
    public String desc;

    public AppVersionInfo() {
    }

    private AppVersionInfo(Parcel in) {
        this.code = in.readInt();
        this.name = in.readString();
        this.url = in.readString();
        this.desc = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.desc);
    }
}
