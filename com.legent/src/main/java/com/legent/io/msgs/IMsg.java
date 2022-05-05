package com.legent.io.msgs;

import com.legent.IKey;
import com.legent.ITag;

/**
 * IO通讯消息接口
 *
 * @author sylar
 */
public interface IMsg extends IKey<Integer>, ITag {

    byte[] getBytes();
}