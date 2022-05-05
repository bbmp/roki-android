package com.legent.io.senders;

import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.legent.Helper;
import com.legent.LogTags;
import com.legent.io.exceptions.SyncTimeoutException;
import com.legent.io.msgs.IMsg;
import com.legent.io.msgs.IMsgCallback;
import com.legent.services.AbsService;
import com.legent.utils.LogUtils;

import java.util.List;
import java.util.Map;

abstract public class AbsSender<Msg extends IMsg> extends AbsService implements
        ISender<Msg> {

    protected boolean isLog_sync = true;
    protected boolean isLog_async = true;

    protected static final String TAG = LogTags.TAG_IO;

    protected IMsgSyncDecider syncDecider;
    protected Map<String, ICachedMsgNode> map = Maps.newLinkedHashMap();

    // -------------------------------------------------------------------------------
    // abstract
    // -------------------------------------------------------------------------------

    abstract protected void sendMsg(String deviceId, Msg msg);

    // -------------------------------------------------------------------------------
    // public
    // -------------------------------------------------------------------------------

    @Override
    public void dispose() {
        clear();
    }

    @Override
    public void setMsgSyncDecider(IMsgSyncDecider syncDecider) {
        this.syncDecider = syncDecider;
    }

    @Override
    public void asyncSend(String deviceId, Msg msg, IMsgCallback<Msg> callback) {

        if (callback == null) {
            if (isLog_async) {
                Log.d(TAG, String.format("send msg:%s\n%s", deviceId, msg));
            }
            LogUtils.i("20170527", "if_asyncSend_msg:" + msg);
            sendMsg(deviceId, msg);
        } else {
            ICachedMsgNode node = newCachedNode(deviceId, msg, callback);
            Preconditions.checkNotNull(node, "not syncMsg code:" + msg.getID());
            map.put(node.getSyncKey(), node);
            if (isLog_sync) {
                Log.i(TAG, String.format("sync send:%s\n%s", deviceId, msg));
            }
            LogUtils.i("20170527", "else_asyncSend_msg:" + msg);
            sendMsg(deviceId, msg);
        }
    }

    @Override
    public boolean match(String deviceId, Msg resMsg) {

        String syncKey = getSyncKey(deviceId, resMsg);
        if (syncKey == null)
            return false;

        if (!map.containsKey(syncKey))
            return false;

        ICachedMsgNode node = null;
        IMsgCallback<Msg> callback = null;
        synchronized (this.map) {
            node = map.remove(syncKey);
            callback = node.getTag();
        }

        if (isLog_sync) {
            Log.i(TAG, String.format("sync recv:%s\n%s", deviceId, resMsg));
        }
        Helper.onSuccess(callback, resMsg);

        return true;
    }

    public void setSyncLogEnable(boolean enable) {
        isLog_sync = enable;
    }

    public void setAsyncLogEnable(boolean enable) {
        isLog_async = enable;
    }

    // -------------------------------------------------------------------------------
    // protected
    // -------------------------------------------------------------------------------

    protected void clear() {
        map.clear();
    }

    protected ICachedMsgNode newCachedNode(String deviceId, Msg reqMsg,
                                           IMsgCallback<Msg> callback) {

        String syncKey = getSyncKey(deviceId, reqMsg);
        if (syncKey == null)
            return null;

        SipmleCachedMsgNode node = new SipmleCachedMsgNode(syncKey);
        node.setTag(callback);
        return node;
    }

    protected String getSyncKey(String deviceId, Msg msg) {
        if (syncDecider == null) return null;
        LogUtils.i("20170527", "msg::" + msg.getID());
        int k1 = msg.getID();
        int k2 = syncDecider.getPairsKey(msg);

        if (k2 == 0)
            return null;

        String syncKey = String.format("%s|%s", deviceId,
                String.format("<%s,%s>", Math.min(k1, k2), Math.max(k1, k2)));
        return syncKey;
    }

    synchronized protected void checkTimeout() {

        if (map.size() == 0 || syncDecider == null)
            return;

        synchronized (this.map) {
            List<ICachedMsgNode> list = Lists.newArrayList(map.values());
            long interval = syncDecider.getSyncTimeout();
            for (ICachedMsgNode node : list) {
                if (node.isTimeout(interval)) {
                    map.remove(node.getSyncKey());
                    onTimeout(node);
                }
            }
        }

    }

    protected void onTimeout(ICachedMsgNode node) {

        if (isLog_sync) {
            Log.e(TAG, String.format("sync timeout: %s", node.getSyncKey()));
        }

        IMsgCallback<Msg> callback = node.getTag();
        Helper.onFailure(callback, new SyncTimeoutException());
    }

}
