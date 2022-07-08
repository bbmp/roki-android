package com.robam.roki.model.bean;

import com.legent.pojos.AbsPojo;

/**
 * Created by Administrator on 2019/9/10.
 */

public class DeviceSteamOvenDiyParams extends AbsPojo {

    private int code;
    private String value;
    private String hasRotate;
    private String paramType;
    private String img;
    private String defaultTemp;
    private String defaultMinute;

    public DeviceSteamOvenDiyParams(int code, String value, String img, String defaultTemp, String defaultMinute) {
        this.code = code;
        this.value = value;
        this.img = img;
        this.defaultTemp = defaultTemp;
        this.defaultMinute = defaultMinute;
    }

    public DeviceSteamOvenDiyParams(String value, String hasRotate, String paramType) {
        this.value = value;
        this.hasRotate = hasRotate;
        this.paramType = paramType;
    }

    public DeviceSteamOvenDiyParams(String value, String hasRotate, String paramType, String img) {
        this.value = value;
        this.hasRotate = hasRotate;
        this.paramType = paramType;
        this.img = img;
    }

    public DeviceSteamOvenDiyParams(String value, String hasRotate, String paramType, String defaultTemp, String defaultMinute) {
        this.value = value;
        this.hasRotate = hasRotate;
        this.paramType = paramType;
        this.defaultTemp = defaultTemp;
        this.defaultMinute = defaultMinute;
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

    public DeviceSteamOvenDiyParams() {
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
}
