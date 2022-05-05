package com.legent.io.buses;

import com.legent.io.msgs.collections.BytesMsg;
import com.legent.utils.LogUtils;

/**
 * 阻塞式总线
 * 
 * @author sylar
 * 
 */
abstract public class AbsOioBus extends AbsBus {

	protected final static int Buffer_Size = 1024;

	protected ReadThread thRead;

	abstract protected int read(byte[] buffer) throws Exception;

	@Override
	public void dispose() {
		super.dispose();
		stopReadThread();
	}

	@Override
	protected void onConnectionChanged(boolean isConnected) {
		super.onConnectionChanged(isConnected);

		if (isConnected) {
			startReadThread();
		} else {
			stopReadThread();
		}
	}

	protected void startReadThread() {

		stopReadThread();

		thRead = new ReadThread();
		thRead.setName("bus read thread");
		thRead.start();
	}

	protected void stopReadThread() {
		if (thRead != null) {
			thRead.interrupt();
			thRead = null;
		}
	}

	protected int getBufferSize() {
		return Buffer_Size;
	}

	class ReadThread extends Thread {

		protected byte[] buffer;

		@Override
		public void run() {

			buffer = new byte[getBufferSize()];
			byte[] data;
			int count;
			LogUtils.i("20170210","isConnected:"+isConnected);
			while (isConnected && !isInterrupted()) {

				try {
					count = read(buffer);

					if (count > 0) {
						data = new byte[count];
						System.arraycopy(buffer, 0, data, 0, count);

						BytesMsg msg = new BytesMsg(data);
						AbsOioBus.this.onMsgReceived(msg);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
