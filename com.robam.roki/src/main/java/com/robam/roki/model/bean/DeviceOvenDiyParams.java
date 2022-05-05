package com.robam.roki.model.bean;

import com.legent.pojos.AbsPojo;

import java.util.List;

/**
 * Created by Administrator on 2019/9/10.
 */

public class DeviceOvenDiyParams extends AbsPojo {

    private int code;
    private String value;
    private String hasRotate;
    private String paramType;
    private String img;
    private String defaultTemp;
    private String defaultMinute;
    private List<Integer>tempList;
    private List<Integer>timeList;

    private List<Integer> upTemp;
    private String upTempDefault;
    private List<Integer> downTemp;
    private String downTempDefault;
    private List<Integer> minute;
    private String minuteDefault;
    private String tempDiff;
    private String tempStart;
    private String tempMin;






    public DeviceOvenDiyParams(String value, String img, String defaultTemp, String defaultMinute, List<Integer> tempList, List<Integer> timeList) {
        this.value = value;
        this.img = img;
        this.defaultTemp = defaultTemp;
        this.defaultMinute = defaultMinute;
        this.tempList = tempList;
        this.timeList = timeList;
    }

    public DeviceOvenDiyParams(String value, String hasRotate, String paramType) {
        this.value = value;
        this.hasRotate = hasRotate;
        this.paramType = paramType;
    }

    public DeviceOvenDiyParams(String value, String hasRotate, String paramType, String img) {
        this.value = value;
        this.hasRotate = hasRotate;
        this.paramType = paramType;
        this.img = img;
    }

    public DeviceOvenDiyParams(String value, String hasRotate, String paramType, String defaultTemp,
                               String defaultMinute) {
        this.value = value;
        this.hasRotate = hasRotate;
        this.paramType = paramType;
        this.defaultTemp = defaultTemp;
        this.defaultMinute = defaultMinute;
    }


    public DeviceOvenDiyParams(int code, String value, String hasRotate, String paramType, String img,
                               String defaultTemp, String defaultMinute, List<Integer> tempList,
                               List<Integer> timeList, List<Integer> upTemp, String upTempDefault, List<Integer> downTemp,
                               String downTempDefault, List<Integer> minute, String minuteDefault, String tempDiff,
                               String tempStart, String tempMin) {
        this.code = code;
        this.value = value;
        this.hasRotate = hasRotate;
        this.paramType = paramType;
        this.img = img;
        this.defaultTemp = defaultTemp;
        this.defaultMinute = defaultMinute;
        this.tempList = tempList;
        this.timeList = timeList;
        this.upTemp = upTemp;
        this.upTempDefault = upTempDefault;
        this.downTemp = downTemp;
        this.downTempDefault = downTempDefault;
        this.minute = minute;
        this.minuteDefault = minuteDefault;
        this.tempDiff = tempDiff;
        this.tempStart = tempStart;
        this.tempMin = tempMin;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public DeviceOvenDiyParams() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHasRotate() {
        return hasRotate;
    }

    public void setHasRotate(String hasRotate) {
        this.hasRotate = hasRotate;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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


    public List<Integer> getUpTemp() {
        return upTemp;
    }

    public void setUpTemp(List<Integer> upTemp) {
        this.upTemp = upTemp;
    }

    public String getUpTempDefault() {
        return upTempDefault;
    }

    public void setUpTempDefault(String upTempDefault) {
        this.upTempDefault = upTempDefault;
    }

    public List<Integer> getDownTemp() {
        return downTemp;
    }

    public void setDownTemp(List<Integer> downTemp) {
        this.downTemp = downTemp;
    }

    public String getDownTempDefault() {
        return downTempDefault;
    }

    public void setDownTempDefault(String downTempDefault) {
        this.downTempDefault = downTempDefault;
    }

    public List<Integer> getMinute() {
        return minute;
    }

    public void setMinute(List<Integer> minute) {
        this.minute = minute;
    }

    public String getMinuteDefault() {
        return minuteDefault;
    }

    public void setMinuteDefault(String minuteDefault) {
        this.minuteDefault = minuteDefault;
    }

    public String getTempDiff() {
        return tempDiff;
    }

    public void setTempDiff(String tempDiff) {
        this.tempDiff = tempDiff;
    }

    public String getTempStart() {
        return tempStart;
    }

    public void setTempStart(String tempStart) {
        this.tempStart = tempStart;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }



}
