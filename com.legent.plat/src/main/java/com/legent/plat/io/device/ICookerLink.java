package com.legent.plat.io.device;

import com.legent.Callback;
import com.legent.plat.pojos.device.DeviceInfo;

/**
 * Created by Dell on 2018/6/14.
 */

public interface ICookerLink {

    void start(String wifiSsid, String wifiPwd, int timeout,
               Callback<DeviceInfo> callback);
    void stop();
}
