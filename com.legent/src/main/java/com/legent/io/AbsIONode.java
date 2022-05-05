package com.legent.io;

import android.util.Log;

import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.io.msgs.IMsg;
import com.legent.services.AbsService;
import com.legent.utils.LogUtils;

abstract public class AbsIONode extends AbsService implements IONode {

    protected boolean isConnected;
    protected IOWatcher watcher;

    public AbsIONode() {

    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    // -------------------------------------------------------------------------------
    // INode
    // -------------------------------------------------------------------------------

    @Override
    public void dispose() {
        super.dispose();
        close(null);
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void open(VoidCallback callback) {
        try {
            if (isConnected) {
                onCallSuccess(callback);
            } else {
                onOpen(callback);
            }
        } catch (Exception e) {
            if (e != null && e.getMessage() != null)
                Log.i("mqttbuserror:", "error1:" + e.getMessage());
        }
    }

    @Override
    public void close(VoidCallback callback) {
        try {
            if (!isConnected) {
                LogUtils.i("AbsioNode","onCallSuccess:"+!isConnected);
                onCallSuccess(callback);
            } else {
                LogUtils.i("AbsioNode","onClose:"+isConnected);
                onClose(callback);
            }
        } catch (Exception e) {
            if (e != null && e.getMessage() != null)
                Log.i("mqttbuserror:", "error0:" + e.getMessage());
        }
    }

    @Override
    public void setWatcher(IOWatcher watcher) {
        this.watcher = watcher;
    }

    // -------------------------------------------------------------------------------
    // protecetd
    // -------------------------------------------------------------------------------

    abstract protected void onOpen(VoidCallback callback);

    abstract protected void onClose(VoidCallback callback);

    protected void onConnectionChanged(boolean isConnected) {
        this.isConnected = isConnected;
        LogUtils.i("mqtt","isConnected:"+isConnected + " watcher:" + watcher);
        if (watcher != null) {
            watcher.onConnectionChanged(isConnected);
        }
    }

    protected void onMsgReceived(IMsg msg) {
        if (watcher != null) {
            watcher.onMsgReceived(msg);
        }
    }

    protected void onCallSuccess(VoidCallback callback) {
        Helper.onSuccess(callback);
    }

    protected void onCallFailure(VoidCallback callback, Throwable t) {
        Helper.onFailure(callback, t);
    }

}
