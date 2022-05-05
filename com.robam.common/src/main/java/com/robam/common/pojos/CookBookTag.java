package com.robam.common.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsStorePojo;

/**
 * Created by Administrator on 2017/4/9.
 */

public class CookBookTag extends AbsStorePojo implements Parcelable{
  //  @DatabaseField(id = true)
    @JsonProperty("id")
    public long id;

  //  @DatabaseField
    @JsonProperty("name")
    public String name;

  //  @DatabaseField
    @JsonProperty("imgUrl")
    public String imageUrl;

   /* @DatabaseField(foreign = true, foreignAutoRefresh = true)
    protected CookBookTagGroup group;*/

    /**
     * 本地存储的库版本号
     */
   // @DatabaseField()
    public int version;

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public CookBookTag() {
    }

    protected CookBookTag(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.imageUrl = in.readString();
        this.version = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<CookBookTag> CREATOR = new Creator<CookBookTag>() {
        public CookBookTag createFromParcel(Parcel source) {
            return new CookBookTag(source);
        }

        public CookBookTag[] newArray(int size) {
            return new CookBookTag[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.version);
    }


    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

}
