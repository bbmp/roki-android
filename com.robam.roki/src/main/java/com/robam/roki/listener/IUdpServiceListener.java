package com.robam.roki.listener;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/3/19.
 * PS: 监听Udp服务的接口.
 */
public interface IUdpServiceListener {
    /**
     * 链接设备结果
     * @param result
     */
    void onDeviceResult(String result);
}
