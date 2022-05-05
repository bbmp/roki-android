package com.legent.plat.io.device;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.io.msgs.IMsgCallback;
import com.legent.plat.io.RCMsgCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.finder.FinderFactory;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgKeys;
import com.legent.plat.io.device.msg.MsgParams;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.services.AbsService;


/**
 * Created by sylar on 15/7/24.
 */
abstract public class AbsCommander extends AbsService implements ICoreCommand {

    abstract public void asyncSend(String deviceId, Msg msg, IMsgCallback<Msg> callback);

 // ==========================================================ICoreCommand==========================================================

    @Override
    public IDeviceFinder getDeviceFinder() {
        return FinderFactory.getFinder();
    }



    @Override
    public void getDevice(String deviceId, final Callback<DeviceInfo> callback) {
        Msg req = Msg.newRequestMsg(MsgKeys.GetDevices_Req, deviceId);
        asyncSend(deviceId, req, new RCMsgCallback(callback) {
            @Override
            protected void afterSuccess(Msg result) {
                DeviceInfo device = (DeviceInfo) result.opt(MsgParams.DeviceInfo);
                Helper.onSuccess(callback, device);
            }
        });
    }

    @Override
    public void setWifiParam(String deviceId, String wifiSsid, String wifiPwd, VoidCallback callback) {
        long ownerId = 0;
        setWifiParamAndOwner(deviceId, false, ownerId, true, wifiSsid, wifiPwd,
                callback);
    }

    @Override
    public void setOwnerId(String deviceId, long ownerId, VoidCallback callback) {
        String wifiSsid = Strings.repeat("0", 1);
        String wifiPwd = Strings.repeat("0", 1);
        setWifiParamAndOwner(deviceId, true, ownerId, false, wifiSsid, wifiPwd,
                callback);
    }


    protected void setWifiParamAndOwner(String deviceId, boolean isSetOwner,
                                        long ownerId, boolean isSetWifi, String wifiSsid, String wifiPwd,
                                        VoidCallback callback) {
        try {
            Msg req = Msg
                    .newRequestMsg(MsgKeys.SetWifiParamsAndOwner_Req, deviceId);

            req.putOpt(MsgParams.IsSetOwner, isSetOwner);
            String strOwnerId = String.valueOf(ownerId);
            strOwnerId = Strings.padEnd(strOwnerId, 10, '\0');
            Preconditions.checkState(strOwnerId.length() == 10, "invalid ownerId");
            req.putOpt(MsgParams.OwnerId, strOwnerId);

            req.putOpt(MsgParams.IsSetWifi, isSetWifi);
            req.putOpt(MsgParams.WifiSsid, wifiSsid);
            req.putOpt(MsgParams.WifiPwd, wifiPwd);
            req.putOpt(MsgParams.WifiSsid_Len, wifiSsid.length());
            req.putOpt(MsgParams.WifiPwd_Len, wifiPwd.length());

            asyncSend(deviceId, req, new RCMsgCallbackWithVoid(callback));
        } catch (Exception e) {
            Helper.onFailure(callback, e);
        }

    }


}
