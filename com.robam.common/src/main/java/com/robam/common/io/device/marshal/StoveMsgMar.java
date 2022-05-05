package com.robam.common.io.device.marshal;

import com.google.common.collect.Lists;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.device.Stove.Stove;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by as on 2017-08-09.
 */

public class StoveMsgMar {
    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;
        // 电磁灶
        switch (key) {

            case MsgKeys.GetStoveStatus_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);

                break;
            case MsgKeys.SetStoveStatus_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                bool = msg.optBoolean(MsgParams.IsCook);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                b = (byte) msg.optInt(MsgParams.IhId);
                buf.put(b);
                //
                b = (byte) msg.optInt(MsgParams.IhStatus);
                buf.put(b);

                break;
            case MsgKeys.SetStoveLevel_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                bool = msg.optBoolean(MsgParams.IsCook);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                b = (byte) msg.optInt(MsgParams.IhId);
                buf.put(b);
                //
                b = (byte) msg.optInt(MsgParams.IhLevel);
                buf.put(b);
                break;
            case MsgKeys.SetStoveShutdown_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);

                //
                b = (byte) msg.optInt(MsgParams.IhId);
                buf.put(b);

                //
                s = (short) msg.optInt(MsgParams.IhTime);
                buf.putShort(s);
                break;
            case MsgKeys.SetStoveLock_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);

                //
                bool = msg.optBoolean(MsgParams.StoveLock);
                buf.put(bool ? (byte) 1 : (byte) 0);

                break;

            case MsgKeys.SetPowerOn_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b= (byte) msg.optInt(MsgParams.AutoPowerOff);
                buf.put(b);
                b= (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                break;

            case MsgKeys.setTimePowerOff_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.StoveLevel);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.AutoPowerOffTime);
                buf.put(b);
                b= (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                break;
            case MsgKeys.setPowerOff_Look_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                break;

            default:
                break;
        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        // 电磁灶
        switch (key) {
            case MsgKeys.GetStoveStatus_Rep:
                int num = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.IhNum, num);
                msg.putOpt(MsgParams.StoveLock,
                        MsgUtils.getShort(payload[offset++]) == 1);

                List<Stove.StoveHead> list = Lists.newArrayList();
                for (short i = 0; i < num; i++) {
                    Stove.StoveHead head = new Stove.StoveHead(i);
                    head.status = MsgUtils.getShort(payload[offset++]);
                    head.level = MsgUtils.getShort(payload[offset++]);
                    head.time = MsgUtils.getShort(payload, offset);
                    offset += 2;
                    head.alarmId = MsgUtils.getShort(payload[offset++]);
                    list.add(head);
                }
                //params长度
                short paramsLen = MsgUtils.getShort(payload[offset++]);
                //9w851工作时间
                for (short i = 0; i < paramsLen; i++) {
                    short paramsKey = MsgUtils.getShort(payload[offset++]);
                    short valueLen;
                    short workTime;
                    if (paramsKey == 65) {
                        valueLen = MsgUtils.getShort(payload[offset++]);
                        offset += valueLen;
                    } else if (paramsKey == 66) {
                        valueLen = MsgUtils.getShort(payload[offset++]);
                        offset += valueLen;
                    } else if (paramsKey == 67) {
                        valueLen = MsgUtils.getShort(payload[offset++]);
                        list.get(0).workTime = MsgUtils.getShort(payload, offset);
                        offset += valueLen;
                    } else if (paramsKey == 68) {
                        valueLen = MsgUtils.getShort(payload[offset++]);
                        list.get(1).workTime = MsgUtils.getShort(payload, offset);
                        offset += valueLen;
                    }
                }
                msg.putOpt(MsgParams.StoveHeadList, list);
                break;
            case MsgKeys.SetStoveStatus_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IhId,
                        MsgUtils.getShort(payload[offset++]));

                break;
            case MsgKeys.SetStoveLevel_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IhId,
                        MsgUtils.getShort(payload[offset++]));

                break;
            case MsgKeys.SetStoveShutdown_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IhId,
                        MsgUtils.getShort(payload[offset++]));

                break;
            case MsgKeys.SetStoveLock_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;

            // -------------------------------------------------------------------------------
            // 通知类
            // -------------------------------------------------------------------------------

            case MsgKeys.StoveAlarm_Noti:
                msg.putOpt(MsgParams.IhId,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.AlarmId,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.StoveEvent_Noti:
                msg.putOpt(MsgParams.IhId,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.EventId,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.EventParam,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.StoveTemp_Noti:        //灶具温度参数上报
                msg.putOpt(MsgParams.Pot_Temp,
                        MsgUtils.getFloat(payload, offset));
                break;
            case MsgKeys.SetPowerOn_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setTimePowerOff_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setPowerOff_Look_Rep:
                msg.putOpt(MsgParams.AutoPowerOff,MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.AutoPowerOffTimeForOne,MsgUtils.getShort(payload,offset++));
                offset++;
                msg.putOpt(MsgParams.AutoPowerOffTimeForTwo,MsgUtils.getShort(payload,offset++));
                offset++;
                msg.putOpt(MsgParams.AutoPowerOffTimeForThree,MsgUtils.getShort(payload,offset++));
                offset++;
                msg.putOpt(MsgParams.AutoPowerOffTimeForFour,MsgUtils.getShort(payload,offset++));
                offset++;
                msg.putOpt(MsgParams.AutoPowerOffTimeForFive,MsgUtils.getShort(payload,offset++));
                offset++;
                break;

            default:
                break;
        }
    }
}
