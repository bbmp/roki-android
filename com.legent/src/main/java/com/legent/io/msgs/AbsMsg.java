package com.legent.io.msgs;


import org.json.JSONObject;

abstract public class AbsMsg extends JSONObject implements IMsg {

    protected int msgKey;
    protected Object tag;
    protected byte[] data;

    @Override
    public Integer getID() {
        return msgKey;
    }

    @Override
    public byte[] getBytes() {
        return data;
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

    public void setKey(int msgKey) {
        this.msgKey = msgKey;
    }

    @Override
    public String toString() {
        String content = super.toString();
        return String.format("msgId:%s content:%s", msgKey, content);
    }

}
