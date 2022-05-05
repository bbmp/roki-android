package com.robam.common.pojos.device.hidkit;

import com.legent.VoidCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.events.HidKitStatusChangedEvent;
import com.robam.common.events.TheUpgradeHidKitEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;

import java.io.Serializable;

/**
 * @author: lixin
 * @email: lx86@myroki.com
 * @date: 2020/10/16.
 * @PS:
 */
public class AbsHidKit extends AbsDeviceHub implements IHidKit, Serializable {


    protected short terminalType = TerminalType.getType();
    public short rssi;
    public short volume;
    public String ssid;

    public AbsHidKit(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public void onPolling() {
        try {
            Msg reqMsg = newReqMsg(MsgKeys.getHidkitStatus_Req);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);// 控制端类型区分
            sendMsg(reqMsg, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        try {
            int key = msg.getID();
            LogUtils.i("20201030", "msg:" + msg.toString());
            switch (key) {
                case MsgKeys.getHidkitStatus_Rep:
                    AbsHidKit.this.rssi = (short) msg.optInt(MsgParams.RSSI);
                    AbsHidKit.this.volume = (short) msg.optInt(MsgParams.Volume);
                    AbsHidKit.this.ssid = msg.optString(MsgParams.SSID);
                    onStatusChanged();
                    break;
                case MsgKeys.getHidkitEventReport:
                    short argument = (short) msg.optInt(MsgParams.ArgumentNumber);
                    while (argument > 0) {
                        LogUtils.i("20201111", "argument:" + argument);
                        int keyEvent = msg.optInt(MsgParams.Key);
                        LogUtils.i("20201111", "keyEvent 2:" + keyEvent);
                        switch (keyEvent) {
                            case 148:
                                int the_upgrade_len = msg.optInt(MsgParams.the_upgrade_len);
                                LogUtils.i("20201111", "2 the_upgrade_len:" + the_upgrade_len);
                                int the_upgrade_val = msg.optInt(MsgParams.the_upgrade_val);
                                LogUtils.i("20201111", "2 the_upgrade_val:" + the_upgrade_val);
                                EventUtils.postEvent(new TheUpgradeHidKitEvent(the_upgrade_val));
                                break;
                        }

                        argument--;
                    }

                    break;


                default:
                    break;
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged() {
        postEvent(new HidKitStatusChangedEvent(AbsHidKit.this));
    }


    @Override
    public void setHidKitStatusCombined(short argumentNumber, short key, short len,
                                        short value, VoidCallback callback) {
        try {

            Msg msg = newReqMsg(MsgKeys.setHidkitStatusCombined_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                if (key == 1) {
                    msg.putOpt(MsgParams.Key, key);
                    msg.putOpt(MsgParams.VolumeControlLen, len);
                    msg.putOpt(MsgParams.VolumeControlValue, value);
                }

                if (key == 2) {
                    msg.putOpt(MsgParams.Key, key);
                    msg.putOpt(MsgParams.startupgrade_len, len);
                    msg.putOpt(MsgParams.startupgrade_val, value);
                }
            }

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20201030", "resMsg:" + resMsg.toString());
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
