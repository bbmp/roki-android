package com.legent.plat.io.device.mqtt;

import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.legent.io.msgs.IMsg;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.plat.io.device.AbsPlatProtocol;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MqttProtocol extends AbsPlatProtocol {

	final int CMD_CODE_SIZE = 1;

	// -------------------------------------------------------------------------------
	// encode/decode
	// -------------------------------------------------------------------------------

	@Override
	public byte[] encode(IMsg msg) throws Exception{
		Preconditions.checkNotNull(msg);
		Preconditions.checkState(msg instanceof Msg);

		Msg reqMsg = (Msg) msg;
		ByteBuffer buf = ByteBuffer.allocate(BufferSize).order(BYTE_ORDER);
		byte[] tmp = null;

		// srcGuid
		String guid = reqMsg.getSource().getGuid();
		Preconditions.checkNotNull(guid, "guid is null");
		Preconditions.checkState(guid.length() == GUID_SIZE,
				"guid length error");
		tmp = guid.getBytes();
		buf.put(tmp);

		// cmdCode
		buf.put(MsgUtils.toByte(reqMsg.getID()));

		// data params
		onEncodeMsg(buf, reqMsg);

		// buf to byte[]
		byte[] data = new byte[buf.position()];
		System.arraycopy(buf.array(), 0, data, 0, data.length);
		buf.clear();
		buf = null;

		return data;
	}

	@Override
	public List<IMsg> decode(byte[] data, Object... params) throws Exception{
		List<Byte> byteList = new ArrayList<>();
		for (int i = 0; i <data.length ; i++) {
			byteList.add(data[i]);
		}
		LogUtils.i("20180608","byteList::"+byteList.toString());
		Preconditions.checkNotNull(data);
		Preconditions.checkNotNull(params);

		String topicString = (String) params[0];
		Topic topic = Topic.parse(topicString);
		Preconditions.checkNotNull(topic);

		List<IMsg> list = Lists.newArrayList();

		try {
			Preconditions.checkState(data.length >= GUID_SIZE + CMD_CODE_SIZE,
					"数据长度不符");

			int offset = 0;
			// guid
			String srcGuid = MsgUtils.getString(data, offset, GUID_SIZE);
			offset += GUID_SIZE;

			// cmd id
			short code = MsgUtils.getShort(data[offset++]);
			Msg msg = Msg.newIncomingMsg(code, srcGuid);

			// payload
			byte[] payload = new byte[data.length - offset];
			System.arraycopy(data, offset, payload, 0, payload.length);

			// paser payload
			onDecodeMsg(msg, payload);
			list.add(msg);

		} catch (Exception e) {
			String log = String.format(
					"mqtt decode error. topic:%s\nerror:%s\nbyte[]:%s",
					topicString, e.getMessage(), StringUtils.bytes2Hex(data));
			Log.d(TAG, log);
			e.printStackTrace();
		}

		return list;
	}

}
