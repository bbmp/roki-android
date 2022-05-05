package com.mxchip.ftc_service;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ServiceThread implements Runnable {
	Socket s = null;
	byte[] buffer = new byte[2048];
	boolean recv = true;
	String data = "";
	String name, model, type, category, uuid, sn, mac, manufacturer, version;
	int count = 0;
	private FTC_Listener listener = null;
	// ���߳������Socket���Ӧ��������
	DataInputStream serverIn = null;
	private OutputStream outputStream = null;

	public ServiceThread(Socket s, FTC_Listener listener2) {
		this.listener = listener2;
		this.s = s;
	}

	@Override
	public void run() {
		try {
			boolean isCorrectData = false;
			int allCount = 0;
			int allOffset = 0;
			while (!isCorrectData) {
				try {
					serverIn = new DataInputStream(new BufferedInputStream(
							s.getInputStream()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				int sumCount = serverIn.available();
				allOffset = allOffset + sumCount;
				if (sumCount == 0) {
//					Log.e("====", "sum:0");
				} else {
//					Log.e("====", "sum:" + String.valueOf(sumCount));

					byte[] buf = new byte[2048];
					int read = 0;
					int offset = 0;
					while (offset < sumCount
							&& (read = serverIn.read(buf)) != -1) {
						data += new String(buf).trim();
						offset += read;
//						Log.e("====", "offset:" + offset);
					}

					if (allCount == 0 && !"".equals(data)) {
						if (data.indexOf("Content-Length:") > -1) {
							int startIndex = data.indexOf("Content-Length:") + 15;
//							Log.e("====", "startIndex:" + startIndex);
							int endIndex = data.indexOf("\r\n\r\n");
//							Log.e("====", "endIndex:" + endIndex);
							String strLength = data.substring(startIndex,
									endIndex).trim();
//							Log.e("====", "strLength:" + strLength);
							allCount = 85 + Integer.parseInt(strLength);
//							Log.e("====", "allCount:" + allCount);
						}
					}

					if (allOffset + 1 >= allCount) {
						isCorrectData = true;

					}
				}
			}
			outputStream = s.getOutputStream();
			outputStream
					.write("HTTP/1.1 202 Accepted\r\nContent-Type: application/json\r\nConnection: keep-alive\r\n\r\n"
							.getBytes());
			Log.e("====", "data:" + data.trim());
			data = data.substring(data.indexOf("\r\n\r\n") + 4);
			if (null != listener)
				listener.onFTCfinished(s, data.trim());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
