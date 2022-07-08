package com.robam.roki.model.bean;

public class DeviceMoreBean {
    private String name;
    private String deviceName;
    private int imageRes;
    private String imageUrl;
    private int type;

    public String getName() {
        return name;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setType(int type) {
        this.type = type;
    }
}
