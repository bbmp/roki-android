package com.legent.io.senders;


import com.legent.io.msgs.IMsg;

abstract public class AbsSenderWithNoWatchdog<Msg extends IMsg> extends AbsSender<Msg> {

	@Override
	public boolean match(String deviceId, Msg resMsg) {
		checkTimeout();
		return super.match(deviceId, resMsg);
	}

}
