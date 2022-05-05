package com.legent.plat.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.pojos.AbsKeyPojo;

import java.util.Calendar;
import java.util.Date;

public class ChatMsg extends AbsKeyPojo<Long> implements Comparable<ChatMsg> {

    public static final int MSG_TYPE_USER2CS = 0;
    public static final int MSG_TYPE_CS2USER = 1;
    public static final int MSG_TYPE_USER2USER = 2;

    public static ChatMsg newSendMsg(String content) {
        ChatMsg msg = new ChatMsg();
        msg.fromUserId = Plat.accountService.getCurrentUserId();
        msg.postTime = Calendar.getInstance().getTimeInMillis();
        msg.msgType = MSG_TYPE_USER2CS;
        msg.msg = content;
        return msg;
    }

    @JsonProperty("id")
    public long id;

    @JsonProperty("fromUserId")
    public long fromUserId;

    @JsonProperty("toUserId")
    public long toUserId;

    @JsonProperty("postTime")
    public long postTime;

    @JsonProperty("msgType")
    public int msgType;

    @JsonProperty("msg")
    public String msg;

    public ChatMsg() {
        // must be build for json
    }

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return msg;
    }

    public void getFromUser(final Callback<User> callback) {
        Plat.accountService.getUser(fromUserId, callback);
    }

    public void getToUser(final Callback<User> callback) {
        Plat.accountService.getUser(toUserId, callback);
    }

    public long getUserId() {
        return fromUserId;
    }

    public long getPostTime() {
        return postTime;
    }

    public Date getDate() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(postTime);
        return c.getTime();
    }

    public boolean isIncoming() {
        return msgType != MSG_TYPE_USER2CS;
    }

    @Override
    public boolean equals(Object o) {
        boolean res = false;

        if (o instanceof ChatMsg) {
            ChatMsg msg = (ChatMsg) o;
            res = msg.fromUserId == fromUserId && msg.msgType == msgType
                    && Math.abs(msg.postTime - postTime) < 1000;
        }
        return res;
    }

    @Override
    public String toString() {
        return String.format("time:%s  msg:%s", getDate().toString(), msg);
    }

    @Override
    public int compareTo(ChatMsg another) {
        long res = postTime - another.postTime;
        return (int) res;
    }

}
