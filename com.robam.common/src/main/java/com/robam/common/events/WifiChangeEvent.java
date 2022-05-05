package com.robam.common.events;

/**
 * Created by Administrator on 2016/6/15.
 */
public class WifiChangeEvent {
    String ssid;
    String passwd;

    public WifiChangeEvent(String ssid, String passwd) {
        this.ssid = ssid;
        this.passwd = passwd;
    }

    public String getSsid() {
        return ssid;
    }

    public String getPasswd() {
        return passwd;
    }
}
