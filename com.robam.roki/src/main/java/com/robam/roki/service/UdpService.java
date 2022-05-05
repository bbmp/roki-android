package com.robam.roki.service;


import com.legent.utils.LogUtils;
import com.robam.roki.listener.IUdpServiceListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/3/20.
 * PS: Not easy to write code, please indicate.
 */

public class UdpService {

    private static DatagramSocket ds;
    private int port = 10001;
    public UdpService() {
        try {
            ds = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void send(String data) {
        try {
            LogUtils.i("20190418","data:" + data);
            //2.把数据打成数据包
            byte[] buf = data.getBytes();
            InetAddress inetAddress = InetAddress.getByName("255.255.255.255");
            DatagramPacket p = new DatagramPacket(buf, buf.length, inetAddress, port);
            //3.发送数据
            ds.send(p);
        } catch (IOException e) {
            LogUtils.i("20190418","send e:" + e.toString());
            e.printStackTrace();

        }
    }

    public void receive(IUdpServiceListener listener) {
        try {
            //2.创建数据包
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            //3.调用方法接受数据包
            ds.receive(packet);
            //4.解析数据包
            byte[] data = packet.getData();
            int length = packet.getLength();
            String result = new String(data, 0, length);
            listener.onDeviceResult(result);
        } catch (IOException e) {
            LogUtils.i("20190418","receive e:" + e.toString());
            e.printStackTrace();
        }
    }

    public void releaseUdp() {
        //4.关闭资源
        if (ds != null && !ds.isClosed()) {
            ds.close();
        }
    }
}
