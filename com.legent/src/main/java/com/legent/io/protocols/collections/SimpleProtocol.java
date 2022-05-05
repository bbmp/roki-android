package com.legent.io.protocols.collections;

import com.google.common.collect.Lists;
import com.legent.io.msgs.IMsg;
import com.legent.io.msgs.collections.BytesMsg;
import com.legent.io.protocols.AbsProtocol;

import java.util.List;

public class SimpleProtocol extends AbsProtocol {

	@Override
	public byte[] encode(IMsg msg) {
		return msg.getBytes();
	}

	@Override
	public List<IMsg> decode(byte[] data, Object... params) {

		BytesMsg msg = new BytesMsg(data);
		if (params != null && params.length > 0) {
			msg.setTag(params[0]);
		}

		List<IMsg> msgs = Lists.newArrayList();
		msgs.add(msg);
		return msgs;
	}

}
