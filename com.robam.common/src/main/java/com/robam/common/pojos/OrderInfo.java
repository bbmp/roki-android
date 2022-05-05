package com.robam.common.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.util.List;

/**
 * 免费配送订单
 * <p/>
 * Created by sylar on 15/7/17.
 */
public class OrderInfo extends AbsStorePojo<Long> implements Parcelable {

    /**
     * 接单中
     */
    final public static int OrderStatus_Accepted = 1;
    /**
     * 备菜中
     */
    final public static int OrderStatus_Preparing = 2;
    /**
     * 配送中
     */
    final public static int OrderStatus_Delivering = 3;
    /**
     * 配送完成
     */
    final public static int OrderStatus_Completed = 4;
    /**
     * 无效订单
     */
    final public static int OrderStatus_nullity = 5;


    @DatabaseField(id = true)
    @JsonProperty("orderId")
    public long id;

    @DatabaseField
    @JsonProperty("date")
    public long submitTime;

    @DatabaseField
    @JsonProperty()
    public int status;

    @DatabaseField
    @JsonProperty()
    public String code;//订单编号

    @DatabaseField
    @JsonProperty()
    public String logisticsCompany;//物流公司

    @DatabaseField
    @JsonProperty()
    public String logisticsNO;//物流编号

    @DatabaseField
    @JsonProperty()
    public OrderContacter customer;

    @DatabaseField
    @JsonProperty("cookbooks")
    public List<OrderRecipe> recipes;


    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return code;
    }

    public String getStatusString() {
        String str = null;
        switch (status) {
            case OrderStatus_Accepted:
                str = "接单中";
                break;
            case OrderStatus_Preparing:
                str = "正在备菜";
                break;
            case OrderStatus_Delivering:
                str = "正在配送";
                break;
            case OrderStatus_Completed:
                str = "配送完成";
                break;
            case OrderStatus_nullity:
                str = "无效订单";
                break;
        }
        return str;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.submitTime);
        dest.writeInt(this.status);
        dest.writeString(this.code);
        dest.writeString(this.logisticsCompany);
        dest.writeString(this.logisticsNO);
        dest.writeTypedList(recipes);
    }

    public OrderInfo() {

    }

    protected OrderInfo(Parcel in) {
        this.id = in.readLong();
        this.submitTime = in.readLong();
        this.status = in.readInt();
        this.code = in.readString();
        this.logisticsCompany = in.readString();
        this.logisticsNO = in.readString();
        this.recipes = in.createTypedArrayList(OrderRecipe.CREATOR);
    }

    public static final Parcelable.Creator<OrderInfo> CREATOR = new Parcelable.Creator<OrderInfo>() {
        public OrderInfo createFromParcel(Parcel source) {
            return new OrderInfo(source);
        }

        public OrderInfo[] newArray(int size) {
            return new OrderInfo[size];
        }
    };
}
