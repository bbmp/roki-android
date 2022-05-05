package com.legent.plat.pojos.dictionary.msg;

import com.google.common.base.Preconditions;
import com.legent.plat.io.device.IAppMsgMarshaller;
import com.legent.plat.io.device.msg.Msg;

/**
 * Created by sylar on 15/7/29.
 */
public class AppMsgMarshaller implements IAppMsgMarshaller {

    MsgDic dic;

    public AppMsgMarshaller(MsgDic dic) {
        this.dic = dic;
    }

    @Override
    public byte[] marshal(Msg msg) throws Exception {
        MsgDesc ms = getMsgDesc(msg);
        return ms.marshal(msg);
    }

    @Override
    public void unmarshal(Msg msg, byte[] payload) throws Exception {
        MsgDesc ms = getMsgDesc(msg);
        ms.unmarshal(msg, payload);
    }

    private MsgDesc getMsgDesc(Msg msg) {
        String dtId = getDeviceType(msg);
        MsgDesc ms = dic.getMsgDesc(dtId, msg.getID().shortValue());
        Preconditions.checkNotNull(ms, String.format("找不到MsgDesc！【dtId:%s msgId:%s】", getDeviceType(msg), msg.getID()));
        return ms;
    }

    private String getDeviceType(Msg msg) {
        return msg.getDeviceGuid().getDeviceTypeId();
    }

}
