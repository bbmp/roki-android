package com.robam.roki.model.bean;

/**
 * Created by Administrator on 2017/8/3.
 */

public class City {

    public static String cityName;
    public static Integer cityCode;

    public static void setCityName(String cityna) {
        cityName = cityna;
    }

    public static void setCityCode(Integer cityCo) {
        cityCode = cityCo;
    }

    public static Integer getCityCode() {
        return cityCode;
    }

    public String getCityName() {
        return cityName;
    }
}
