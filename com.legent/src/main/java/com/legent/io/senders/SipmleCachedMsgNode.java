package com.legent.io.senders;

import java.util.Calendar;


public class SipmleCachedMsgNode implements ICachedMsgNode {

	protected Object tag;
	protected String syncKey;
	protected long createTime;

	public SipmleCachedMsgNode(String syncKey) {
		this.syncKey = syncKey;
		this.createTime = Calendar.getInstance().getTimeInMillis();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Tag> Tag getTag() {
		return (Tag) tag;
	}

	@Override
	public <Tag> void setTag(Tag tag) {
		this.tag = tag;
	}

	@Override
	public String getSyncKey() {
		return syncKey;
	}

	@Override
	public boolean isTimeout(long ratedInterval) {
		boolean isTimeout = Calendar.getInstance().getTimeInMillis()
				- createTime > ratedInterval;

		return isTimeout;
	}

}
