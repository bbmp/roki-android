package com.robam.roki.ui.page.device.steamovenone;

import android.os.Parcel;
import android.os.Parcelable;

import com.robam.roki.model.bean.CookbookGroup;

/**
 * Created by Administrator on 2019/8/27.
 */

public class CookBookBean implements Parcelable {

    public long id;
    public String functionCode;
    public String functionName;
    public String deviceCategory;
    public String deviceType;
    public String backgroundImg;
    public String functionParams;


    protected CookbookGroup group;

    protected CookBookBean(Parcel in) {
        id = in.readLong();
        functionCode = in.readString();
        functionName = in.readString();
        deviceCategory = in.readString();
        deviceType = in.readString();
        backgroundImg = in.readString();
        functionParams = in.readString();
    }

    public static final Creator<CookBookBean> CREATOR = new Creator<CookBookBean>() {
        @Override
        public CookBookBean createFromParcel(Parcel in) {
            return new CookBookBean(in);
        }

        @Override
        public CookBookBean[] newArray(int size) {
            return new CookBookBean[size];
        }
    };

    public CookbookGroup getParent() {
        return group;
    }


    public CookBookBean() {
    }

    public CookBookBean(long id, String functionCode, String functionName, String deviceCategory, String deviceType, String backgroundImg, String functionParams) {
        this.id = id;
        this.functionCode = functionCode;
        this.functionName = functionName;
        this.deviceCategory = deviceCategory;
        this.deviceType = deviceType;
        this.backgroundImg = backgroundImg;
        this.functionParams = functionParams;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(String deviceCategory) {
        this.deviceCategory = deviceCategory;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public String getFunctionParams() {
        return functionParams;
    }

    public void setFunctionParams(String functionParams) {
        this.functionParams = functionParams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.functionCode);
        dest.writeString(this.functionName);
        dest.writeString(this.deviceCategory);
        dest.writeString(this.deviceType);
    }
}
