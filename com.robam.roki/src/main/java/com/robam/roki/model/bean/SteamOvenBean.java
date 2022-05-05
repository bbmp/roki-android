package com.robam.roki.model.bean;

import androidx.annotation.NonNull;

import java.util.List;

public class SteamOvenBean implements Comparable<SteamOvenBean> {
    private int id;
    private String code;
    private String modeName;
    private List<Integer> temp;
    private List<Integer>time;
    private String defaultTemp;
    private String defaultTime;

    public SteamOvenBean() {
    }

    public SteamOvenBean(int id, String code, String modeName, List<Integer> temp, List<Integer> time, String defaultTemp, String defaultTime) {
        this.id = id;
        this.code = code;
        this.modeName = modeName;
        this.temp = temp;
        this.time = time;
        this.defaultTemp = defaultTemp;
        this.defaultTime = defaultTime;
    }

    public SteamOvenBean(String code, String modeName, List<Integer> temp, List<Integer> time, String defaultTemp, String defaultTime) {
        this.code = code;
        this.modeName = modeName;
        this.temp = temp;
        this.time = time;
        this.defaultTemp = defaultTemp;
        this.defaultTime = defaultTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public List<Integer> getTemp() {
        return temp;
    }

    public void setTemp(List<Integer> temp) {
        this.temp = temp;
    }

    public List<Integer> getTime() {
        return time;
    }

    public void setTime(List<Integer> time) {
        this.time = time;
    }

    public String getDefaultTemp() {
        return defaultTemp;
    }

    public void setDefaultTemp(String defaultTemp) {
        this.defaultTemp = defaultTemp;
    }

    public String getDefaultTime() {
        return defaultTime;
    }

    public void setDefaultTime(String defaultTime) {
        this.defaultTime = defaultTime;
    }

    @Override
    public int compareTo(@NonNull SteamOvenBean o) {
        return 0;
    }
}
