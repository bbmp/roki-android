package com.legent.plat.pojos.dictionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

/**
 * Created by sylar on 15/7/30.
 */
public class ServerOpt extends AbsPojo {

     //private static final String DEFAULT_HOST = "mqtt.myroki.com";
    // protected static final String DEFAULT_HOST = "115.29.246.216";
    //protected static final String DEFAULT_HOST = "172.16.14.32";
    protected static final String DEFAULT_HOST = "118.178.157.97";

    /**
     * restful 服务器
     */
    @JsonProperty(defaultValue = DEFAULT_HOST)
    public String ecsHost;

    /**
     * restful 服务端口
     * 生产服务器端口80
     * 测试服务器端口8088
     * 本地测试机8081
     * 新架构测试环境http80
     */
    @JsonProperty(defaultValue = "80")
    public int ecsPort;

    /**
     * mqtt 服务器
     */
    @JsonProperty(defaultValue = DEFAULT_HOST)
    public String acsHost;

    /**
     * mqtt 服务端口
     */
    @JsonProperty(defaultValue = "1883")
    public int acsPort;

    /**
     * mqtt user
     */
    @JsonProperty(defaultValue = "smalKettle")
    public String mqttUser;

    /**
     * mqtt pwd
     */
    @JsonProperty(defaultValue = "smal2014")
    public String mqttPwd;


    public void set(String host) {
        set(host, 80, host, 1883);
    }

    public void set(String host, int ecsPort) {
        set(host, ecsPort, host, acsPort);
    }

    public void set(String host, int ecsPort, int acsPort) {
        set(host, ecsPort, host, acsPort);
    }

    public void set(String ecsHost, int ecsPort, String acsHost, int acsPort) {
        this.ecsHost = ecsHost;
        this.ecsPort = ecsPort;
        this.acsHost = acsHost;
        this.acsPort = acsPort;
    }

    public String getRestfulBaseUrl() {
        return String.format("http://%s:%s", ecsHost, ecsPort);
    }
}

