package com.legent.plat.pojos.dictionary.msg;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.legent.pojos.AbsPojo;

import java.util.List;
import java.util.Map;

/**
 * Created by sylar on 15/9/5.
 */
public class MsgDic extends AbsPojo {

    @JsonProperty(defaultValue = "2000")
    public long syncTimeout;

    @JsonProperty
    public List<MsgIndex> indexs;


    @JsonProperty
    public List<MsgTemplate> templates;


    //===========================================================================================
    protected Map<String, MsgIndex> mapIndex = Maps.newHashMap();
    protected Map<String, MsgTemplate> mapTemplate = Maps.newHashMap();

    @Override
    public void init(Context cx, Object... ps) {
        super.init(cx, ps);

        Preconditions.checkNotNull(indexs, "MsgDic's indexs must be none-null");
        Preconditions.checkNotNull(templates, "MsgDic's templates must be none-null");

        for (MsgTemplate foo : templates) {
            Preconditions.checkState(!mapTemplate.containsKey(foo.id), "MsgTemplate id 冲突");
            mapTemplate.put(foo.id, foo);

            foo.init(cx);
        }

        for (MsgIndex foo : indexs) {
            Preconditions.checkState(!mapIndex.containsKey(foo.deviceTypeId), "MsgIndex id 冲突");
            mapIndex.put(foo.deviceTypeId, foo);

            Preconditions.checkState(mapTemplate.containsKey(foo.templateId), "templateId 无效");
        }
    }

    public MsgDesc getMsgDesc(String deviceTypeId, short msgCode) {
        MsgTemplate mt = getMsgTemplateByDeviceType(deviceTypeId);
        return mt.findMsg(msgCode);
    }

    public MsgTemplate getMsgTemplateByDeviceType(String deviceTypeId) {
        return getMsgTemplate(getMsgIndex(deviceTypeId).templateId);
    }


    public MsgIndex getMsgIndex(String indexId) {
        return mapIndex.get(indexId);
    }


    public MsgTemplate getMsgTemplate(String templateId) {
        return mapTemplate.get(templateId);
    }

    //===========================================================================================

}
