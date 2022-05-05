package com.legent.plat.io.device.finder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * 上海庆科EasyLink V2.3
 */
public class EasyLink23 implements IEasyLink {
    private final static String syncHString = "abcdefghijklmnopqrstuvw";

    private static EasyLink23 instance = new EasyLink23();
    private boolean isRunning;
    private Thread sendingThread;
    private String head = "239.118.0.0";
    private String ip;
    private String ssid;
    private String key;
    private String userInfo;
    private EasyLink23() {
    }

    synchronized public static EasyLink23 getInstance() {
        return instance;
    }

    private static int getRandomNumber() {
        int num = new Random().nextInt(65536);
        if (num < 10000)
            return 65523;
        else
            return num;
    }

    @Override
    public void start(String ssid, String wifiPwd, String userInfo) {

        this.ssid = ssid;
        this.key = wifiPwd;
        this.userInfo = userInfo;

        isRunning = true;
        sendingThread = new Thread(new Runnable() {
            public void run() {
                try {
                    send();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sendingThread.setName("EasyLink SettingThread");
        sendingThread.start();
    }

    @Override
    public void stop() {
        if (!isRunning)
            return;

        isRunning = false;
        if (sendingThread != null) {

            try {
                if (Thread.currentThread() != sendingThread) {
                    this.sendingThread.join();
                }
            } catch (Exception ex) {
            } finally {
                sendingThread = null;
            }
        }
    }

    private void send() {

        try {
            while (isRunning) {
                sendSync(ssid, key, userInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        } finally {
            isRunning = false;
        }
    }

    protected void sendSync(String ssid, String key, String user_info)
            throws InterruptedException, IOException {
        InetSocketAddress sockAddr;
        byte[] syncHBuffer = syncHString.getBytes();
        byte[] s_key = new byte[key.length()];
        byte[] s_ssid = new byte[ssid.getBytes(StandardCharsets.UTF_8).length];
        byte[] data = new byte[2];
        byte[] userinfo = null;
        int userlength = user_info.getBytes(StandardCharsets.UTF_8).length;

        if (userlength == 0) {
            userlength++;
            userinfo = new byte[1];
            userinfo[0] = 0;
        } else {
            userinfo = new byte[userlength];
            System.arraycopy(userinfo, 0, userinfo, 0, userlength);
        }
        if (null != user_info) {
            userlength = user_info.getBytes(StandardCharsets.UTF_8).length;
            userinfo = new byte[userlength];
            System.arraycopy(user_info.getBytes(StandardCharsets.UTF_8), 0, userinfo, 0,
                    userlength);
        }

        System.arraycopy(key.getBytes(), 0, s_key, 0, key.length());
        System.arraycopy(ssid.getBytes(StandardCharsets.UTF_8), 0, s_ssid, 0,
                ssid.getBytes(StandardCharsets.UTF_8).length);
        data[0] = (byte) s_ssid.length;
        data[1] = (byte) s_key.length;
        byte[] temp = Helper.byteMerger(s_ssid, s_key);
        data = Helper.byteMerger(data, temp);

        for (int i = 0; i < 5; i++) {
            sockAddr = new InetSocketAddress(InetAddress.getByName(head),
                    getRandomNumber());
            sendData(new DatagramPacket(syncHBuffer, 20, sockAddr), head);
            Thread.sleep(10L);
        }
        if (userlength == 0) {
            for (int k = 0; k < data.length; k += 2) {
                if (k + 1 < data.length)
                    ip = "239.126." + (data[k] & 0xff) + "."
                            + (data[k + 1] & 0xff);
                else
                    ip = "239.126." + (data[k] & 0xff) + ".0";
                sockAddr = new InetSocketAddress(InetAddress.getByName(ip),
                        getRandomNumber());
                byte[] bbbb = new byte[k / 2 + 20];
                sendData(new DatagramPacket(bbbb, k / 2 + 20, sockAddr), ip);
                Thread.sleep(10L);
            }
        } else {
            if (data.length % 2 == 0) {
                if (userinfo.length == 0) {
                    byte[] temp_length = {(byte) userlength, 0, 0};
                    data = Helper.byteMerger(data, temp_length);
                } else {
                    byte[] temp_length = {(byte) userlength, 0};
                    data = Helper.byteMerger(data, temp_length);
                }
            } else {
                byte[] temp_length = {0, (byte) userlength, 0};
                data = Helper.byteMerger(data, temp_length);
            }
            data = Helper.byteMerger(data, userinfo);
            for (int k = 0; k < data.length; k += 2) {
                if (k + 1 < data.length)
                    ip = "239.126." + (data[k] & 0xff) + "."
                            + (data[k + 1] & 0xff);
                else
                    ip = "239.126." + (data[k] & 0xff) + ".0";
                sockAddr = new InetSocketAddress(InetAddress.getByName(ip),
                        getRandomNumber());
                byte[] bbbb = new byte[k / 2 + 20];
                sendData(new DatagramPacket(bbbb, k / 2 + 20, sockAddr), ip);
                Thread.sleep(10L);
            }
        }
    }

    protected void sendData(DatagramPacket datagramPacket, String ip_addr)
            throws IOException {
        MulticastSocket sock = null;
        sock = new MulticastSocket(54064);
        sock.joinGroup(InetAddress.getByName(ip_addr));
        sock.send(datagramPacket);
        sock.close();
    }

    static class Helper {
        static public byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
            byte[] byte_3 = new byte[byte_1.length + byte_2.length];
            System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
            System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
            return byte_3;
        }
    }

}
