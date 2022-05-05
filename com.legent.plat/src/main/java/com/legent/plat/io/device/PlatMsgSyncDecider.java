package com.legent.plat.io.device;

import com.google.common.collect.BiMap;
import com.legent.io.msgs.IMsg;
import com.legent.io.senders.IMsgSyncDecider;
import com.legent.io.senders.AbsMsgSyncDecider;
import com.legent.plat.io.device.msg.MsgKeys;

/**
 * 同步消息判定器的基类，内置平台所需的同步判定逻辑
 *
 * @author sylar
 */
public class PlatMsgSyncDecider implements IMsgSyncDecider {

    IMsgSyncDecider internal = new InternalSyncDecider();
    IMsgSyncDecider extend;/******************   new RokiMsgSyncDecider()*/

    public PlatMsgSyncDecider(IMsgSyncDecider extend) {
        setExtend(extend);
    }

    public void setExtend(IMsgSyncDecider extend) {
        this.extend = extend;
    }

    @Override
    public int getPairsKey(IMsg msg) {
        //因为指令码分段规划，扩展指令与平台内置指令不会指令码冲突

        // 平台内置的消息对先匹配
        int res = internal.getPairsKey(msg);

        // 不是平台内置命令
        if (res == 0 && extend != null) {
            res = extend.getPairsKey(msg);
        }

        return res;
    }

    @Override
    public long getSyncTimeout() {
        return extend != null ? extend.getSyncTimeout() : internal.getSyncTimeout();
    }

    class InternalSyncDecider extends AbsMsgSyncDecider {
        @Override
        protected void initBiMap() {

            addPairsKey(MsgKeys.GetDevices_Req, MsgKeys.GetDevices_Rep);

            addPairsKey(MsgKeys.SetWifiParamsAndOwner_Req,
                    MsgKeys.SetWifiParamsAndOwner_Rep);
            addPairsKey(MsgKeys.GetWifiSignal_Req, MsgKeys.GetWifiSignal_Rep);

            // ------------废弃
            addPairsKey(MsgKeys.MakePair_Req, MsgKeys.MakePair_Rep);
            addPairsKey(MsgKeys.ExitPair_Req, MsgKeys.ExitPair_Rep);
            addPairsKey(MsgKeys.RemoveChildDevice_Req,
                    MsgKeys.RemoveChildDevice_Rep);
            // ------------废弃
        }


        @Override
        protected int getPairsKey(BiMap<Integer, Integer> map, int key) {
            return super.getPairsKey(map, key);
        }
    }
}
