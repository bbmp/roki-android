package com.legent.plat.pojos.dictionary.msg;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Enums;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.pojos.AbsKeyPojo;
import com.legent.utils.api.ResourcesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * Created by sylar on 15/9/5.
 */
public class ParamDesc extends AbsKeyPojo<String> implements IParamContainer, IParamMarshaller {

    @JsonProperty
    public String id;

    @JsonProperty
    public String name;

    @JsonProperty
    public String desc;

    @JsonProperty
    public String type;

    @JsonProperty
    public int len;

    @JsonProperty
    public String refParamId;

    @JsonProperty
    public List<ParamDesc> params;

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return ResourcesUtils.getStringOrFromRes(name);
    }


    //===========================================================================================

    protected Map<String, ParamDesc> mapParams = Maps.newHashMap();
    protected IParamContainer container;
    protected ePT pt;
    protected ParamDesc refParam;

    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);

        Preconditions.checkState(!Strings.isNullOrEmpty(id), "param id 为空");
        Preconditions.checkState(!Strings.isNullOrEmpty(name), "param name 为空");

        checkTypeAndLength();

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
    public void encode(JSONObject jsonObject, ByteBuffer buf) throws Exception {
        byte[] bytes;
        int dataLen;

        switch (pt) {
            case BYTE:
                buf.put((byte) jsonObject.optInt(id));
                break;
            case BOOL:
                buf.put(jsonObject.optBoolean(id) ? (byte) 1 : (byte) 0);
                break;
            case SHORT:
                if (this.len == 1) {
                    buf.put((byte) jsonObject.optInt(id));
                } else {
                    buf.putShort((short) jsonObject.optInt(id));
                }
                break;
            case INT:
                buf.putInt(jsonObject.optInt(id));
                break;
            case FLOAT:
                buf.putFloat((float) jsonObject.optDouble(id));
                break;
            case LONG:
                buf.putLong(jsonObject.optLong(id));
                break;
            case DOUBLE:
                buf.putDouble(jsonObject.optDouble(id));
                break;
            case BYTES:
                dataLen = getLengthOrRef(jsonObject);
                bytes = (byte[]) jsonObject.opt(id);
                checkBytesLength(bytes, dataLen);
                buf.put(bytes);
                break;
            case STRING:
                dataLen = getLengthOrRef(jsonObject);
                bytes = jsonObject.optString(id).getBytes();
                checkBytesLength(bytes, dataLen);
                buf.put(bytes);
                break;
            case LIST:
                dataLen = getLengthOrRef(jsonObject);
                boolean isFixedLength = dataLen > 0;
                JSONArray ja = jsonObject.optJSONArray(id);
                if (isFixedLength) {
                    Preconditions.checkState(ja != null && dataLen == ja.length(), "param 实际长度与配置长度不符");
                } else {
                    dataLen = ja != null ? ja.length() : 0;
                }

                for (int i = 0; i < dataLen; i++) {
                    JSONObject jo = ja.optJSONObject(i);
                    for (ParamDesc param : params) {
                        param.encode(jo, buf);
                    }
                }

                break;
            default:
                break;
        }
    }

    @Override
    public int decode(JSONObject jsonObject, byte[] payload, int offset) throws Exception {
        byte[] bytes;
        int dataLen;

        switch (pt) {
            case BYTE:
                checkBytesOffset(payload, offset, 1);
                jsonObject.put(id, payload[offset++]);
                break;
            case BOOL:
                checkBytesOffset(payload, offset, 1);
                jsonObject.put(id, payload[offset++] == (byte) 1);
                break;
            case SHORT:
                if (this.len == 1) {
                    checkBytesOffset(payload, offset, 1);
                    jsonObject.put(id, MsgUtils.getShort(payload[offset++]));
                } else {
                    checkBytesOffset(payload, offset, 2);
                    jsonObject.put(id, MsgUtils.getShort(payload, offset));
                    offset += 2;
                }
                break;
            case INT:
                checkBytesOffset(payload, offset, 4);
                jsonObject.put(id, MsgUtils.getInt(payload, offset));
                offset += 4;
                break;
            case FLOAT:
                checkBytesOffset(payload, offset, 4);
                jsonObject.put(id, MsgUtils.getFloat(payload, offset));
                offset += 4;
                break;
            case LONG:
                checkBytesOffset(payload, offset, 8);
                jsonObject.put(id, MsgUtils.getLong(payload, offset));
                offset += 8;
                break;
            case DOUBLE:
                checkBytesOffset(payload, offset, 8);
                jsonObject.put(id, MsgUtils.getDouble(payload, offset));
                offset += 8;
                break;
            case BYTES:
                dataLen = getLengthOrRef(jsonObject);
                checkBytesOffset(payload, offset, dataLen);
                bytes = new byte[dataLen];
                System.arraycopy(payload, offset, bytes, 0, dataLen);
                jsonObject.put(id, bytes);
                offset += dataLen;
                break;
            case STRING:
                dataLen = getLengthOrRef(jsonObject);
                checkBytesOffset(payload, offset, dataLen);
                bytes = new byte[dataLen];
                System.arraycopy(payload, offset, bytes, 0, dataLen);
                jsonObject.put(id, new String(bytes));
                offset += dataLen;
                break;
            case LIST:
                JSONArray ja = new JSONArray();
                jsonObject.put(id, ja);

                dataLen = getLengthOrRef(jsonObject);

                for (int i = 0; i < dataLen; i++) {
                    JSONObject jo = new JSONObject();
                    for (ParamDesc p : params) {
                        offset = p.decode(jo, payload, offset);
                    }
                    ja.put(jo);
                }

                break;
            default:
                break;
        }
        return offset;
    }

    //==========================================================================

    void checkTypeAndLength() {

        Preconditions.checkState(!Strings.isNullOrEmpty(type), "param type 为空");
        if (Objects.equal(type.toLowerCase(), "boolean")) {
            type = "bool";
        }
        if (Objects.equal(type.toLowerCase(), "integer")) {
            type = "int";
        }
        if (Objects.equal(type.toLowerCase(), "byte[]")) {
            type = "bytes";
        }
        if (Objects.equal(type.toLowerCase(), "strings")) {
            type = "string";
        }
        if (Objects.equal(type.toLowerCase(), "array")) {
            type = "list";
        }

        boolean isValid = Enums.getIfPresent(ePT.class, type.toUpperCase()).isPresent();
        Preconditions.checkState(isValid, "param type 无效");
        pt = ePT.valueOf(type.toUpperCase());

        switch (pt) {
            case BYTE:
                len = 1;
                break;
            case BOOL:
                len = 1;
                break;
            case SHORT:
                Preconditions.checkState(this.len == 1 || this.len == 2, String.format("数据长度不符!【type:%s length:%s】", pt.toString(), this.len));
                break;
            case INT:
                len = 4;
                break;
            case FLOAT:
                len = 4;
                break;
            case LONG:
                len = 8;
                break;
            case DOUBLE:
                len = 8;
                break;
            case BYTES:
                break;
            case STRING:
                break;
            case LIST:
                Preconditions.checkNotNull(params, String.format("params 未配置！【paramId:%s paramDesc:%s】", id, name));
//                Preconditions.checkNotNull(len > 0 || !Strings.isNullOrEmpty(refParamId), String.format("params 未配置数据长度！【paramId:%s paramDesc:%s】", id, name));
                if (!Strings.isNullOrEmpty(refParamId)) {
                    refParam = container.findParam(refParamId);
                    Preconditions.checkNotNull(refParamId, "param refParamId 无效");
                }

                break;
            default:
                break;
        }
    }

    void checkBytesLength(byte[] bytes, int len) {
        Preconditions.checkState(len == bytes.length, String.format("param实际长度不等于配置长度！【paramId:%s paramDesc:%s】", id, name));
    }

    void checkBytesOffset(byte[] bytes, int startIndex, int len) {
        Preconditions.checkState(startIndex < bytes.length, String.format("bytes offset 越界！【paramId:%s paramDesc:%s】", id, name));
        Preconditions.checkState(startIndex + len <= bytes.length, String.format("param 取值超出 bytes 范围！【paramId:%s paramDesc:%s】", id, name));
    }

    int getLengthOrRef(JSONObject jsonObject) throws Exception {
        if (len > 0) {
            return len;
        } else if (refParam != null) {
            return jsonObject.getInt(refParam.id);
        } else {
            return 0;
        }
    }


    //===========================================================================================

}
