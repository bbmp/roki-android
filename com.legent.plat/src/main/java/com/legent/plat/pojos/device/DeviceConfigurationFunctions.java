package com.legent.plat.pojos.device;

import android.os.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

import java.io.Serializable;
import java.util.List;


/**
 * Created by 14807 on 2018/4/4.
 */

public class DeviceConfigurationFunctions extends AbsPojo implements Serializable {

    @JsonProperty("id")
    public long id;

    @JsonProperty("functionCode")
    public String functionCode;

    @JsonProperty("work_mode")
    public String work_mode;

    @JsonProperty("functionName")
    public String functionName;

    @JsonProperty("deviceCategory")
    public String deviceCategory;

    @JsonProperty("deviceType")
    public String deviceType;

    @JsonProperty("functionType")//类型
    public String functionType;

    @JsonProperty("backgroundImg")
    public String backgroundImg;//背景图

    @JsonProperty("backgroundImgH")
    public String backgroundImgH;

    @JsonProperty("functionParams")
    public String functionParams;

    @JsonProperty("subViewName")
    public String subViewName;

    @JsonProperty("subView")
    public SubView subView;

    @JsonProperty("msg")
    public String msg;

}
