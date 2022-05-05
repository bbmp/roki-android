package com.legent.services;

import com.google.common.eventbus.Subscribe;
import com.legent.events.ConnectionModeChangedEvent;
import com.legent.pojos.AbsObject;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;

abstract public class AbsService extends AbsObject {

	public AbsService() {
		EventUtils.regist(this);
	}

	@Override
	public void dispose() {
		super.dispose();
		EventUtils.unregist(this);
	}

	@Subscribe
	public void onEvent(ConnectionModeChangedEvent event) {
		int mode = event.connectionMode;
		switch (mode) {

		case ConnectivtyService.ConnectionMode_Broken://0
			onDisconnected();
			break;
		case ConnectivtyService.ConnectionMode_Wifi://1
			onConnected(true);
			break;
		case ConnectivtyService.ConnectionMode_Mobil://2
			onConnected(false);
			break;

		default:
			break;
		}
	}

	protected void onDisconnected() {
	}

	protected void onConnected(boolean isWifi) {
	}

}
