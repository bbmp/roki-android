package com.legent.io.senders;

import com.legent.ITag;

/**
 * 缓存已经发送的消息，等待配对的接收消息，直至超时
 *
 * @author sylar
 */
public interface ICachedMsgNode extends ITag {

    String getSyncKey();

    boolean isTimeout(long ratedInterval);
}
