package com.legent.plat.pojos.dictionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsKeyPojo;
import com.legent.utils.api.ResourcesUtils;

/**
 * Created by sylar on 15/6/3.
 */
public class DeviceType extends AbsKeyPojo<String> {

    @JsonProperty
    protected String id;

    @JsonProperty
    protected String name;

    @JsonProperty
    protected String dc;

    /**
     * 设备编码在MQTT协议中的对应的2字节编码
     */
    @JsonProperty
    public String mqttId;

    @JsonProperty
    public String tag;

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getID() {
        return id;
    }


    @Override
    public String getName() {
//        LogUtils.i("20180120","name:"+name);
        return ResourcesUtils.getStringOrFromRes(name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }


    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    @Override
    public String toString() {
        return getName();
    }
}
