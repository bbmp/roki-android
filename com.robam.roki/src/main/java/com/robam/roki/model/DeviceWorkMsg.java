package com.robam.roki.model;

import java.io.Serializable;

/**
 * Created by Rosicky on 15/12/12.
 */
public class DeviceWorkMsg implements Serializable {
    /**
     * 类型
     */
    private String type;
    /**
     * 温度
     */
    private String temperature;
    /**
     * 时间
     */
    private String time;
    /**
     * 水量状态
     */
    private String volumn;
    /**
     * 烹饪模式
     */
    private short mode;

    public short getMode() {
        return mode;
    }

    public void setMode(short mode) {
        this.mode = mode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVolumn() {
        return volumn;
    }

    public void setVolumn(String volumn) {
        this.volumn = volumn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (type != null) {
            sb.append(type);
        }
        if (temperature != null)
            sb.append(temperature);
        if (time != null)
            sb.append(time);
        if (volumn != null)
            sb.append(volumn);
        return sb.toString();
    }
}
