package com.legent.plat.io.device;

import com.legent.plat.io.device.msg.PushMsg;

/**
 * 通告处理器。包括收到推送消息、设备上线、设备激活、设备变更能通知
 *
 * @author sylar
 */
public interface IAppNoticeReceiver {

    /**
     * 收到推送消息
     *
     */
    void onReceivedPushMsg(PushMsg pushMsg);

}