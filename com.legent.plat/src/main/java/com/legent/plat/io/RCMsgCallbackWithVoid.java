package com.legent.plat.io;

import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.exceptions.ExceptionHelper;
import com.legent.plat.exceptions.RCException;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgCallback;
import com.legent.plat.io.device.msg.MsgParams;
import com.legent.plat.services.ResultCodeManager;
import com.legent.utils.LogUtils;

public class RCMsgCallbackWithVoid implements MsgCallback {

    VoidCallback callback;

    public RCMsgCallbackWithVoid(VoidCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onSuccess(Msg resMsg) {

        try {

            LogUtils.i("20190820555","onSuccess");
            boolean isRcMsg = !resMsg.isNull(MsgParams.RC);
            if (isRcMsg) {
                int rc = resMsg.getInt(MsgParams.RC);
                boolean isSuccess = ResultCodeManager.isSuccessRC(rc);

                if (!isSuccess) {

                    RCException e = (RCException) ExceptionHelper.newRCException(rc);
                    e.setTag(resMsg);
                    Helper.onFailure(callback, e);
                    LogUtils.i("20190820555","fail::::"+e.errorMsg);
                } else {
                    LogUtils.i("20190820555","success:::"+resMsg.toString());
                    afterSuccess(resMsg);
                    Helper.onSuccess(callback);
                }
            } else {
                afterSuccess(resMsg);
                Helper.onSuccess(callback);

                LogUtils.i("20190820555","success:::"+resMsg.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("20190820555",e.getMessage());
        }


    }

    @Override
    public void onFailure(Throwable t) {
        LogUtils.i("20190820555",t.getMessage());
        Helper.onFailure(callback, ExceptionHelper.newDeviceIOException(t.getMessage()));
    }

    protected void afterSuccess(Msg resMsg) {
    }
}
