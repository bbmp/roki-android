package com.legent.plat.io.device.mqtt;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.legent.plat.pojos.device.DeviceGuid;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.device.SubDeviceInfo;

import java.util.List;

abstract public class Topic implements ITopic {

	final static public int TopicType_Unicast = 1;
	final static public int TopicType_Boardcast = 2;

	final static protected String TOPIC_UNICAST = "u";//单播
	final static protected String TOPIC_BROADCAST = "b";//广播
	final static protected String TOPIC_FORMAT = "/%s/%s/%s";

	protected String type, number;
	protected DeviceGuid guid;

	public Topic() {
	}

	public Topic(DeviceGuid guid) {
		this.guid = guid;
		this.type = guid.getDeviceTypeId();
		this.number = guid.getDeviceNumber();
	}

	public Topic(String guid) {
		this(DeviceGuid.newGuid(guid));
	}

	abstract public int getTopicType();

	@Override
	public String toString() {
		return getTopic();
	}

	public DeviceGuid getGuid() {
		return guid;
	}

	static public class UnicastTopic extends Topic {

		private UnicastTopic(String guid) {
			super(guid);
		}

		@Override
		public String getTopic() {
			return String.format(TOPIC_FORMAT, TOPIC_UNICAST, type, number);
		}

		public String getTargetType() {
			return type;
		}

		public String getTargetId() {
			return number;
		}

		@Override
		public int getTopicType() {
			return TopicType_Unicast;
		}

	}

	static public class BroadcastTopic extends Topic {

		private BroadcastTopic(String guid) {
			super(guid);
		}

		@Override
		public String getTopic() {
			return String.format(TOPIC_FORMAT, TOPIC_BROADCAST, type, number);
		}

		public String getSorceType() {
			return type;
		}

		public String getSourceId() {
			return number;
		}

		@Override
		public int getTopicType() {
			return TopicType_Boardcast;
		}

	}

	static public Topic parse(String topicString) {
		Preconditions.checkNotNull(topicString);
		List<String> list = Splitter.on("/").omitEmptyStrings().trimResults()
				.splitToList(topicString);
		Preconditions.checkState(list.size() >= 3);

		String topicType = list.get(0);
		String guid = list.get(1) + list.get(2);
		if (Objects.equal(topicType, TOPIC_UNICAST)) {
			return newUnicastTopic(guid);
		} else if (Objects.equal(topicType, TOPIC_BROADCAST)) {
			return newBroadcastTopic(guid);
		}

		else {
			Preconditions.checkState(false, "inalid topic");
			return null;
		}

	}

	static public Topic newUnicastTopic(String guid) {
		return new UnicastTopic(guid);
	}

	static public Topic newBroadcastTopic(String guid) {
		return new BroadcastTopic(guid);
	}

	/**
	 * 获取目标设备需要注册的topic列表
	 * 
	 * @param guid
	 *            目标设备
	 * @return
	 */
	static public List<String> getWillRegistTopics(String guid) {
		List<String> list = Lists.newArrayList();
		list.add(Topic.newBroadcastTopic(guid).getTopic());
		return list;
	}

	static public List<String> getWillRegistTopics(DeviceInfo device) {

		List<String> list = Lists.newArrayList();
		list.addAll(getWillRegistTopics(device.guid));

		if (device.subDevices != null) {
			for (SubDeviceInfo subDevice : device.subDevices) {
				list.addAll(getWillRegistTopics(subDevice.guid));
			}
		}

		return list;
	}
}
