package com.robam.roki.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by linxiaobin on 2015/12/21.
 */
public class NormalModeItemMsg implements Serializable {
    private String type;
    private Drawable drawable;

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    private String time;
    private String temperature;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (type != null) {
            sb.append(type);
        }
        return sb.toString();
    }
}
