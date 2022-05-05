package com.legent.io.senders;

import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.legent.LogTags;
import com.legent.io.msgs.IMsg;

abstract public class AbsMsgSyncDecider implements IMsgSyncDecider {

    protected final static String TAG = LogTags.TAG_IO;
    private BiMap<Integer, Integer> map = HashBiMap.create();

    abstract protected void initBiMap();

    @Override
    public int getPairsKey(IMsg msg) {
        int key = msg.getID();
        int res = getPairsKey(map, key);
        return res;
    }

    @Override
    public long getSyncTimeout() {
        return 2000;
    }

    protected void addPairsKey(int key1, int key2) {
        addPairsKey(map, key1, key2);
    }

    protected void addPairsKey(BiMap<Integer, Integer> map, int key1, int key2) {
        Preconditions.checkNotNull(map);
        Preconditions.checkState(key1 != 0 && key2 != 0);
        map.put(key1, key2);
    }

    protected int getPairsKey(BiMap<Integer, Integer> map, int key) {
        if (map.size() == 0) {
            initBiMap();
        }
        Preconditions.checkNotNull(map);
        Preconditions.checkState(key != 0);
        int res = 0;
        try {
            if (map.containsKey(key)) {
                res = map.get(key);
            } else {
                BiMap<Integer, Integer> map2 = map.inverse();
                if (map2.containsKey(key)) {
                    res = map2.get(key);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getPairsKey error:" + e.getMessage());
        }

        return res;
    }

}
