package com.robam.softap;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import static com.robam.softap.Constants.DEVICE_READY_FOR_TRANSFER_DATA;
import static com.robam.softap.Constants.ERROR_CODE;

public class HandlerThread implements Runnable {
    public static final String TAG = "HandlerThread";
    private Socket socket;
    private String mSsid;
    private String mPassword;
    private NetworkConfig.IDiscoveryDeviceListener mIDiscoveryDeviceListener;

    public HandlerThread(Socket client, String ssid, String password, NetworkConfig.IDiscoveryDeviceListener iDiscoveryDeviceListener) {
        socket = client;
        this.mSsid = ssid;
        this.mPassword = password;
        this.mIDiscoveryDeviceListener = iDiscoveryDeviceListener;

    }

    @Override
    public void run() {
        /**
         *  读取 client 端发送过来的数据
         */
        Log.d("获取网关信息", socket.toString());
        ByteArrayOutputStream deviceReadySteam = getByteArrayFromStream(socket);
        int readyCode = decryptedByteAndGetCode(deviceReadySteam.toByteArray());
        Log.d(TAG, "fetchDataFromSocket: readyCode = " + readyCode);
        if (readyCode == DEVICE_READY_FOR_TRANSFER_DATA) {
            if (mIDiscoveryDeviceListener != null) {
                mIDiscoveryDeviceListener.onAcceptedDeviceReadyData();
            }

            /**
             *  加密 wifi 信息数据
             */
            byte[] encryptedContent = encryptWifiInfoToByte(mSsid, mPassword);
            Log.d(TAG, "fetchDataFromSocket: encryptedContent = " + Arrays.toString(encryptedContent));
            /**
             *  发送数据给设备端
             */
            int sendStatus = sendDataToWifiDevice(socket, encryptedContent);
            mIDiscoveryDeviceListener.onSendDataToDevice(sendStatus);

            /**
             *
             *  再次接受 设备(固件)发送过来的数据
             */
            ByteArrayOutputStream byteArrayOutputStream = getByteArrayFromStream(socket);
            int returnCode = decryptedByteAndGetCode(byteArrayOutputStream.toByteArray());
            Log.d(TAG, "run: returnCode = " + returnCode);
            if (mIDiscoveryDeviceListener != null) {
                mIDiscoveryDeviceListener.onFetchDataFromDeviceFeedback(returnCode);
            }

        } else {
            if (mIDiscoveryDeviceListener != null) {
                mIDiscoveryDeviceListener.onDiscoveryFail("no ready message");
            }
        }
    }

    private ByteArrayOutputStream getByteArrayFromStream(Socket socket) {
        InputStream inputStream = null;
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream tempOutStream = null;
        while (true) {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            try {
                if ((len = inputStream.read(buffer)) == -1) {
                    break;
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            outStream.write(buffer, 0, len);
            Log.d(TAG, "outStream.toByteArray = " + Arrays.toString(outStream.toByteArray()));
            if (outStream.toByteArray().length > 0) {
                tempOutStream = outStream;
                break;
            }
        }
        return tempOutStream;
    }


    private byte[] encryptWifiInfoToByte(String ssid, String password) {
        Map<String, String> wifiInfoMap = new HashMap<String, String>();
        wifiInfoMap.put("ssid", ssid);
        wifiInfoMap.put("key", password);
        wifiInfoMap.put("extraData", "{\"AppType\":\"RKDRD\",\"AppId\":\"LmbaVG70I9tg\",\"UserId\":\"3305047125\"}");

        Gson gson = new Gson();
        String wifiInfoGson = gson.toJson(wifiInfoMap);
        Log.d(TAG, "encryptWifiInfoToByte: wifiInfoGson = " + wifiInfoGson);
        byte[] key = new byte[4];
        byte[] wifiInfoGsonBytes = wifiInfoGson.getBytes();
        /**
         * 明文的前四个字节
         */
        Random random = new Random(0);
        random.nextBytes(key);

        /**
         * 对 jSon 数据加密
         */
        byte[] encryptedJsonContent = AESUtils.AESEncode(wifiInfoGsonBytes, DigestUtils.md5(key));
        if (encryptedJsonContent == null) {
            return null;
        }
        byte[] encryptedBytes = new byte[encryptedJsonContent.length + 8];
        /**
         *  加密后的json 数据, 拷贝到 encryptedBytes 后面
         */
        System.arraycopy(encryptedJsonContent, 0, encryptedBytes, 8, encryptedJsonContent.length);
        /**
         *  key 作为明文, 拷贝到 encryptedBytes 前面四位
         */
        System.arraycopy(key, 0, encryptedBytes, 0, key.length);
        return encryptedBytes;
    }

    private int sendDataToWifiDevice(Socket acceptSocket, byte[] encryptedContent) {
        int status = -1;
        OutputStream outputStream = null;
        while (true) {
            try {
                if (acceptSocket != null) {
                    outputStream = (acceptSocket.getOutputStream());
                }
                Log.d(TAG, "fetchDataFromSocket: outputStream1 " + outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (outputStream != null) {
                    outputStream.write(encryptedContent);
                    Log.d(TAG, "sendDataToWifiDevice: write all data ");
                    status = 0;
                    break;
                }
            } catch (IOException e) {
                Log.d(TAG, "sendDataToWifiDevice: 99999999999999999");
                e.printStackTrace();
            }
        }
        return status;
    }

    private int decryptedByteAndGetCode(byte[] encryptedByte) {
        /**
         * 取出前面的四个字节
         */
        byte[] key = new byte[4];
        System.arraycopy(encryptedByte, 0, key, 0, key.length);
        /**
         * 从第8位开始, 去除后面的加密的json内容
         */
        byte[] jsonByte = new byte[encryptedByte.length - 8];
        System.arraycopy(encryptedByte, 8, jsonByte, 0, jsonByte.length);
        String decryptedByte = null;
        decryptedByte = AESUtils.AESDecode(jsonByte, DigestUtils.md5(key));

        Log.d(TAG, "decryptedByteAndGetCode: decryptedByte = " + decryptedByte);
        int readyCode = ERROR_CODE;
        try {
            readyCode = new JSONObject(decryptedByte).getInt("code");
        } catch (NullPointerException nullPointerException) {

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "decryptedByteAndGetCode: readyCode = " + readyCode);
        return readyCode;
    }
}
