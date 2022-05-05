package com.legent.plat.io.device;

import com.legent.plat.io.device.msg.Msg;

/**
 * 设备通讯消息解析器
 *
 * @author sylar
 */
public interface IAppMsgMarshaller {
    /**
     * 将设备通讯消息模型序列化成字节流
     *
     * @throws Exception
     */
    byte[] marshal(Msg msg) throws Exception;

    /**
     * 将字节流反序列化成设备通讯消息模型
     *
     * @throws Exception
     */
    void unmarshal(Msg msg, byte[] payload) throws Exception;
}
