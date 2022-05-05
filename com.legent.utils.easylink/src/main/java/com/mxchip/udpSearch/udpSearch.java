package com.mxchip.udpSearch;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class udpSearch {

	private String udpresult = null;
	private DatagramSocket socketSend, socketReceive = null;
	boolean bFindGateway = false;

	/**
	 * Send UDP broadcast
	 * 
	 * @param BroadcastData
	 * @param BroadcastPort
	 * @param BroadcastIP
	 * @param listener
	 */
	public void doUdpFind(final String BroadcastData, final int BroadcastPort,
			final String BroadcastIP, final udpSearch_Listener listener) {
		try {
			socketSend = socketReceive = new DatagramSocket();
			socketSend.setReuseAddress(true);
			socketReceive.setReuseAddress(true);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					socketSend.setBroadcast(true);
					String str = BroadcastData;
					byte[] dataSend = str.getBytes();

					InetAddress address = InetAddress.getByName(BroadcastIP);
					DatagramPacket sendPacket = new DatagramPacket(dataSend,
							dataSend.length, address, BroadcastPort);
					int i = 0;

					while (!bFindGateway && i < 5) {
						i++;
						socketSend.send(sendPacket);
						Thread.sleep(1000l);
					}
					if (null == udpresult)
						listener.onDeviceFound(null);
					bFindGateway = true;
					socketSend.close();
					socketReceive.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				byte[] dataReceive = new byte[1024];
				DatagramPacket packetReceive = new DatagramPacket(dataReceive,
						dataReceive.length);

				while (!bFindGateway) {
					dataReceive = new byte[1024];
					packetReceive = new DatagramPacket(dataReceive,
							dataReceive.length);
					try {
						socketReceive.receive(packetReceive);

						// System.out.println("receive message is ok.");
						// ip = packetReceive.getAddress().getHostAddress();
						udpresult = new String(packetReceive.getData(),
								packetReceive.getOffset(),
								packetReceive.getLength());
						// Log.i("", "data:" + udpresult);
						// Log.i("", "ip:" + ip);
						listener.onDeviceFound(udpresult.trim());
					} catch (IOException e) {
						// e.printStackTrace();
					}
				}

			}
		}).start();
	}

	public void stopUdpFind() {
		bFindGateway = true;
	}
}
