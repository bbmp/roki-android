package com.legent.io;

import com.legent.IDispose;
import com.legent.Initialization;
import com.legent.VoidCallback;
import com.legent.io.msgs.IMsg;

/**
 * IO节点模型接口
 *
 * @author sylar
 */
public interface IONode extends Initialization, IDispose {

    boolean isConnected();

    void open(VoidCallback callback);

    void close(VoidCallback callback);

    void send(IMsg msg, VoidCallback callback);

    void setWatcher(IOWatcher watcher);
}