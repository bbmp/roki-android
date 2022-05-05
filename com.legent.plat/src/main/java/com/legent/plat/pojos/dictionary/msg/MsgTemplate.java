package com.legent.plat.pojos.dictionary.msg;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.legent.pojos.AbsKeyPojo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sylar on 15/9/5.
 */
public class MsgTemplate extends AbsKeyPojo<String> implements IMsgContainer {
    @JsonProperty
    public String id;

    @JsonProperty
    public String name;

    @JsonProperty
    public List<MsgDesc> msgs;


    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }


    //===========================================================================================
    protected Map<Short, MsgDesc> mapMsgs = Maps.newHashMap();

    @Override
    public void init(Context cx, Object... ps) {
        super.init(cx, ps);

        Preconditions.checkState(!Strings.isNullOrEmpty(id), "MsgTemplate id 为空");
        Preconditions.checkNotNull(msgs, "MsgTemplate's msgs must be none-null");

        mapMsgs.clear();
        if (msgs != null) {
            Set<String> idsName = Sets.newHashSet();

            for (MsgDesc m : msgs) {
                Preconditions.checkState(!mapMsgs.containsKey(m.id), "msg id 冲突");
                mapMsgs.put(m.id, m);
                Preconditions.checkState(!idsName.contains(m.name), "msg name 冲突");
                idsName.add(m.name);

                m.container = this;
                m.init(cx);
            }

            idsName.clear();
        }

        for (MsgDesc m : mapMsgs.values()) {
            if (m.pairsId > 0) {
                Preconditions.checkState(mapMsgs.containsKey(m.pairsId), "pairsId 无对应 msg");
                Preconditions.checkState(findMsg(m.pairsId).pairsId == m.id, "pairsId 须相互对应:" + m.id);
            }
        }
    }

    @Override
    public MsgDesc findMsg(short msgId) {
        return mapMsgs.get(msgId);
    }

    //===========================================================================================

}
