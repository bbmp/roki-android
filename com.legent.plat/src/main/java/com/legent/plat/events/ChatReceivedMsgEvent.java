package com.legent.plat.events;

import com.legent.plat.pojos.ChatMsg;

import java.util.List;

public class ChatReceivedMsgEvent {

    public List<ChatMsg> msgList;

    public ChatReceivedMsgEvent(List<ChatMsg> msgList) {
        this.msgList = msgList;
    }

}
