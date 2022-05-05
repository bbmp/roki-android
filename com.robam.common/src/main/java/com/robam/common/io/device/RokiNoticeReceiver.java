package com.robam.common.io.device;

import android.util.Log;

import com.legent.LogTags;
import com.legent.plat.io.device.IAppNoticeReceiver;
import com.legent.plat.io.device.msg.PushMsg;

public class RokiNoticeReceiver implements IAppNoticeReceiver {

    @Override
    public void onReceivedPushMsg(PushMsg pushMsg) {

        try {
            Log.i(LogTags.TAG_IO, pushMsg.toString());

        } catch (Exception e) {
            Log.e(LogTags.TAG_IO, "onReceivedPushMsg error:" + e.getMessage());
            e.printStackTrace();
        }
    }

}
