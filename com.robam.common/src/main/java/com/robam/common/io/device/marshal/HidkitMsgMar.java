package com.robam.common.io.device.marshal;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.utils.LogUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

import java.nio.ByteBuffer;

/**
 * @author: lixin
 * @email: lx86@myroki.com
 * @date: 2020/10/16.
 * @PS:藏宝盒协议
 */
public class HidkitMsgMar {

    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {

        byte b;
        switch (key) {

            case MsgKeys.getHidkitStatus_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);

                break;
            case MsgKeys.setHidkitStatusCombined_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                int arg = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put((byte) arg);
                int volumeControlKey = (byte) msg.optInt(MsgParams.Key);
                buf.put((byte) volumeControlKey);
                if (arg > 0) {
                    if (volumeControlKey == 1) {
                        b = (byte) msg.optInt(MsgParams.VolumeControlLen);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.VolumeControlValue);
                        buf.put(b);
                    }

                    if (volumeControlKey == 2) {
                        b = (byte) msg.optInt(MsgParams.startupgrade_len);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.startupgrade_val);
                        buf.put(b);
                    }
                }
                break;

            default:
                break;
        }
    }

    public static void unmashaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        LogUtils.i("20201109", "unmashaller Key:" + key);
        switch (key) {
            case MsgKeys.getHidkitStatus_Rep:


                short argument = MsgUtils.getShort(payload[offset++]);
                //参数个数
                msg.putOpt(MsgParams.ArgumentNumber,
                        argument);

                //取可变参数值
                while (argument > 0) {
                    short argument_key = MsgUtils.getShort(payload[offset++]);

                    switch (argument_key) {
                        case 1:
                            msg.putOpt(MsgParams.Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.RSSI,
                                    MsgUtils.getShort(payload[offset++]));

                            break;
                        case 2:
                            msg.putOpt(MsgParams.Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.Volume,
                                    MsgUtils.getShort(payload[offset++]));

                            break;
                        case 3:
                            msg.putOpt(MsgParams.Length,
                                    MsgUtils.getShort(payload[offset++]));

                            msg.putOpt(MsgParams.SSID,
                                    MsgUtils.getString(payload, offset, payload.length - offset));

                            break;
                    }

                    argument--;
                }

                break;

            case MsgKeys.setHidkitStatusCombined_Rep:

                short RC = MsgUtils.getShort(payload[offset++]);
                LogUtils.i("20201109", "RC:" + RC);
                msg.putOpt(MsgParams.ArgumentNumber,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.Key,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));

                break;

            case MsgKeys.getHidkitEventReport:

                short ArgumentNumber = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.ArgumentNumber,
                        ArgumentNumber);
                LogUtils.i("20201111", "ArgumentNumber:" + ArgumentNumber);
                while (ArgumentNumber > 0) {

                    short keyEvent = MsgUtils.getShort(payload[offset++]);
                    LogUtils.i("20201111", "keyEvent:" + keyEvent);
                    msg.putOpt(MsgParams.Key, key);
                    switch (keyEvent) {
                        case 1:
                            msg.putOpt(MsgParams.newVersion_len,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.newVersion_val,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 2:
                            short the_upgrade_len = MsgUtils.getShort(payload[offset++]);
                            LogUtils.i("20201111", "the_upgrade_len:" + the_upgrade_len);
                            msg.putOpt(MsgParams.the_upgrade_len,
                                    the_upgrade_len);
                            short the_upgrade_val = MsgUtils.getShort(payload[offset++]);
                            LogUtils.i("20201111", "the_upgrade_val:" + the_upgrade_val);
                            msg.putOpt(MsgParams.the_upgrade_val,
                                    the_upgrade_val);
                            break;
                    }
                    ArgumentNumber--;
                }


                break;

            default:
                break;
        }
    }
}
