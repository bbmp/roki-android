package com.legent.io.msgs.collections;

import com.legent.io.msgs.AbsMsg;
import com.legent.utils.StringUtils;

public class BytesMsg extends AbsMsg {

	public BytesMsg(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		if (data != null)
			return StringUtils.bytes2Hex(data);
		else
			return getClass().getSimpleName();
	}
}
