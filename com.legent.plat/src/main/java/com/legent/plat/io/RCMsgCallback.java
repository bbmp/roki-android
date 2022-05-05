package com.legent.plat.io;

import com.legent.Callback;
import com.legent.Helper;
import com.legent.plat.exceptions.RCException;
import com.legent.plat.io.device.msg.MsgParams;
import com.legent.plat.exceptions.ExceptionHelper;
import com.legent.plat.io.device.msg.MsgCallback;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.services.ResultCodeManager;

public class RCMsgCallback implements MsgCallback {

    Callback<?> callback;

    public RCMsgCallback(Callback<?> callback) {
        this.callback = callback;
    }

    @Override
    public void onSuccess(Msg resMsg) {
        try {
            boolean isRcMsg = !resMsg.isNull(MsgParams.RC);
            if (isRcMsg) {
                int rc = resMsg.getInt(MsgParams.RC);
                boolean isSuccess = ResultCodeManager.isSuccessRC(rc);

                if (!isSuccess) {
                    RCException e = (RCException) ExceptionHelper.newRCException(rc);
                    e.setTag(resMsg);
                    Helper.onFailure(callback, e);
                } else {
                    afterSuccess(resMsg);
                }
            } else {
                afterSuccess(resMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Helper.onFailure(callback, ExceptionHelper.newDeviceIOException(t.getMessage()));
    }

    /**
     * RC码正确时的处理
     *
     * @param result
     */
    protected void afterSuccess(Msg resMsg) {

    }
}
