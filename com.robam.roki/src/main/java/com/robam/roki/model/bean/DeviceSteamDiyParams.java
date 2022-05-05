package com.robam.roki.model.bean;

import com.legent.pojos.AbsPojo;

import java.util.List;

public class DeviceSteamDiyParams extends AbsPojo {
    private String value;
    private String paramType;
    private String img;
    private String defaultTemp;
    private String defaultMinute;
    private List<Integer> tempList;
    private List<Integer> timeList;


    public DeviceSteamDiyParams() {
    }

    public DeviceSteamDiyParams(String value, String paramType, String img, String defaultTemp, String defaultMinute) {
        this.value = value;
        this.paramType = paramType;
        this.img = img;
        this.defaultTemp = defaultTemp;
        this.defaultMinute = defaultMinute;
    }

    public DeviceSteamDiyParams(String value, String paramType, String img, String defaultTemp, String defaultMinute, List<Integer> tempList, List<Integer> timeList) {
        this.value = value;
        this.paramType = paramType;
        this.img = img;
        this.defaultTemp = defaultTemp;
        this.defaultMinute = defaultMinute;
        this.tempList = tempList;
        this.timeList = timeList;
    }

    public List<Integer> getTempList() {
        return tempList;
    }

    public void setTempList(List<Integer> tempList) {
        this.tempList = tempList;
    }

    public List<Integer> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<Integer> timeList) {
        this.timeList = timeList;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDefaultTemp() {
        return defaultTemp;
    }

    public void setDefaultTemp(String defaultTemp) {
        this.defaultTemp = defaultTemp;
    }

    public String getDefaultMinute() {
        return defaultMinute;
    }

    public void setDefaultMinute(String defaultMinute) {
        this.defaultMinute = defaultMinute;
    }
}
