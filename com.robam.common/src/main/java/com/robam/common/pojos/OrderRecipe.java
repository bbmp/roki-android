package com.robam.common.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.util.List;

/**
 * 免费配送订单内的 单条菜谱信息
 * Created by sylar on 15/7/17.
 */
public class OrderRecipe extends AbsStorePojo<Long> implements Parcelable {


    @DatabaseField(id = true)
    @JsonProperty()
    public long id;

    @DatabaseField
    @JsonProperty()
    public String name;

    @DatabaseField
    @JsonProperty("image")
    public String imgUrl;

    @DatabaseField
    @JsonProperty()
    public int number;

    @DatabaseField
    @JsonProperty()
    public List<String> materials;


    @Override
    public Long getID() {
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
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.imgUrl);
        dest.writeInt(this.number);
        dest.writeStringList(this.materials);
    }

    public OrderRecipe() {
    }

    protected OrderRecipe(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.imgUrl = in.readString();
        this.number = in.readInt();
        this.materials = in.createStringArrayList();
    }

    public static final Parcelable.Creator<OrderRecipe> CREATOR = new Parcelable.Creator<OrderRecipe>() {
        public OrderRecipe createFromParcel(Parcel source) {
            return new OrderRecipe(source);
        }

        public OrderRecipe[] newArray(int size) {
            return new OrderRecipe[size];
        }
    };
}
