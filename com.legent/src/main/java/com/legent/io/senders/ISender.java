package com.legent.io.senders;


import com.legent.IDispose;
import com.legent.io.msgs.IMsg;
import com.legent.io.msgs.IMsgCallback;

/**
 * IO指令发送器
 *
 * @author sylar
 */
public interface ISender<Msg extends IMsg> extends IDispose {

    void setMsgSyncDecider(IMsgSyncDecider syncDecider);

    void asyncSend(String deviceId, Msg msg, IMsgCallback<Msg> callback);

    boolean match(String deviceId, Msg resMsg);
}

