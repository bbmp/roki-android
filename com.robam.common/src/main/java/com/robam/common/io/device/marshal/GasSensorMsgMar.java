package com.robam.common.io.device.marshal;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.utils.LogUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2018/5/11.
 */

public class GasSensorMsgMar {

    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;
        switch (key) {
            case MsgKeys.GasSensor_Status_Check_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                break;
            case MsgKeys.GasSensor_SetCheckSelf_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                break;
            default:
                break;

        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        switch (key) {
            case MsgKeys.GasSensor_Status_Check_Rep:
                msg.putOpt(MsgParams.GasSensorStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.GasCon,
                        MsgUtils.getInt(payload, offset++));
                offset++;
                offset++;
                offset++;
                short argument1 = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.ArgumentNumber,
                        argument1);
                break;
            case MsgKeys.GasSensor_SetCheckSelf_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.GasSensor_Status_Noti:
                msg.putOpt(MsgParams.GasSensorStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.GasCon,
                        MsgUtils.getInt(payload, offset++));
                offset++;
                offset++;
                offset++;
                short argument2 = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.ArgumentNumber, argument2);
                break;
            case MsgKeys.GasSensor_Alarm_Noti:
                msg.putOpt(MsgParams.Alarm,
                        MsgUtils.getShort(payload[offset++]));
                short alarmArgu = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.ArgumentNumber, alarmArgu);
                break;
            default:
                break;
        }
    }
}
