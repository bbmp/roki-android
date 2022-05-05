package com.robam.roki.ui.view.umpush;

/**
 * Created by  on 2017/4/12.
 * 缓存友盟传来的数据
 */
public class UMPushMsg {
    private static int msgKey;
    private static int  msgId;


    public static int getMsgType() {
        return msgKey;
    }

    public static void setMsgType(int msgKey) {
        UMPushMsg.msgKey = msgKey;
    }

    public static int getMsgId() {
        return msgId;
    }

    public static void setMsgId(int msgId) {
        UMPushMsg.msgId = msgId;
    }
}
