package com.legent.io.buses.collections;

import android.content.Context;

import com.google.common.base.Preconditions;
import com.legent.VoidCallback;
import com.legent.io.buses.AbsOioBus;
import com.legent.io.msgs.IMsg;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class OioTcpClient extends AbsOioBus {

	protected String host;
	protected int port;

	protected Socket socket;
	protected InputStream in;
	protected OutputStream out;

	@Override
	public void init(Context cx, Object... params) {
		super.init(cx, params);

		Preconditions.checkNotNull(params, "params is null");
		Preconditions
				.checkState(params.length >= 2, "invalid length of params");

		this.host = (String) params[0];
		this.port = (Integer) params[1];
	}

	@Override
	protected void onOpen(VoidCallback callback) {
		try {
			socket = new Socket(host, port);
			in = socket.getInputStream();
			out = socket.getOutputStream();

			onCallSuccess(callback);
		} catch (Exception e) {
			onCallFailure(callback, e.getCause());
		} finally {
		}
	}

	@Override
	protected void onClose(VoidCallback callback) {
		try {
			if (socket != null)
				socket.close();
			if (in != null)
				in.close();
			if (out != null)
				out.close();

			onCallSuccess(callback);
		} catch (Exception e) {
			onCallFailure(callback, e.getCause());
		} finally {
			socket = null;
			in = null;
			out = null;
		}
	}

	@Override
	public void send(IMsg msg, VoidCallback callback) {
		try {
			byte[] data = msg.getBytes();
			out.write(data);
			out.flush();

			onCallSuccess(callback);
		} catch (Exception e) {
			onCallFailure(callback, e.getCause());
		}
	}

	@Override
	protected int read(byte[] buffer) throws Exception {
		int count = in.read(buffer);
		return count;
	}

}
