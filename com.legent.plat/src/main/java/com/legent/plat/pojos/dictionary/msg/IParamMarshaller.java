package com.legent.plat.pojos.dictionary.msg;

import org.json.JSONObject;

import java.nio.ByteBuffer;

/**
 * Created by sylar on 15/9/5.
 */
public interface IParamMarshaller {
    void encode(JSONObject jsonObject, ByteBuffer buf) throws Exception;

    int decode(JSONObject jsonObject, byte[] payload, int offset) throws Exception;
}
