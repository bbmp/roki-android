package com.mxchip.easylink_v2;

import com.mxchip.helper.Helper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.Random;

public class EasyLink_v2 {
	private static boolean stopSending;
	private static String head = "239.118.0.0";
	private static String ip;
	private static String syncHString = "abcdefghijklmnopqrstuvw";
	private static EasyLink_v2 e2;
    private byte[] key = new byte[65];
    private byte[] ssid = new byte[65];
    private byte[] user_info = new byte[65];

	private EasyLink_v2() {
		stopSending = false;
	}
	
	public static EasyLink_v2 getInstence() {
		if (e2 == null) {
			e2 = new EasyLink_v2();
		}
		return e2;
	}

	protected static void sendData(DatagramPacket datagramPacket, String ip_addr)
			throws IOException {
		MulticastSocket sock = null;
		sock = new MulticastSocket(54064);
		sock.joinGroup(InetAddress.getByName(ip_addr));
		// sock.setReuseAddress(true);
		sock.send(datagramPacket);
		sock.close();
	}

	private static int getRandomNumber() {
		int num = new Random().nextInt(65536);
		if (num < 10000)
			return 65523;
		else
			return num;
	}

	/**
	 * Start EasyLink
	 */
	public void transmitSettings(byte[] Ssid, byte[] Key, byte[] Userinfo) {
		this.ssid = Ssid;
		this.key = Key;
		this.user_info = Userinfo;
//		Log.e("TTTTTTTTTTT", Helper.ConvertHexByteArrayToString(Userinfo));
		new Thread(new Runnable() {
			@Override
			public void run() {

				stopSending = false;
				send();
			}
		}).start();
	}

	private void send() {
		while (!stopSending) {
			try {
				sendSync();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void sendSync()
			throws InterruptedException, IOException {
		InetSocketAddress sockAddr;
		byte[] syncHBuffer = syncHString.getBytes();
		byte[] data = new byte[2];
		int userlength = user_info.length;

		if (userlength == 0) {
			userlength++;
			user_info = new byte[1];
			user_info[0] = 0;
		}
		// if(null!=user_info){
		// userlength = user_info.getBytes("UTF-8").length;
		// userinfo = new byte[userlength];
		// System.arraycopy(user_info.getBytes("UTF-8"), 0, userinfo, 0,
		// userlength);
		// }

		data[0] = (byte) ssid.length;
		data[1] = (byte) key.length;
		byte[] temp = Helper.byteMerger(ssid, key);
		data = Helper.byteMerger(data, temp);

		for (int i = 0; i < 5; i++) {
			sockAddr = new InetSocketAddress(InetAddress.getByName(head),
					getRandomNumber());
			sendData(new DatagramPacket(syncHBuffer, 20, sockAddr), head);
			Thread.sleep(10);
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
				Thread.sleep(10);
			}
		} else {
			if (data.length % 2 == 0) {
				if (user_info.length == 0) {
					byte[] temp_length = { (byte) userlength, 0, 0 };
					data = Helper.byteMerger(data, temp_length);
				} else {
					byte[] temp_length = { (byte) userlength, 0 };
					data = Helper.byteMerger(data, temp_length);
				}
			} else {
				byte[] temp_length = { 0, (byte) userlength, 0 };
				data = Helper.byteMerger(data, temp_length);
			}
			data = Helper.byteMerger(data, user_info);
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
				Thread.sleep(10);
			}
		}
	}

	/**
	 * Stop EasyLink
	 */
	public void stopTransmitting() {
		stopSending = true;
	}
}
