package com.robam.roki.ui.page.device.oven;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/8/27.
 */

public class CookBookTag implements Serializable {

    public long id;
    public String functionCode;
    public String functionName;
    public String deviceCategory;
    public String deviceType;
    public String backgroundImg;
    public String backgroundImgH ;
    public String functionParams;


    protected CookbookGroup group;

    protected CookBookTag(Parcel in) {
        id = in.readLong();
        functionCode = in.readString();
        functionName = in.readString();
        deviceCategory = in.readString();
        deviceType = in.readString();
        backgroundImg = in.readString();
        backgroundImgH = in.readString();
        functionParams = in.readString();
    }



    public CookbookGroup getParent() {
        return group;
    }


    public CookBookTag() {
    }

    public CookBookTag(long id, String functionCode, String functionName, String deviceCategory, String deviceType, String backgroundImg,String functionParams) {
        this.id = id;
        this.functionCode = functionCode;
        this.functionName = functionName;
        this.deviceCategory = deviceCategory;
        this.deviceType = deviceType;
        this.backgroundImg = backgroundImg;
        this.functionParams = functionParams;
    }

    public CookBookTag(long id, String functionCode, String functionName, String deviceCategory, String deviceType, String backgroundImg, String backgroundImghH, String functionParams) {
        this.id = id;
        this.functionCode = functionCode;
        this.functionName = functionName;
        this.deviceCategory = deviceCategory;
        this.deviceType = deviceType;
        this.backgroundImg = backgroundImg;
        this.backgroundImgH = backgroundImghH;
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

    public String getBackgroundImghH() {
        return backgroundImgH;
    }

    public void setBackgroundImghH(String backgroundImghH) {
        this.backgroundImgH = backgroundImghH;
    }

    public String getFunctionParams() {
        return functionParams;
    }

    public void setFunctionParams(String functionParams) {
        this.functionParams = functionParams;
    }


}
