package com.legent.plat.io.device.mqtt;

import android.content.Context;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.io.buses.IBus;
import com.legent.io.channels.AbsChannel;
import com.legent.io.protocols.IProtocol;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectedNoticEvent;

import java.util.List;

public class MqttChannel extends AbsChannel {


	static private MqttChannel instance = new MqttChannel();

	static synchronized public MqttChannel getInstance() {
		return instance;
	}

	protected boolean isInited;

	private MqttChannel() {

	}

	@Override
	protected IBus createBus() {
		return new MqttBus();
	}

	@Override
	protected IProtocol createProtocol() {
		return new MqttProtocol();
	}

	@Override
	public void init(Context cx, Object... params) {
		super.init(cx, params);
		MqttBus.MqttParams busPrams = new MqttBus.MqttParams(Plat.appGuid);
		bus.init(cx, busPrams);
		isInited = true;
	}

	@Override
	protected void onConnectionChanged(boolean isConnected) {
		super.onConnectionChanged(isConnected);

		if (isConnected) {
			String topic = Topic.newUnicastTopic(Plat.appGuid).getTopic();
			registTipic(topic);
		}
	}

	@Subscribe
	public void onEvent(DeviceConnectedNoticEvent event) {
		List<String> topics = Topic.getWillRegistTopics(event.deviceInfo);
		registTipics(topics);
	}


	// -------------------------------------------------------------------------------
	// topic
	// -------------------------------------------------------------------------------
	
	public void regist(String deviceId) {
		List<String> topics = Topic.getWillRegistTopics(deviceId);
		registTipics(topics);
	}

	public void unregist(String deviceId) {
		List<String> topics = Topic.getWillRegistTopics(deviceId);
		unregistTipics(topics);
	}

	public void registTipics(List<String> topicList) {
		if (!isInited)
			return;
		((MqttBus) bus).subscribe(topicList);
	}

	public void unregistTipics(List<String> topicList) {
		if (!isInited)
			return;
		((MqttBus) bus).unsubscribe(topicList);
	}

    private void registTipic(String topic) {
		if(topic == null)return;
		List<String> topics = Lists.newArrayList();
		topics.add(topic);
		registTipics(topics);
	}

}
