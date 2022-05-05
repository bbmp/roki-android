package com.robam.common.io.device.marshal;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

import java.nio.ByteBuffer;

/**
 * Created by as on 2017-08-09.
 */

public class PotMsgMar {
    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;
        switch (key) {
            case MsgKeys.GetPotTemp_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                break;
            case MsgKeys.SetPotCom_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.potBurningWarnSwitch);
                buf.put(b);
                break;
            case MsgKeys.SetPotSwitch_Req:

                break;
            default:
                break;
        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        switch (key) {
            case MsgKeys.SetPotTemp_Rep:
                msg.putOpt(MsgParams.Pot_Temp,
                        MsgUtils.getFloat(payload, offset++));
                offset++;
                offset++;
                offset++;
                msg.putOpt(MsgParams.Pot_status,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.ActiveTemp_Rep:
                msg.putOpt(MsgParams.Pot_Temp,
                        MsgUtils.getFloat(payload, offset++));

                break;
            case MsgKeys.PotKey_Report:
                msg.putOpt(MsgParams.Pot_keybood,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.PotAlarm_Report:
                msg.putOpt(MsgParams.AlarmId,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.GetPotCom_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;

            case MsgKeys.GetPotSwitch_Rep:
                msg.putOpt(MsgParams.potBurningWarnSwitch,
                        MsgUtils.getShort(payload[offset++]));
                break;
            default:
                break;
        }
    }
}
