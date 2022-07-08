package com.robam.softap;

 public class Constants {
    /**
     * 本地开启的 ServerSocket 的端口号
     */
    public static final int PORT = 30123;

    /**
     * ServerSocket 接受 client 端的,的超时
     */
    public static final int SOCKET_ACCEPT_TIME_OUT = 120 * 1000;

    /**
     * 模块准备就绪, 表明 自有 app, 可以开始传输配网数据
     */
    public static final int DEVICE_READY_FOR_TRANSFER_DATA = 100;
    /**
     * 设备已经收到配网的数据
     */
    public static final int FEEDBACK_SEND_DATA_SUCCESS_CODE = 0;

    /**
     *  固件设备已经配网返回信息
     */
    public static final int FEEDBACK_DEVICE_ALREADY_CONFIG = 1004;
    /**
     *   固件设备其他状态的信息
     */
    public static final int FEEDBACK_DEVICE_OTHER_STATUS_CODE = 1000;

    public static final int ERROR_CODE = -1;

    public static final String WIFI_ENCRYPT_WPA = "WPA";
    public static final String WIFI_ENCRYPT_WEP = "WEP";
    public static final String WIFI_ENCRYPT_OPEN = "OPEN";

    /**
     *  设备(也就是固件) 需要连接的WiFi信息, 也就是要送给 设备(固件)端的信息,然后利用这个信息去连接网络
     */
    public static final String DEVICE_CONNECT_WIFI_SSID = "DKB001";
    /**
     *  设备(也就是固件) 需要连接的 SSID 信息, 也就是要送给 设备(固件)端的信息,然后利用这个信息去连接网络
     */
    public static final String DEVICE_CONNECT_WIFI_PASSWORD = "robamDKB";

    /**
     *  设备(固件)发射出的 WiFi, App 一开始需要代码的方式连接上这个 WiFi
     */
//    public static final String MXHCIP_LIGHT_WIFI_SSID = "ROKI-OHOS-";
     public static final String MXHCIP_LIGHT_WIFI_SSID = "ROKI-";

     public static final String MXHCIP_LIGHT_WIFI_PASSWORD = "roki123456";

    public static final String DISCOVERY_FAILURE_REASON_SOCKET_TIMEOUT = "server socket timeout";
    public static final String DISCOVERY_FAILURE_REASON_SOCKET_ACCEPT_FAILED = "tcp server socket accept failed";
}
