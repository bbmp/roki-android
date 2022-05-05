package com.legent.io;

import com.legent.io.msgs.IMsg;

/**
 * IO观察者接口
 *
 * @author sylar
 */
public interface IOWatcher {

    void onConnectionChanged(boolean isConnected);

    void onMsgReceived(IMsg msg);
}