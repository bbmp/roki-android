package com.legent.io.senders;

import com.legent.io.msgs.IMsg;

/**
 * 同步消息判定器
 *
 * @author sylar
 */
public interface IMsgSyncDecider {
    /**
     * 获取与指定消息相配对的消息key
     *
     * @param msg 指定消息
     * @return 有配对的消息码则返回消息标识码，否则返回零
     */
    int getPairsKey(IMsg msg);

    /**
     * 获取同步超时时间
     *
     * @return 同步超时，单位毫秒
     */
    long getSyncTimeout();
}