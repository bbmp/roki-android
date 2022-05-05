package com.legent.plat.io.device.finder;

public interface IEasyLink {
    void start(String ssid, String wifiPwd, String userInfo);

    void stop();
}