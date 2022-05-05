package com.robam.softap;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

//import com.robam.softap.Constants;
//
//import static com.robam.softap.Constants.DISCOVERY_FAILURE_REASON_SOCKET_ACCEPT_FAILED;
//import static com.robam.softap.Constants.DISCOVERY_FAILURE_REASON_SOCKET_TIMEOUT;
//import static com.robam.softap.Constants.PORT;
//import static com.robam.softap.Constants.SOCKET_ACCEPT_TIME_OUT;

public class NetworkConfig {
    public static final String TAG = "NetworkConfig";
    private boolean mIsStopSoftAp = true;
    private ServerSocket mTcpServerSocket;
    IDiscoveryDeviceListener mIDiscoveryDeviceListener;
    CountDownTimer countDownTimer = new CountDownTimer(Constants.SOCKET_ACCEPT_TIME_OUT, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            Log.d(TAG, "onTick: 111111111");
        }

        @Override
        public void onFinish() {
            if (mIDiscoveryDeviceListener != null) {
                mIDiscoveryDeviceListener.onDiscoveryFail(Constants.DISCOVERY_FAILURE_REASON_SOCKET_TIMEOUT);
            }
            countDownTimer.cancel();
        }
    };

    public void setIDiscoverySoftApListener(IDiscoveryDeviceListener softApListener) {
        mIDiscoveryDeviceListener = softApListener;
    }

    public interface IDiscoveryDeviceListener {
        /**
         * 发现设备
         */
        void onDiscoverySoftAp();

        /**
         * ServerSocket accept 超时的
         */
        void onServerSocketAccept();

        /**
         * 接收到了固件已经准备好配网状态信息
         */
        void onAcceptedDeviceReadyData();


        /**
         * app 端发送信息给 固件
         *
         * @param sendStatus, 0 表示成功, -1 表示失败
         */
        void onSendDataToDevice(int sendStatus);

        /**
         * 接收到了 固件返回回来的状态信息
         *
         * @param feedbackCode
         */
        void onFetchDataFromDeviceFeedback(int feedbackCode);

        /**
         * 发现设备失败.
         *
         * @param reason
         */
        void onDiscoveryFail(String reason);
    }

    /**
     * 发现 设备发出的 WiFi 的回调
     */
    public interface IWiFiDiscoveryCallBack {
        void onDiscoverySuccess();

        void onDiscoveryFail();
    }


    public void startSoftAp(Context context, String mWifiSSID, String wifiPassword, IDiscoveryDeviceListener iDiscoveryDeviceListener) {
        if (mIsStopSoftAp) {
            startCheckSoftApVersion(context, mWifiSSID, wifiPassword, iDiscoveryDeviceListener);
        }
    }

    private void startCheckSoftApVersion(Context context, String ssid, String password, IDiscoveryDeviceListener iDiscoveryDeviceListener) {
        mIsStopSoftAp = false;
        try {
            mTcpServerSocket = new ServerSocket(Constants.PORT);
//            mTcpServerSocket = new ServerSocket(Constants.PORT,50,InetAddress.getByName("192.168.1.114"));

        } catch (BindException bindException) {
            bindException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        if (mTcpServerSocket == null) {
            return;
        }
        Log.w(TAG, "startCheckSoftApVersion < ----- 发现设备热点  ------>");
        String threadName = Thread.currentThread().getName();
        Log.d(TAG, "run: threadName = " + threadName + "-" + Thread.currentThread().getId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                getMsgFromTcpSocket(ssid, password);
            }
        }).start();
    }

    public void stopCheckSoftAP() {
        mIsStopSoftAp = true;
    }


    private void getMsgFromTcpSocket(String ssid, String password) {
        Socket acceptSocket = null;


        try {
            countDownTimer.start();
            acceptSocket = mTcpServerSocket.accept();
            Log.d(TAG, "getMsgFromTcpSocket: ");
            if (mIDiscoveryDeviceListener != null) {
                mIDiscoveryDeviceListener.onServerSocketAccept();
            }
            HandlerThread handlerThread = new HandlerThread(acceptSocket, ssid, password, mIDiscoveryDeviceListener);
            new Thread(handlerThread).start();
        } catch (IOException e) {
            if (mIDiscoveryDeviceListener != null) {
                mIDiscoveryDeviceListener.onDiscoveryFail(Constants.DISCOVERY_FAILURE_REASON_SOCKET_ACCEPT_FAILED);
            }
            e.printStackTrace();
        } finally {
            Log.d(TAG, "getMsgFromTcpSocket: finally");
            countDownTimer.cancel();
        }
    }

    public void stopSoftAp() {
        if (mTcpServerSocket != null) {
            try {
                mTcpServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
