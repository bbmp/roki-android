package com.legent.io.buses.collections;

import android.content.Context;

import com.google.common.base.Preconditions;
import com.legent.VoidCallback;
import com.legent.io.buses.AbsOioBus;
import com.legent.io.msgs.IMsg;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class OioUdpClient extends AbsOioBus {

	protected String host;
	protected int port;

	protected InetSocketAddress address;
	protected DatagramSocket socket;
	protected DatagramPacket dpSend, dpRecv;

	@Override
	public void init(Context cx, Object... params) {
		super.init(cx, params);

		Preconditions.checkNotNull(params, "params is null");
		Preconditions
				.checkState(params.length >= 2, "invalid length of params");

		host = (String) params[0];
		port = (Integer) params[1];

		address = new InetSocketAddress(host, port);
	}

	@Override
	protected void onOpen(VoidCallback callback) {
		try {
			socket = new DatagramSocket();
			onCallSuccess(callback);
		} catch (Exception e) {
			onCallFailure(callback, e.getCause());
		}
	}

	@Override
	protected void onClose(VoidCallback callback) {
		try {
			if (socket != null) {
				socket.close();
			}
			onCallSuccess(callback);
		} catch (Exception e) {
			onCallFailure(callback, e.getCause());
		} finally {
			socket = null;
			address = null;
			dpSend = null;
			dpRecv = null;
		}
	}

	@Override
	public void send(IMsg msg, VoidCallback callback) {
		try {
			byte[] data = msg.getBytes();
			dpSend = new DatagramPacket(data, data.length, address);
			socket.send(dpSend);

			onCallSuccess(callback);
		} catch (Exception e) {
			onCallFailure(callback, e.getCause());
		} finally {
			dpSend = null;
		}
	}

	@Override
	protected int read(byte[] buffer) throws Exception {
		try {
			dpRecv = new DatagramPacket(buffer, buffer.length);
			socket.receive(dpRecv);
			int count = dpRecv.getLength();
			return count;
		} finally {
			dpRecv = null;
		}
	}

}
