package com.robam.common.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yinwei on 2016/12/29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataInfo implements Parcelable {
   /* public final static String COLUMN_DATAINFO_ID = "datainfo_id";

    @Override
    public String toString() {
        return "DataInfo{" +
                "id=" + id +
                ", timeType='" + timeType + '\'' +
                ", time='" + time + '\'' +
                ", volume='" + volume + '\'' +
                ", ranking='" + '\'' +
                ", drinkingItem=" + drinkingItem +
                '}';
    }

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    @JsonProperty("timeType")
    public String timeType;

    @DatabaseField
    @JsonProperty("time")
    public String time;

    @DatabaseField
    @JsonProperty("volume")
    public String volume;

    @DatabaseField(foreign = true, columnName = COLUMN_DATAINFO_ID)
    protected DrinkingItem drinkingItem;

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return null;
    }*/
   @JsonProperty("timeType")
   public String timeType;

    @JsonProperty("time")
    public String time;

    @JsonProperty("volume")
    public String volume;

    @JsonProperty("ranking")
    public int ranking;

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<DataInfo> CREATOR=new Creator<DataInfo>() {
        @Override
        public DataInfo createFromParcel(Parcel parcel) {
            DataInfo di=new DataInfo();
            di.time=parcel.readString();
            di.volume=parcel.readString();
            di.timeType=parcel.readString();
            di.ranking=parcel.readInt();
            return di;
        }

        @Override
        public DataInfo[] newArray(int i) {
            return new DataInfo[i];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(timeType);
            parcel.writeString(time);
            parcel.writeString(volume);
            parcel.writeInt(ranking);
    }

    @Override
    public String toString() {
        return "DataInfo{" +
                "timeType='" + timeType + '\'' +
                ", time='" + time + '\'' +
                ", volume='" + volume + '\'' +
                ", ranking=" + ranking +
                '}';
    }
}
