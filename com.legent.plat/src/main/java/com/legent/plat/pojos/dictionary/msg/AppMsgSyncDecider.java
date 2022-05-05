package com.legent.plat.pojos.dictionary.msg;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.legent.io.msgs.IMsg;
import com.legent.io.senders.AbsMsgSyncDecider;
import com.legent.plat.io.device.IAppMsgSyncDecider;
import com.legent.plat.io.device.msg.Msg;
import com.legent.utils.LogUtils;

import java.util.Map;
import java.util.Set;

/**
 * Created by sylar on 15/7/28.
 */
public class AppMsgSyncDecider implements IAppMsgSyncDecider {

    MsgDic dic;
    Map<String, Decider> map = Maps.newHashMap();

    public AppMsgSyncDecider(MsgDic dic) {
        this.dic = dic;

        MsgTemplate mt;
        Decider decider;
        for (MsgIndex foo : dic.indexs) {
            mt = dic.getMsgTemplate(foo.templateId);
            decider = new Decider(mt);
            map.put(foo.deviceTypeId, decider);
        }

    }

    @Override
    public int getPairsKey(IMsg msg) {
        int res = 0;
        Msg m = (Msg) msg;
        String dtId = m.getDeviceGuid().getDeviceTypeId();
        if (map.containsKey(dtId)) {
            res = map.get(dtId).getPairsKey(msg);
        }

        return res;
    }

    @Override
    public long getSyncTimeout() {
        return dic.syncTimeout;
    }

     class Decider extends AbsMsgSyncDecider {

        MsgTemplate template;

        public Decider(MsgTemplate template) {
            this.template = template;
        }

        @Override
        protected void initBiMap() {
            Set<Short> ids = Sets.newHashSet();
            for (MsgDesc ms : template.msgs) {
                if (ms.pairsId == 0)
                    continue;

                if (!ids.contains(ms.id) && !ids.contains(ms.pairsId)) {
                    addPairsKey(ms.id, ms.pairsId);
                    ids.add(ms.id);
                    ids.add(ms.pairsId);
                }
            }
        }
    }

}
