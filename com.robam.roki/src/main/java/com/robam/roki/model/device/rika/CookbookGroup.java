package com.robam.roki.model.device.rika;

import java.util.List;



public class CookbookGroup {
    public long id;

    public String functionCode;
    public String functionName;


    public String deviceCategory;

    public String deviceType;


    public List<CookBookTag> cookBookTagList;

    public CookbookGroup() {
    }



    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
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

    public List<CookBookTag> getCookBookTagList() {
        return cookBookTagList;
    }

    public void setCookBookTagList(List<CookBookTag> cookBookTagList) {
        this.cookBookTagList = cookBookTagList;
    }
}
