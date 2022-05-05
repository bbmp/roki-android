package com.legent.io.protocols;

import com.legent.IDispose;
import com.legent.Initialization;
import com.legent.io.msgs.IMsg;

import java.util.List;

/**
 * 协议解析接口
 *
 * @author sylar
 */
public interface IProtocol extends Initialization, IDispose {

    byte[] encode(IMsg msg) throws Exception;

    List<IMsg> decode(byte[] data, Object... params) throws Exception;
}