package com.legent.plat.pojos.dictionary.msg;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.legent.plat.io.device.AbsPlatProtocol;
import com.legent.plat.io.device.IAppMsgMarshaller;
import com.legent.plat.io.device.msg.Msg;
import com.legent.pojos.AbsKeyPojo;
import com.legent.utils.api.ResourcesUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;

/**
 * Created by sylar on 15/9/5.
 */
public class MsgDesc extends AbsKeyPojo<Short> implements IParamContainer, IAppMsgMarshaller {

    static public final int BufferSize = AbsPlatProtocol.BufferSize;
    static public final ByteOrder BYTE_ORDER = AbsPlatProtocol.BYTE_ORDER;


    @JsonProperty
    public short id;

    @JsonProperty
    public String name;

    @JsonProperty
    public short pairsId;

    @JsonProperty
    public String desc;

//    @JsonProperty
//    public String implMsgMarshaller;

    @JsonProperty
    public List<ParamDesc> params;


    @Override
    public Short getID() {
        return id;
    }

    @Override
    public String getName() {
        return ResourcesUtils.getStringOrFromRes(name);
    }

//    public IAppMsgMarshaller getMsgMarshaller() {
//        return ReflectUtils.getReflectObj(implMsgMarshaller);
//    }

    //===========================================================================================

    protected Map<String, ParamDesc> mapParams = Maps.newHashMap();
    protected IMsgContainer container;

    @Override
    public void init(Context cx, Object... ps) {
        super.init(cx, ps);

        Preconditions.checkState(id > 0, "msg id 无效");
        Preconditions.checkState(!Strings.isNullOrEmpty(name), "msg name 为空");
        if (pairsId > 0) {
            Preconditions.checkState(pairsId != id, "msg pairsId不能为自身id");
        }

        //
        mapParams.clear();
        if (this.params != null) {
            for (ParamDesc p : this.params) {
                Preconditions.checkState(!mapParams.containsKey(p.id), "param id 冲突");
                mapParams.put(p.id, p);
                p.container = this;

                p.init(cx);
            }
        }
    }

    @Override
    public ParamDesc findParam(String paramId) {
        return mapParams.get(paramId);
    }


    //===========================================================================================

    @Override
    public byte[] marshal(Msg msg) throws Exception {
        ByteBuffer buf = ByteBuffer.allocate(BufferSize).order(BYTE_ORDER);

        if (params != null) {
            for (ParamDesc p : params) {
                p.encode(msg, buf);
            }
        }

        //------------------------buffer to byte[]---------------------
        byte[] data = new byte[buf.position()];
        System.arraycopy(buf.array(), 0, data, 0, data.length);
        buf.clear();
        return data;
    }

    @Override
    public void unmarshal(Msg msg, byte[] payload) throws Exception {

        if (params != null) {
            int offset = 0;
            for (ParamDesc p : params) {
                offset = p.decode(msg, payload, offset);
            }
        }
    }


    //===========================================================================================

}
