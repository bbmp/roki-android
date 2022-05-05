package com.legent.plat.io.device.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.IJsonPojo;
import com.legent.utils.JsonUtils;

import java.util.Map;

public class PushMsg implements IJsonPojo {

	@JsonProperty("notification")
	public Notification notification;

	@JsonProperty("message")
	public Message message;

	String json;

	@Override
	public String toString() {
		return json;
	}

public class Notification implements IJsonPojo {

	@JsonProperty("ticket")
	public String ticket;

	@JsonProperty("title")
	public String title;

	@JsonProperty("alert")
	public String content;

	@JsonProperty("alert_format")
	public Object contentFormat;

	@JsonProperty("sound")
	public String sound;

	@JsonProperty("params")
	public Map<String, String> params;


}

public class Message implements IJsonPojo {

	@JsonProperty("code")
	public String code;

	@JsonProperty("params")
	public Map<String, String> params;


}

	static public PushMsg newPushMsg(String json) {

		try {
			PushMsg msg = JsonUtils.json2Pojo(json, PushMsg.class);
			msg.json = json;

			return msg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
